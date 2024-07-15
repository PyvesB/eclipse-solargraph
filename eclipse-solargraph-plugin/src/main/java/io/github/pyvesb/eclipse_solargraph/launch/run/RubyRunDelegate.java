/*******************************************************************************
 * Copyright (c) 2019-2024 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Alexander Kurtakov  (Red Hat Inc.) - Initial implementation
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Various improvements
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Debugger support
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Quote script path
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch.run;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import io.github.pyvesb.eclipse_solargraph.launch.RubyLaunchShortcut;
import io.github.pyvesb.eclipse_solargraph.utils.ConfigHelper;
import io.github.pyvesb.eclipse_solargraph.utils.LaunchHelper;

public class RubyRunDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) {
		String script = ConfigHelper.getConfigString(configuration, RubyLaunchShortcut.SCRIPT);
		String arguments = ConfigHelper.getConfigString(configuration, RubyLaunchShortcut.ARGUMENTS);
		String workingDirectory = ConfigHelper.getConfigString(configuration, DebugPlugin.ATTR_WORKING_DIRECTORY);
		if (script == null || arguments == null || workingDirectory == null) {
			// a config attribute reader threw exception & was logged
			return;
		}
		String command = "ruby \"" + script + "\" " + arguments;
		LaunchHelper.createJob(launch, command, workingDirectory).schedule();
	}

}
