package io.github.pyvesb.eclipse_solargraph.preferences;

public class Preferences {

	public static final String GEM_PATH = "GemPath";
	public static final String GEM_PATH_DEFAULT = "";
	public static final String GEM_PATH_DESC = "Solargraph executable:";

	public static final String UPDATE_GEM = "AutoUpdate";
	public static final boolean UPDATE_GEM_DEFAULT = true;
	public static final String UPDATE_GEM_DESC = "Automatically update Solargraph when starting the plugin";

	public static final String SYSTEM_RUBY = "SystemRuby";
	public static final boolean SYSTEM_RUBY_DEFAULT = true;
	public static final String SYSTEM_RUBY_DESC = "Launch Ruby, Gem and Bundler commands using system path";

	public static final String RUBY_DIR = "RubyDir";
	public static final String RUBY_DIR_DEFAULT = "";
	public static final String RUBY_DIR_DESC = "Ruby bin directory:";

	private Preferences() {
		// Not called.
	}

}
