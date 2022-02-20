/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Alexander Kurtakov (Red Hat Inc.) - Initial implementation
 *  Pierre-Yves B.  (pyvesdev@gmail.com) - Various improvements
 *******************************************************************************/
package io.github.pyvesb.eclipse_solargraph.launch;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public class RubyLaunchTab extends AbstractLaunchConfigurationTab {

	private Text programPathText;
	private Text argumentsText;
	private Text workingDirectoryText;

	@Override
	public void createControl(Composite parent) {
		Composite resComposite = new Composite(parent, SWT.NONE);
		resComposite.setLayout(new GridLayout(2, false));
		new Label(resComposite, SWT.NONE).setText("Ruby script");
		programPathText = new Text(resComposite, SWT.BORDER);
		programPathText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		ControlDecoration decoration = new ControlDecoration(programPathText, SWT.TOP | SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_ERROR);
		decoration.setImage(fieldDecoration.getImage());
		programPathText.addModifyListener(event -> {
			setDirty(true);
			File file = new File(programPathText.getText());
			if (!file.isFile()) {
				String errorMessage = "Unknown file";
				setErrorMessage(errorMessage);
				decoration.setDescriptionText(errorMessage);
				decoration.show();
			} else if (!file.canRead()) {
				String errorMessage = "Non readable file";
				setErrorMessage(errorMessage);
				decoration.setDescriptionText(errorMessage);
				decoration.show();
			} else {
				setErrorMessage(null);
				decoration.hide();
			}
			updateLaunchConfigurationDialog();
		});
		new Label(resComposite, SWT.NONE).setText("Arguments");
		argumentsText = new Text(resComposite, SWT.BORDER);
		argumentsText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		argumentsText.addModifyListener(e -> {
			setDirty(true);
			updateLaunchConfigurationDialog();
		});
		new Label(resComposite, SWT.NONE).setText("Working directory");
		workingDirectoryText = new Text(resComposite, SWT.BORDER);
		workingDirectoryText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		workingDirectoryText.addModifyListener(e -> {
			setDirty(true);
			updateLaunchConfigurationDialog();
		});
		setControl(resComposite);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// Nothing to do
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			programPathText.setText(configuration.getAttribute(RubyLaunchShortcut.SCRIPT, ""));
			argumentsText.setText(configuration.getAttribute(RubyLaunchShortcut.ARGUMENTS, ""));
			workingDirectoryText.setText(configuration.getAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, ""));
		} catch (CoreException e) {
			LogHelper.error("Exception whilst initializing launch configuration tab", e);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(RubyLaunchShortcut.SCRIPT, programPathText.getText());
		configuration.setAttribute(RubyLaunchShortcut.ARGUMENTS, argumentsText.getText());
		configuration.setAttribute(DebugPlugin.ATTR_WORKING_DIRECTORY, workingDirectoryText.getText());
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public Image getImage() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL url = FileLocator.find(bundle, new Path("/icon/ruby-editor.png"));
		return ImageDescriptor.createFromURL(url).createImage();
	}
}
