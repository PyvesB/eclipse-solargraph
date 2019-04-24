package io.github.pyvesb.eclipse_solargraph.preferences;

public class Preferences {

	public static final String GEM_PATH = "GemPath";
	public static final String GEM_PATH_DEFAULT = "";
	public static final String GEM_PATH_DESC = "Path to Solargraph's executable:";

	public static final String UPDATE_GEM = "AutoUpdate";
	public static final boolean UPDATE_GEM_DEFAULT = true;
	public static final String UPDATE_GEM_DESC = "Automatically update Solargraph when starting the plugin";

	private Preferences() {
		// Not called.
	}

}
