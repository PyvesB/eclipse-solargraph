/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others.
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
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch.run;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import io.github.pyvesb.eclipse_solargraph.launch.RubyLaunchShortcut;
import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;

public class RubyRunDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String script = configuration.getAttribute(RubyLaunchShortcut.SCRIPT, "");
		String arguments = configuration.getAttribute(RubyLaunchShortcut.ARGUMENTS, "");
		String workingDirectory = configuration.getAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, "");
		String command = "ruby " + script + " " + arguments;
		String[] absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		Job.create("Running " + command, r -> {
			Process process = DebugPlugin.exec(absolutePlatformCommand, new File(workingDirectory));
			DebugPlugin.newProcess(launch, process, command);
		}).schedule();
	}

}
