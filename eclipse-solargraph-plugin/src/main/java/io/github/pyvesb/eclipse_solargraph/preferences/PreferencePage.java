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
package io.github.pyvesb.eclipse_solargraph.preferences;

import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.SYSTEM_RUBY;
import static io.github.pyvesb.eclipse_solargraph.preferences.BooleanPreferences.UPDATE_GEM;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.GEM_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "io.github.pyvesb.eclipse_solargraph";

	private BooleanFieldEditor systemRubyFieldEditor;
	private DirectoryFieldEditor rubyDirFieldEditor;
	private Composite rubyDirFieldEditorParent;

	@Override
	public void createFieldEditors() {
		addField(new FileFieldEditor(GEM_PATH.getKey(), GEM_PATH.getDesc(), true, getFieldEditorParent()));
		addField(new BooleanFieldEditor(UPDATE_GEM.getKey(), UPDATE_GEM.getDesc(), getFieldEditorParent()));
		systemRubyFieldEditor = new BooleanFieldEditor(SYSTEM_RUBY.getKey(), SYSTEM_RUBY.getDesc(), getFieldEditorParent());
		addField(systemRubyFieldEditor);
		rubyDirFieldEditorParent = getFieldEditorParent();
		GridDataFactory.fillDefaults().indent(30, 0).applyTo(rubyDirFieldEditorParent);
		rubyDirFieldEditor = new DirectoryFieldEditor(RUBY_DIR.getKey(), RUBY_DIR.getDesc(), rubyDirFieldEditorParent);
		rubyDirFieldEditor.setEnabled(!SYSTEM_RUBY.getValue(), rubyDirFieldEditorParent);
		addField(rubyDirFieldEditor);
	}

	@Override
	public void init(IWorkbench workbench) {
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, bundle.getSymbolicName()));
		setDescription("Modify settings of the Ruby Solargraph plugin.");
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() == systemRubyFieldEditor) {
			boolean systemRuby = Boolean.TRUE.equals(event.getNewValue());
			rubyDirFieldEditor.setEnabled(!systemRuby, rubyDirFieldEditorParent);
		}
		super.propertyChange(event);
	}

}
