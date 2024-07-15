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
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class RubyLaunchShortcut implements IResourceLaunchShortcut {

	public static final String SCRIPT = "SCRIPT";
	public static final String ARGUMENTS = "ARGUMENTS";

	@Override
	public void launchResource(IResource resource, String mode) {
		try {
			ILaunchConfiguration launchConfig = getLaunchConfiguration(resource);
			DebugUITools.launch(launchConfig, mode);
		} catch (CoreException e) {
			LogHelper.error("Exception whilst launching " + resource.getName(), e);
			Display display = Display.getDefault();
			display.asyncExec(() -> MessageDialog.openError(display.getActiveShell(), "Launch failed",
					"Please check the Error Log view for details."));
		}
	}

	private ILaunchConfiguration getLaunchConfiguration(IResource resource) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType configType = launchManager.getLaunchConfigurationType(getClass().getPackage().getName());

		String scriptLocation = resource.getLocation().toOSString();
		for (ILaunchConfiguration launchConfig : launchManager.getLaunchConfigurations(configType)) {
			if (launchConfig.getAttribute(SCRIPT, "").equals(scriptLocation)) {
				return launchConfig;
			}
		}

		String configName = launchManager.generateLaunchConfigurationName(resource.getName());
		ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, configName);
		wc.setAttribute(SCRIPT, scriptLocation);
		wc.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, resource.getProject().getLocation().toOSString());
		return wc.doSave();
	}

}
