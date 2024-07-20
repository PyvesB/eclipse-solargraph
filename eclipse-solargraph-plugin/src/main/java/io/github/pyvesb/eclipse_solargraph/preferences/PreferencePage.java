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
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SOLARGRAPH_DIAGNOSTICS;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.GEM_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.READAPT_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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
		boolean isWindows = Platform.OS_WIN32.equals(Platform.getOS());
		
		Composite parent = getFieldEditorParent();
		parent.setLayout(new GridLayout(1, false));
		Font defaultFont = parent.getFont();

		addPadding(parent, 1);

		Group pathsGroup = new Group(parent, SWT.NONE);
		pathsGroup.setText("Solargraph (language server) and Readapt (debugger) executables");
		pathsGroup.setLayout(new GridLayout(3, false)); // 3 columns for label, input field, and browse button.
		pathsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		if (isWindows) {
			addPadding(pathsGroup, 3);
		}
		Composite gemPathComposite = new Composite(pathsGroup, SWT.NONE);
		gemPathComposite.setLayout(new GridLayout(3, false));
		gemPathComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		gemPath = new FileFieldEditor(GEM_PATH.getKey(), GEM_PATH.getDesc(), gemPathComposite);
		gemPath.getLabelControl(gemPathComposite).setFont(defaultFont);
		addField(gemPath);
		Composite readaptPathComposite = new Composite(pathsGroup, SWT.NONE);
		readaptPathComposite.setLayout(new GridLayout(3, false));
		readaptPathComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		readaptPath = new FileFieldEditor(READAPT_PATH.getKey(), READAPT_PATH.getDesc(), readaptPathComposite);
		readaptPath.getLabelControl(readaptPathComposite).setFont(defaultFont);
		addField(readaptPath);
		BooleanFieldEditor updateGemFieldEditor = new BooleanFieldEditor(UPDATE_GEM.getKey(), UPDATE_GEM.getDesc(),
				pathsGroup);
		updateGemFieldEditor.getDescriptionControl(pathsGroup).setFont(defaultFont);
		addField(updateGemFieldEditor);

		addPadding(parent, 1);

		Group runAsGroup = new Group(parent, SWT.NONE);
		runAsGroup.setText("Run *.rb, Gemfile, and *.gemspec files");
		runAsGroup.setLayout(new GridLayout(1, false));
		runAsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		if (isWindows) {
			addPadding(runAsGroup, 1);
		}
		systemRubyFieldEditor = new BooleanFieldEditor(SYSTEM_RUBY.getKey(), SYSTEM_RUBY.getDesc(), runAsGroup);
		systemRubyFieldEditor.getDescriptionControl(runAsGroup).setFont(defaultFont);
		addField(systemRubyFieldEditor);
		rubyDirFieldEditorParent = new Composite(runAsGroup, SWT.NONE);
		rubyDirFieldEditorParent.setLayout(new GridLayout(3, false));
		rubyDirFieldEditorParent.setFont(defaultFont);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.horizontalIndent = 30;
		rubyDirFieldEditorParent.setLayoutData(gridData);
		rubyDirFieldEditor = new DirectoryFieldEditor(RUBY_DIR.getKey(), RUBY_DIR.getDesc(), rubyDirFieldEditorParent);
		rubyDirFieldEditor.getLabelControl(rubyDirFieldEditorParent).setFont(defaultFont);
		rubyDirFieldEditor.setEnabled(!SYSTEM_RUBY.getValue(), rubyDirFieldEditorParent);
		addField(rubyDirFieldEditor);

		addPadding(parent, 1);

		Group optionsGroup = new Group(parent, SWT.NONE);
		optionsGroup.setText("Other options");
		optionsGroup.setLayout(new GridLayout(1, false));
		optionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		if (isWindows) {
			addPadding(optionsGroup, 1);
		}
		BooleanFieldEditor solargraphDiagnosticsFieldEditor = new BooleanFieldEditor(SOLARGRAPH_DIAGNOSTICS.getKey(),
				SOLARGRAPH_DIAGNOSTICS.getDesc(), optionsGroup);
		solargraphDiagnosticsFieldEditor.getDescriptionControl(optionsGroup).setFont(defaultFont);
		addField(solargraphDiagnosticsFieldEditor);
		BooleanFieldEditor debugReadaptFieldEditor = new BooleanFieldEditor(DEBUG_READAPT.getKey(),
				DEBUG_READAPT.getDesc(), optionsGroup);
		debugReadaptFieldEditor.getDescriptionControl(optionsGroup).setFont(defaultFont);
		addField(debugReadaptFieldEditor);

		addPadding(parent, 1);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 5));
		URL url = FileLocator.find(FrameworkUtil.getBundle(getClass()), new Path("/icon/github.png"));
		gitHubImage = ImageDescriptor.createFromURL(url).createImage();
		new Label(composite, SWT.NONE).setImage(gitHubImage);
		Link supportLink = new Link(composite, SWT.NONE);
		supportLink.setText("Need support? Head over to <a href=\"https://github.com/PyvesB/eclipse-solargraph\">GitHub</a>, "
				+ "and if you find the plugin helpful, consider starring the repo!");
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

	private void addPadding(Composite composite, int columns) {
		Label spacer = new Label(composite, SWT.NONE);
		GridData spacerData = new GridData();
		spacerData.horizontalSpan = columns;
		spacerData.heightHint = 2;
		spacer.setLayoutData(spacerData);
	}
}