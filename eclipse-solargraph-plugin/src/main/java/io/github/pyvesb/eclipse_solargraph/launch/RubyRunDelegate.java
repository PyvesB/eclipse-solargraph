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
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;
import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class RubyRunDelegate extends LaunchConfigurationDelegate implements ResourceLaunchShortcut {

	static final String SCRIPT = "SCRIPT";
	static final String ARGUMENTS = "ARGUMENTS";
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String program = configuration.getAttribute(SCRIPT, "");
		String arguments = configuration.getAttribute(ARGUMENTS, "");
		String workingDirectory = configuration.getAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, "");
		String command = "ruby " + program + " " + arguments;
		String[] absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		Job.create("Running " + command, r -> {
			Process process = DebugPlugin.exec(absolutePlatformCommand, new File(workingDirectory));
			DebugPlugin.newProcess(launch, process, command);
		}).schedule();
	}

	@Override
	public void launchResource(IResource resource, String mode) {
		try {
			ILaunchConfiguration launchConfig = getLaunchConfiguration(resource);
			launchConfig.launch(mode, new NullProgressMonitor());
		} catch (CoreException e) {
			LogHelper.error("Exception whilst launching " + resource.getName(), e);
			Display display = Display.getDefault();
			display.asyncExec(() -> MessageDialog.openError(display.getActiveShell(), "Launch failed",
					"Please open the Error Log view for details."));
		}
	}

	private ILaunchConfiguration getLaunchConfiguration(IResource resource) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType configType = launchManager.getLaunchConfigurationType(getClass().getCanonicalName());
		
		for (ILaunchConfiguration launchConfig : launchManager.getLaunchConfigurations(configType)) {
			if (launchConfig.getName().equals(resource.getName())) {
				return launchConfig;
			}
		}
		
		String configName = launchManager.generateLaunchConfigurationName(resource.getName());
		ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, configName);
		wc.setAttribute(SCRIPT, resource.getLocation().toOSString());
		wc.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, resource.getProject().getLocation().toOSString());
		return wc.doSave();
	}

}
