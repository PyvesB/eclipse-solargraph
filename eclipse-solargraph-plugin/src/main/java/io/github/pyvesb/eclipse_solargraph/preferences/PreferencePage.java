/*******************************************************************************
 * Copyright (c) 2020-2024 Pierre-Yves B. and others.
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
package io.github.pyvesb.eclipse_solargraph.preferences;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.DEBUG_READAPT;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.GEM_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.READAPT_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "io.github.pyvesb.eclipse_solargraph";

	private FileFieldEditor gemPath;
	private FileFieldEditor readaptPath;
	private BooleanFieldEditor systemRubyFieldEditor;
	private DirectoryFieldEditor rubyDirFieldEditor;
	private Composite rubyDirFieldEditorParent;
	private Image gitHubImage;

	@Override
	public void createFieldEditors() {
		gemPath = new FileFieldEditor(GEM_PATH.getKey(), GEM_PATH.getDesc(), true, getFieldEditorParent());
		addField(gemPath);
		readaptPath = new FileFieldEditor(READAPT_PATH.getKey(), READAPT_PATH.getDesc(), true, getFieldEditorParent());
		addField(readaptPath);
		addField(new BooleanFieldEditor(UPDATE_GEM.getKey(), UPDATE_GEM.getDesc(), getFieldEditorParent()));
		systemRubyFieldEditor = new BooleanFieldEditor(SYSTEM_RUBY.getKey(), SYSTEM_RUBY.getDesc(), getFieldEditorParent());
		addField(systemRubyFieldEditor);
		rubyDirFieldEditorParent = getFieldEditorParent();
		GridDataFactory.fillDefaults().indent(30, 0).applyTo(rubyDirFieldEditorParent);
		rubyDirFieldEditor = new DirectoryFieldEditor(RUBY_DIR.getKey(), RUBY_DIR.getDesc(), rubyDirFieldEditorParent);
		rubyDirFieldEditor.setEnabled(!SYSTEM_RUBY.getValue(), rubyDirFieldEditorParent);
		addField(rubyDirFieldEditor);
		addField(new BooleanFieldEditor(DEBUG_READAPT.getKey(), DEBUG_READAPT.getDesc(), getFieldEditorParent()));

		Composite composite = new Composite(rubyDirFieldEditorParent.getParent(), SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 7));
		URL url = FileLocator.find(FrameworkUtil.getBundle(getClass()), new Path("/icon/github.png"));
		gitHubImage = ImageDescriptor.createFromURL(url).createImage();
		new Label(composite, SWT.NONE).setImage(gitHubImage);
		Link supportLink = new Link(composite, SWT.NONE);
		supportLink.setText("Need support? Head over to <a href=\"https://github.com/PyvesB/eclipse-solargraph\">GitHub</a>! "
				+ "If you find the plugin helpful, consider starring the repo ⭐");
		supportLink.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> Program.launch(e.text)));
	}

	@Override
	public void init(IWorkbench workbench) {
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, bundle.getSymbolicName()));
		setDescription("Settings for Ruby development tools:");
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() == systemRubyFieldEditor) {
			boolean systemRuby = Boolean.TRUE.equals(event.getNewValue());
			rubyDirFieldEditor.setEnabled(!systemRuby, rubyDirFieldEditorParent);
		}
		super.propertyChange(event);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (gitHubImage != null) {
			gitHubImage.dispose();
		}
	}

	public FileFieldEditor getPathField(String gem) {
		return "readapt".equalsIgnoreCase(gem) ? readaptPath : gemPath;
	}

}
