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
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.Platform;

public class CommandHelper {

	private static final String MACOS_DSCL_SHELL_PREFIX = "UserShell: ";

	private static final AtomicReference<String> DEFAULT_SHELL_MACOS = new AtomicReference<>();

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
		if (isWindows()) {
			return new String[] { "cmd.exe", "/c", command };
		} else if (isMacOS()) {
			String defaultShellMacOS = DEFAULT_SHELL_MACOS.get();
			if (defaultShellMacOS == null) {
				defaultShellMacOS = findDefaultShellMacOS();
				DEFAULT_SHELL_MACOS.set(defaultShellMacOS);
			}
			return new String[] { defaultShellMacOS, "-c", "-li", command };
		}
		return new String[] { "bash", "-c", "-l", command };
	}

	public static boolean isWindows() {
		return Platform.OS_WIN32.equals(Platform.getOS());
	}

	public static boolean isMacOS() {
		return Platform.OS_MACOSX.equals(Platform.getOS());
	}

	 // Inspired from https://github.com/eclipse-wildwebdeveloper/wildwebdeveloper/blob/0447608f659c1683da4816fff185d34ef54dcf8a/org.eclipse.wildwebdeveloper.embedder.node/src/org/eclipse/wildwebdeveloper/embedder/node/NodeJSManager.java#L297
	private static String findDefaultShellMacOS() {
		String[] dsclCommand = new String[] { "/bin/bash", "-c", "-l", "dscl . -read ~/ UserShell" };
		try {
			Process process = new ProcessBuilder(dsclCommand).redirectErrorStream(true).start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String shell = reader.readLine();
				if (shell != null && shell.startsWith(MACOS_DSCL_SHELL_PREFIX)) {
					return shell.substring(MACOS_DSCL_SHELL_PREFIX.length());
				}
			}
		} catch (IOException e) {
			LogHelper.error("Failed to find location of default macOS shell, using '/bin/zsh' instead.", e);
		}
		return "/bin/zsh"; // Default shell since macOS 10.15.
	}

	private CommandHelper() {
		// Utility class.
	}

}
