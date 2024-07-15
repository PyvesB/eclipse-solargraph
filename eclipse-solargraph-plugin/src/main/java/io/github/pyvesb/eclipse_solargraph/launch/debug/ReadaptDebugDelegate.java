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
package io.github.pyvesb.eclipse_solargraph.launch.debug;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.DEBUG_READAPT;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.READAPT_PATH;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import io.github.pyvesb.eclipse_solargraph.launch.RubyLaunchShortcut;
import io.github.pyvesb.eclipse_solargraph.preferences.PreferencePage;
import io.github.pyvesb.eclipse_solargraph.utils.ConfigHelper;
import io.github.pyvesb.eclipse_solargraph.utils.GemHelper;
import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class ReadaptDebugDelegate extends DSPLaunchDelegate {

	private static final AtomicBoolean HAS_UPDATED_READAPT = new AtomicBoolean();

	private static final List<String> READAPT_STDIO = List.of("stdio");
	private static final Long READAPT_UPDATE_DELAY = 5000L;

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) {
		String readaptPath = READAPT_PATH.getValue();
		if (readaptPath == null || !new File(readaptPath).exists()) {
			displayNotFoundWarning();
			return;
		}

		DSPLaunchDelegateLaunchBuilder builder = new DSPLaunchDelegateLaunchBuilder(configuration, mode, launch, monitor);
		builder.setLaunchDebugAdapter(readaptPath, READAPT_STDIO);
		builder.setMonitorDebugAdapter(DEBUG_READAPT.getValue());
		builder.setDspParameters(Map.of(
				"program", ConfigHelper.getConfigString(configuration, RubyLaunchShortcut.SCRIPT),
				"runtimeArgs", ConfigHelper.getConfigString(configuration, RubyLaunchShortcut.ARGUMENTS),
				"cwd", ConfigHelper.getConfigString(configuration, DebugPlugin.ATTR_WORKING_DIRECTORY),
				"request", "launch"));
		try {
			super.launch(builder);
		} catch (CoreException e) {
			String msg = "Exception when launching readapt: " + readaptPath + " " + String.join(" ", READAPT_STDIO);
			LogHelper.error(msg);
			return;
		}

		if (UPDATE_GEM.getValue() && !HAS_UPDATED_READAPT.getAndSet(true)) {
			GemHelper.scheduleUpdate("Readapt", READAPT_UPDATE_DELAY);
		}
	}

	private void displayNotFoundWarning() {
		Display display = Display.getDefault();
		display.asyncExec(() -> {
			MessageDialog notFoundDialog = new MessageDialog(display.getActiveShell(), "Readapt gem not found", null,
					"Readapt is required for debugging. Let Eclipse install the gem locally, or specify its path "
							+ "after running \"gem install readapt\" in a terminal." + System.lineSeparator()
							+ System.lineSeparator()
							+ "Please restart the debug session once the installation is complete.",
					MessageDialog.WARNING, 0, "Install gem", "Specify path");
			int buttonIndex = notFoundDialog.open();
			if (buttonIndex == 0) {
				GemHelper.install("Readapt", READAPT_PATH);
				HAS_UPDATED_READAPT.set(true);
			} else if (buttonIndex == 1) {
				PreferenceDialog preferenceDialog = PreferencesUtil.createPreferenceDialogOn(null, PreferencePage.PAGE_ID,
						null, null);
				((PreferencePage) preferenceDialog.getSelectedPage()).getPathField("readapt").setFocus();
				preferenceDialog.open();
			}
		});
	}

}
