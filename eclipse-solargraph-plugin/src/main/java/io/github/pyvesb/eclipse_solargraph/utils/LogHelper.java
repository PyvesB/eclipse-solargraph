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

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class LogHelper {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(LogHelper.class);
	private static final String LOGNAME = BUNDLE.getSymbolicName();
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	public static void log(int severity, String message) {
		LOGGER.log(new Status(severity, LOGNAME, message));
	}

	public static void log(int severity, String message, Throwable throwable) {
		LOGGER.log(new Status(severity, LOGNAME, message, throwable));
	}

	public static void info(String message) {
		log(IStatus.INFO, message);
	}

	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, message, exception);
	}

	public static void error(String message) {
		log(IStatus.ERROR, message);
	}

	public static void cancelled(String message) {
		log(IStatus.CANCEL, message);
	}

	private LogHelper() {
		// Utility class.
	}

}
