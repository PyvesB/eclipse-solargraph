package io.github.pyvesb.eclipse_solargraph.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public class ConfigHelper {

	public static String getConfigString(ILaunchConfiguration configuration, String name) {
		return getConfigString(configuration, name, "");
	}

	public static String getConfigString(ILaunchConfiguration configuration, String name, String defaultValue) {
		try {
			return configuration.getAttribute(name, defaultValue);
		} catch (CoreException e) {
			LogHelper.error("Unable to access configuration attribute: " + name, e);
			return null;
		}
	}

}
