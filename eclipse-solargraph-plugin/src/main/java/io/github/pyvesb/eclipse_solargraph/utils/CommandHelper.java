package io.github.pyvesb.eclipse_solargraph.utils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;

public class CommandHelper {
	
	public static List<String> getPlatformCommand(String command) {
		return isWindows() ? Arrays.asList("cmd.exe", "/c", command) : Arrays.asList("/bin/bash", "-c", "-l", command);
	}
	
	public static boolean isWindows() {
		return Platform.OS_WIN32.equals(Platform.getOS());
	}
	
	private CommandHelper() {
		// Utility class.
	}

}
