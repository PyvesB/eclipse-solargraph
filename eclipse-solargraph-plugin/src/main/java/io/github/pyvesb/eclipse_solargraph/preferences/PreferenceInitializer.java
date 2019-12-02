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
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.READAPT_PATH;
import static io.github.pyvesb.eclipse_solargraph.preferences.StringPreferences.RUBY_DIR;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		IEclipsePreferences defaultPreferences = DefaultScope.INSTANCE.getNode(bundle.getSymbolicName());
		defaultPreferences.put(GEM_PATH.getKey(), GEM_PATH.getDef());
		defaultPreferences.put(READAPT_PATH.getKey(), READAPT_PATH.getDef());
		defaultPreferences.putBoolean(UPDATE_GEM.getKey(), UPDATE_GEM.getDef());
		defaultPreferences.putBoolean(SYSTEM_RUBY.getKey(), SYSTEM_RUBY.getDef());
		defaultPreferences.put(RUBY_DIR.getKey(), RUBY_DIR.getDef());
	}

}
