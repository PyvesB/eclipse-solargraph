/*******************************************************************************
 * Copyright (c) 2019-2021 Pierre-Yves B. and others.
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

public enum BooleanPreferences implements Preference<Boolean> {

	UPDATE_GEM("AutoUpdate", "Automatically update the Solargraph and Readapt gems", true),
	SYSTEM_RUBY("SystemRuby", "Launch Ruby, Gem and Bundler commands using system path", true),
	DEBUG_READAPT("DebugReadapt", "Troubleshoot Readapt debugger by enabling adapter logs", false);

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
	}

}
