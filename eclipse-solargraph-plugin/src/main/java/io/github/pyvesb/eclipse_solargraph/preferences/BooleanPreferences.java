package io.github.pyvesb.eclipse_solargraph.preferences;

public enum BooleanPreferences implements Preference<Boolean> {

	UPDATE_GEM("AutoUpdate", "Automatically update Solargraph after starting the plugin", true),
	SYSTEM_RUBY("SystemRuby", "Launch Ruby, Gem and Bundler commands using system path", true);

	private final String key;
	private final String desc;
	private final Boolean def;

	private BooleanPreferences(String key, String desc, Boolean def) {
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
