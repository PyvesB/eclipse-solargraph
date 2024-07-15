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

import static io.github.pyvesb.eclipse_solargraph.utils.CommandHelper.isWindows;

import io.github.pyvesb.eclipse_solargraph.utils.CommandHelper;

public enum StringPreferences implements Preference<String> {

	GEM_PATH("GemPath", "Solargraph executable:", CommandHelper.findPath(isWindows() ? "solargraph.bat" : "solargraph")),
	RUBY_DIR("RubyDir", "Ruby bin directory for \"Run As\" launches:", CommandHelper.findDirectory("ruby")),
	READAPT_PATH("ReadaptPath", "Readapt executable:", CommandHelper.findPath(isWindows() ? "readapt.bat" : "readapt"));

	private final String key;
	private final String desc;
	private final String def;

	StringPreferences(String key, String desc, String def) {
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
	public String getValue() {
		return PREFERENCES.get(key, def);
	}

	@Override
	public String getDef() {
		return def;
	}

	@Override
	public void setValue(String value) {
		PREFERENCES.put(key, value);
	}

}
