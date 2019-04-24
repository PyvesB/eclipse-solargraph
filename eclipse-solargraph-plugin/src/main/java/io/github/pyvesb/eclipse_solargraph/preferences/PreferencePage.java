package io.github.pyvesb.eclipse_solargraph.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "io.github.pyvesb.eclipse_solargraph";

	@Override
	public void createFieldEditors() {
		addField(new FileFieldEditor(Preferences.GEM_PATH, Preferences.GEM_PATH_DESC, true, getFieldEditorParent()));
		addField(new BooleanFieldEditor(Preferences.UPDATE_GEM, Preferences.UPDATE_GEM_DESC, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(SolargraphPlugin.getDefault().getPreferenceStore());
		setDescription("Modify settings of the Ruby Solargraph plugin.");
	}

}
