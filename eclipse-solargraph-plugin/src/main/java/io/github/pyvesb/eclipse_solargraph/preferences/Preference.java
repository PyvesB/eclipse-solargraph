package io.github.pyvesb.eclipse_solargraph.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.FrameworkUtil;

public interface Preference<T> {

	static final IEclipsePreferences PREFERENCES = InstanceScope.INSTANCE
			.getNode(FrameworkUtil.getBundle(Preference.class).getSymbolicName());

	String getKey();

	String getDesc();

	T getValue();

	T getDef();

	void setValue(T value);

}
