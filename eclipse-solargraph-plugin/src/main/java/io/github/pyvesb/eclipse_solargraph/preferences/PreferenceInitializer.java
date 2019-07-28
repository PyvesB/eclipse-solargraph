package io.github.pyvesb.eclipse_solargraph.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import io.github.pyvesb.eclipse_solargraph.SolargraphPlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = SolargraphPlugin.getDefault().getPreferenceStore();
		store.setDefault(Preferences.GEM_PATH, Preferences.GEM_PATH_DEFAULT);
		store.setDefault(Preferences.UPDATE_GEM, Preferences.UPDATE_GEM_DEFAULT);
		store.setDefault(Preferences.SYSTEM_RUBY, Preferences.SYSTEM_RUBY_DEFAULT);
		store.setDefault(Preferences.RUBY_DIR, Preferences.RUBY_DIR_DEFAULT);
	}

}
