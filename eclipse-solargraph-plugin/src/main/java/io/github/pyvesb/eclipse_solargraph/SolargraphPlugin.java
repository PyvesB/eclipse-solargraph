package io.github.pyvesb.eclipse_solargraph;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SolargraphPlugin extends AbstractUIPlugin {

	private static SolargraphPlugin plugin; // Shared plugin instance.

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static SolargraphPlugin getDefault() {
		return plugin;
	}

	public static IEclipsePreferences getPreferences() {
		return InstanceScope.INSTANCE.getNode(plugin.getBundle().getSymbolicName());
	}

}
