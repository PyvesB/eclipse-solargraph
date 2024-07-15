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

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.Platform;

public class CommandHelper {

	public static String findDirectory(String executable) {
		String executablePath = findPath(executable);
		if (!executablePath.isEmpty()) {
			File executableDirectory = new File(executablePath).getParentFile();
			if (executableDirectory != null && executableDirectory.isDirectory()) {
				return executableDirectory.getAbsolutePath();
			}
		}
		return "";
	}

	public static String findPath(String executable) {
		String locationCommand = (isWindows() ? "where " : "which ") + executable;
		try {
			Process process = new ProcessBuilder(getPlatformCommand(locationCommand)).redirectErrorStream(true).start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String path = reader.readLine();
				if (path != null && new File(path).exists()) {
					return path;
				}
			}
		} catch (IOException e) {
			LogHelper.error("Failed to find location of " + executable, e);
		}
		return "";
	}

	public static String[] getAbsolutePlatformCommand(String command) {
		if (!SYSTEM_RUBY.getValue()) {
			String rubyDir = RUBY_DIR.getValue();
			if (rubyDir != null && !rubyDir.isEmpty()) {
				return CommandHelper.getPlatformCommand(rubyDir + File.separator + command);
			}
		}
		return CommandHelper.getPlatformCommand(command);
	}

	public static String[] getPlatformCommand(String command) {
		return isWindows() ? new String[] { "cmd.exe", "/c", command } : new String[] { "bash", "-c", "-l", command };
	}

	public static boolean isWindows() {
		return Platform.OS_WIN32.equals(Platform.getOS());
	}

	private CommandHelper() {
		// Utility class.
	}

}
