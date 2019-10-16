package io.github.pyvesb.eclipse_solargraph.preferences;

public enum StringPreferences implements Preference<String> {

	GEM_PATH("GemPath", "Solargraph executable:", ""),
	RUBY_DIR("RubyDir", "Ruby bin directory:", "");

	private final String key;
	private final String desc;
	private final String def;

	private StringPreferences(String key, String desc, String def) {
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
