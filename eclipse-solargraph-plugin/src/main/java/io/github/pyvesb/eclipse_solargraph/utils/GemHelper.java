/*******************************************************************************
 * Copyright (c) 2019-2024 Pierre-Yves B. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Initial implementation
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.utils;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.osgi.framework.FrameworkUtil;

import io.github.pyvesb.eclipse_solargraph.preferences.PreferencePage;
import io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences;

public class GemHelper {

	private static String buildGemCmd(String cmd, String lowerCaseGem) {
		return String.format(
				"gem %s -V -n \"%s\" %s",
				cmd, getPluginStateLocation(), lowerCaseGem);
	}

	public static void install(String gem, StringPreferences pathPreference) {
		String lowerCaseGem = gem.toLowerCase();
		String[] command = CommandHelper.getPlatformCommand(buildGemCmd("install", lowerCaseGem));
		CommandJob installCommandJob = new CommandJob(gem, command, "Installation in progress");

		installCommandJob.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult() == Status.OK_STATUS) {
					String extension = CommandHelper.isWindows() ? ".bat" : "";
					String gemPath = getPluginStateLocation() + File.separator + lowerCaseGem + extension;
					pathPreference.setValue(gemPath);
				} else {
					Display.getDefault().asyncExec(() -> displayInstallationError(gem));
				}
			}
		});
		installCommandJob.schedule();
	}

	public static void scheduleUpdate(String gem, long delay) {
		String[] command = CommandHelper.getPlatformCommand(buildGemCmd("update", gem));
		new CommandJob(gem, command, "Update in progress").schedule(delay);
	}

	private static String getPluginStateLocation() {
		return Platform.getStateLocation(FrameworkUtil.getBundle(GemHelper.class)).toOSString();
	}

	private static void displayInstallationError(String gem) {
		MessageDialog errorDialog = new MessageDialog(Display.getDefault().getActiveShell(), gem + " installation failed",
				null, "Please check the Error Log view for details. To manually install it, run \"gem install "
						+ gem.toLowerCase() + "\" in a terminal and specify the path in the plugin's preferences.",
				MessageDialog.ERROR, 0, "View error logs", "Specify path");
		int buttonIndex = errorDialog.open();
		if (buttonIndex == 0) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView("org.eclipse.pde.runtime.LogView");
			} catch (PartInitException e) {
				LogHelper.error("Failed to open Error Log view.", e);
			}
		} else if (buttonIndex == 1) {
			PreferenceDialog prefDialog = PreferencesUtil.createPreferenceDialogOn(null, PreferencePage.PAGE_ID, null, null);
			((PreferencePage) prefDialog.getSelectedPage()).getPathField(gem).setFocus();
			prefDialog.open();
		}
	}

	private GemHelper() {
		// Utility class.
	}

}
