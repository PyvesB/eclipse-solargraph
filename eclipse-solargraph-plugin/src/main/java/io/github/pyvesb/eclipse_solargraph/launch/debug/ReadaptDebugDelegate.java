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
package io.github.pyvesb.eclipse_solargraph.launch.debug;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.READAPT_PATH;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import io.github.pyvesb.eclipse_solargraph.launch.RubyLaunchShortcut;
import io.github.pyvesb.eclipse_solargraph.preferences.PreferencePage;
import io.github.pyvesb.eclipse_solargraph.utils.GemHelper;

public class ReadaptDebugDelegate extends DSPLaunchDelegate {

	private static final AtomicBoolean HAS_UPDATED_READAPT = new AtomicBoolean();

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		if (!new File(READAPT_PATH.getValue()).exists()) {
			displayNotFoundWarning();
			return;
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("program", configuration.getAttribute(RubyLaunchShortcut.SCRIPT, ""));
		parameters.put("runtimeArgs", configuration.getAttribute(RubyLaunchShortcut.ARGUMENTS, ""));
		parameters.put("cwd", configuration.getAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, ""));
		parameters.put("request", "launch");

		DSPLaunchDelegateLaunchBuilder builder = new DSPLaunchDelegateLaunchBuilder(configuration, mode, launch, monitor);
		builder.setLaunchDebugAdapter(READAPT_PATH.getValue(), Collections.singletonList("stdio"));
		builder.setDspParameters(parameters);
		super.launch(builder);

		if (UPDATE_GEM.getValue() && !HAS_UPDATED_READAPT.getAndSet(true)) {
			GemHelper.scheduleUpdate("Readapt", 5000L);
		}
	}

	private void displayNotFoundWarning() {
		Display display = Display.getDefault();
		display.asyncExec(() -> {
			MessageDialog dialog = new MessageDialog(display.getActiveShell(), "Readapt was not found", null,
					"Readapt is required for debugging. Let Eclipse install the gem locally or specify its path "
							+ "after running \"gem install readapt\" in a terminal." + System.lineSeparator()
							+ System.lineSeparator() + "Please restart the debug session once installation is complete.",
					MessageDialog.WARNING, 0, "Install gem", "Specify path");
			if (dialog.open() == 0) { // First button index, install.
				GemHelper.install("Readapt", READAPT_PATH);
				HAS_UPDATED_READAPT.set(true);
			} else {
				PreferencesUtil.createPreferenceDialogOn(null, PreferencePage.PAGE_ID, null, null).open();
			}
		});
	}

}
