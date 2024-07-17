/*******************************************************************************
 * Copyright (c) 2022 Sean Champ and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Sean Champ - Initial implementation
 *******************************************************************************/
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
