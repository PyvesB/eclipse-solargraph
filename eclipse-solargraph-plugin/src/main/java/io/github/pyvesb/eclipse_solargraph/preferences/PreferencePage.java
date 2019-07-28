package io.github.pyvesb.eclipse_solargraph.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String PAGE_ID = "io.github.pyvesb.eclipse_solargraph";

	private BooleanFieldEditor systemRubyFieldEditor;
	private DirectoryFieldEditor rubyDirFieldEditor;
	private Composite rubyDirFieldEditorParent;

	@Override
	public void createFieldEditors() {
		addField(new FileFieldEditor(Preferences.GEM_PATH, Preferences.GEM_PATH_DESC, true, getFieldEditorParent()));
		addField(new BooleanFieldEditor(Preferences.UPDATE_GEM, Preferences.UPDATE_GEM_DESC, getFieldEditorParent()));
		systemRubyFieldEditor = new BooleanFieldEditor(Preferences.SYSTEM_RUBY, Preferences.SYSTEM_RUBY_DESC,
				getFieldEditorParent());
		addField(systemRubyFieldEditor);
		rubyDirFieldEditorParent = getFieldEditorParent();
		GridDataFactory.fillDefaults().indent(30, 0).applyTo(rubyDirFieldEditorParent);
		rubyDirFieldEditor = new DirectoryFieldEditor(Preferences.RUBY_DIR, Preferences.RUBY_DIR_DESC,
				rubyDirFieldEditorParent);
		boolean systemRuby = SolargraphPlugin.getPreferences().getBoolean(Preferences.SYSTEM_RUBY,
				Preferences.SYSTEM_RUBY_DEFAULT);
		rubyDirFieldEditor.setEnabled(!systemRuby, rubyDirFieldEditorParent);
		addField(rubyDirFieldEditor);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(SolargraphPlugin.getDefault().getPreferenceStore());
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

	@Override
	protected void performDefaults() {
		super.performDefaults();
		// PropertyChangeEvents aren't sent when applying defaults.
		rubyDirFieldEditor.setEnabled(!Preferences.SYSTEM_RUBY_DEFAULT, rubyDirFieldEditorParent);
	}

}
