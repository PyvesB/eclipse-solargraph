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
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class RubyRunDelegate extends LaunchConfigurationDelegate implements ILaunchShortcut {

	public static final String PROGRAM = "PROGRAM";
	public static final String ARGUMENTS = "ARGUMENTS";

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
 		String fileName = configuration.getAttribute(PROGRAM, "");
		String argument = configuration.getAttribute(ARGUMENTS, "");
		String workingDir = configuration.getAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, "");
		DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
		String[] command = getFileCommand(fileName, argument);
		String commandSummary = "ruby " + fileName;
		Job.create("Running " + commandSummary, r -> {
			Process process = DebugPlugin.exec(command, new File(workingDir));
			DebugPlugin.newProcess(launch, process, commandSummary);
		}).schedule();
	}

	private String[] getFileCommand(String fileName, String arguments) {
		StringBuilder ruby = new StringBuilder();
		if (!SYSTEM_RUBY.getValue()) {
			ruby.append(RUBY_DIR.getValue());
			if (ruby.length() > 0) {
				ruby.append(File.separator);
			}
		}
		ruby.append("ruby");
		List<String> command = new ArrayList<>();
		command.add(ruby.toString());
		command.add(fileName);
		command.addAll(Arrays.asList(arguments.split(" ")));
		return command.toArray(new String[0]);
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			IResource resource = null;
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				resource = ((IAdaptable) element).getAdapter(IResource.class);
			}

			if (resource != null) {
				try {
					ILaunchConfiguration launchConfig = getLaunchConfiguration(mode, resource);
					launchConfig.launch(mode, new NullProgressMonitor());
				} catch (CoreException e) {
					LogHelper.error("Exception whilst launching " + resource.getName(), e);
				}
				return;
			}
		}
		Display.getDefault().asyncExec(() -> {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
					"Issue running");
		});

	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = input.getAdapter(IFile.class);

		try {
			ILaunchConfiguration launchConfig = getLaunchConfiguration(mode, file);
			launchConfig.launch(mode, new NullProgressMonitor());
		} catch (CoreException e) {
			LogHelper.error("Exception whilst launching " + file.getName(), e);
		}

	}

	private ILaunchConfiguration getLaunchConfiguration(String mode, IResource resource) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType configType = launchManager
				.getLaunchConfigurationType("io.github.pyvesb.eclipse_solargraph.launch.RubyRunDelegate"); //$NON-NLS-1$
		ILaunchConfiguration[] launchConfigurations = launchManager.getLaunchConfigurations(configType);

		String configName = resource.getName();

		for (ILaunchConfiguration iLaunchConfiguration : launchConfigurations) {
			if (iLaunchConfiguration.getName().equals(configName)) {
				return iLaunchConfiguration;
			}
		}
		configName = launchManager.generateLaunchConfigurationName(configName);
		ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, configName);
		wc.setAttribute(PROGRAM, resource.getLocation().toString());
		wc.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, resource.getProject().getLocation().toString());
		wc.doSave();
		return wc;
	}

}
