/*******************************************************************************
 * Copyright (c) 2019-2024 Pierre-Yves B. and others.
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

import org.osgi.service.prefs.BackingStoreException;

import io.github.pyvesb.eclipse_solargraph.utils.LogHelper;

public enum BooleanPreferences implements Preference<Boolean> {

	UPDATE_GEM("AutoUpdate", "Automatically update gems", true),
	SYSTEM_RUBY("SystemRuby", "Launch files using the Ruby installation from the PATH environment variable", true),
	DEBUG_READAPT("DebugReadapt", "Troubleshoot the Readapt debugger by adding an output to the Console view", false);

	private final String key;
	private final String desc;
	private final Boolean def;

	BooleanPreferences(String key, String desc, Boolean def) {
		this.key = key;
		this.desc = desc;
		this.def = def;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public Boolean getValue() {
		return PREFERENCES.getBoolean(key, def);
	}

	@Override
	public Boolean getDef() {
		return def;
	}

	@Override
	public void setValue(Boolean value) {
		PREFERENCES.putBoolean(key, value);
		try {
			PREFERENCES.sync();
		} catch (BackingStoreException e) {
			LogHelper.error("Exception whilst persisting preference " + this.getKey(), e);
		}
	}

}
