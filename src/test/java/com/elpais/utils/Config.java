package com.elpais.utils;

public final class Config {
	private Config() {}

	public static String get(String key, String defaultValue) {
		String sys = System.getProperty(key);
		if (sys != null && !sys.isBlank()) return sys;
		String env = System.getenv(key);
		if (env != null && !env.isBlank()) return env;
		return defaultValue;
	}
}


