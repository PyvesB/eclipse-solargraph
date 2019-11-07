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
package io.github.pyvesb.eclipse_solargraph.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;

import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;

public class BundleGemLaunchShortcut implements ResourceLaunchShortcut {

	@Override
	public void launchResource(IResource resource, String mode) {
		Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		String command = getBaseCommand(resource) + resource.getLocation().toOSString();
		String[] absolutePlatformCommand = CommandHelper.getAbsolutePlatformCommand(command);
		Job.create("Running " + command, r -> {
			Process process = DebugPlugin.exec(absolutePlatformCommand, null);
			DebugPlugin.newProcess(launch, process, command);
		}).schedule();
	}

	private String getBaseCommand(IResource resource) {
		return "Gemfile".equals(resource.getName()) ? "bundle install --gemfile=" : "gem build ";
	}

}
