package io.github.pyvesb.eclipse_solargraph.utils;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;

public class LogHelper {
	
	private static final ILog LOGGER = SolargraphPlugin.getDefault().getLog();
	
	public static void info(String message) {
		LOGGER.log(new Status(IStatus.INFO, SolargraphPlugin.PLUGIN_ID, message));
	}

	public static void error(String message, Throwable exception) {
		LOGGER.log(new Status(IStatus.ERROR, SolargraphPlugin.PLUGIN_ID, message, exception));
	}
	
	private LogHelper() {
		// Utility class.
	}

}
