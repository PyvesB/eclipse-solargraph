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
package io.github.pyvesb.eclipse_solargraph.server;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.GEM_PATH;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import io.github.pyvesb.eclipse_solargraph.preferences.PreferencePage;
import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;
import io.github.pyvesb.eclipse_solargraph.utils.GemHelper;

public class SolargraphStreamConnectionProvider extends ProcessStreamConnectionProvider {

	private static final AtomicBoolean HAS_DISPLAYED_NOT_FOUND_WARNING = new AtomicBoolean();
	private static final AtomicBoolean HAS_UPDATED_SOLARGRAPH = new AtomicBoolean();

	public SolargraphStreamConnectionProvider() {
		super(getSolargraphCommand(), System.getProperty("user.dir"));
	}

	@Override
	public Object getInitializationOptions(URI rootUri) {
		return Map.of("diagnostics", true);
	}

	@Override
	public void start() throws IOException {
		if (getCommands().isEmpty()) {
			// Attempt to find and set Solargraph command again - the gem may have been installed in the meantime.
			setCommands(getSolargraphCommand());
		}
		if (getCommands().isEmpty() && !HAS_DISPLAYED_NOT_FOUND_WARNING.getAndSet(true)) {
			displayNotFoundWarning();
		}
		super.start();
		if (UPDATE_GEM.getValue() && !HAS_UPDATED_SOLARGRAPH.getAndSet(true)) {
			GemHelper.scheduleUpdate("Solargraph", 30000L);
		}
	}

	private static List<String> getSolargraphCommand() {
		String gemPath = GEM_PATH.getValue();
		if (gemPath != null && new File(gemPath).exists()) {
			return List.of(CommandHelper.getPlatformCommand("\"" + gemPath + "\" stdio"));
		}
		return List.of();
	}

	private void displayNotFoundWarning() {
		Display display = Display.getDefault();
		display.asyncExec(() -> {
			MessageDialog notFoundDialog = new MessageDialog(display.getActiveShell(), "Solargraph gem not found", null,
					"Solargraph is required for key features. Let Eclipse install the gem locally, or specify its path "
							+ "after running \"gem install solargraph\" in a terminal.",
					MessageDialog.WARNING, 0, "Install gem", "Specify path");
			int buttonIndex = notFoundDialog.open();
			if (buttonIndex == 0) {
				GemHelper.install("Solargraph", GEM_PATH);
				HAS_UPDATED_SOLARGRAPH.set(true);
			} else if (buttonIndex == 1) {
				PreferenceDialog preferenceDialog = PreferencesUtil.createPreferenceDialogOn(null, PreferencePage.PAGE_ID,
						null, null);
				((PreferencePage) preferenceDialog.getSelectedPage()).getPathField("solargraph").setFocus();
				preferenceDialog.open();
			}
		});
	}

}
