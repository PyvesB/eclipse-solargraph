package io.github.pyvesb.eclipse_solargraph;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SolargraphPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "io.github.pyvesb.eclipse_solargraph";

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

}
