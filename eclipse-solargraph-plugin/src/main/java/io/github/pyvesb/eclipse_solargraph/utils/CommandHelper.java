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

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.io.File;

import org.eclipse.core.runtime.Platform;

public class CommandHelper {

	public static String[] getAbsolutePlatformCommand(String command) {
		if (!SYSTEM_RUBY.getValue()) {
			String rubyDir = RUBY_DIR.getValue();
			if (rubyDir.length() > 0) {
				return CommandHelper.getPlatformCommand(rubyDir + File.separator + command);
			}
		}
		return CommandHelper.getPlatformCommand(command);
	}

	public static String[] getPlatformCommand(String command) {
		return isWindows() ? new String[] { "cmd.exe", "/c", command } : new String[] { "/bin/bash", "-c", "-l", command };
	}

	public static boolean isWindows() {
		return Platform.OS_WIN32.equals(Platform.getOS());
	}

	private CommandHelper() {
		// Utility class.
	}

}
