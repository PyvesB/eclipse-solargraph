/*******************************************************************************
 * Copyright (c) 2019 Pierre-Yves B. and others.
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
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.FrameworkUtil;

import io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences;

public class GemHelper {

	private static String buildGemCmd(String cmd, String gem) {
		return String.format(
			"gem %s -V -n \"%s\" \"%s\"",
			cmd, getPluginStateLocation(), gem.toLowerCase()
		);
	}
	
	public static void install(String gem, StringPreferences pathPreference) {
		String[] command = CommandHelper.getPlatformCommand(buildGemCmd("install", gem));
		CommandJob installCommandJob = new CommandJob(gem, command, "Installation in progress");

		installCommandJob.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult() == Status.OK_STATUS) {
					String extension = CommandHelper.isWindows() ? ".bat" : "";
					String gemPath = getPluginStateLocation() + File.separator + gem + extension;
					pathPreference.setValue(gemPath);
				} else {
					Display display = Display.getDefault();
					display.asyncExec(() -> MessageDialog.openError(display.getActiveShell(), gem + " intallation failed",
							"Please open the Error Log view for details. To manually install it, run \"gem install " + gem
									+ "\" in a terminal and specify the path in the plugin's preferences."));
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

	private GemHelper() {
		// Utility class.
	}

}
