package io.github.pyvesb.eclipse_solargraph.utils;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class LogHelper {
	
	private static final Bundle BUNDLE = FrameworkUtil.getBundle(LogHelper.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);
	
	public static void info(String message) {
		LOGGER.log(new Status(IStatus.INFO, BUNDLE.getSymbolicName(), message));
	}

	public static void error(String message, Throwable exception) {
		LOGGER.log(new Status(IStatus.ERROR, BUNDLE.getSymbolicName(), message, exception));
	}
	
	private LogHelper() {
		// Utility class.
	}

}
