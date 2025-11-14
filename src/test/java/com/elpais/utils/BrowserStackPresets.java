package com.elpais.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BrowserStackPresets {
	private BrowserStackPresets() {}

	public static List<Map<String, Object>> defaultCapabilities() {
		List<Map<String, Object>> list = new ArrayList<>();

		// Chrome Latest on Windows 11
		Map<String, Object> winChrome = new HashMap<>();
		winChrome.put("browserName", "Chrome");
		winChrome.put("browserVersion", "latest");
		winChrome.put("bstack:options", Map.of(
				"os", "Windows",
				"osVersion", "11",
				"buildName", "ElPais BDD",
				"sessionName", "Chrome Win11"
		));
		list.add(winChrome);

		// Firefox Latest on Windows 11
		Map<String, Object> winFirefox = new HashMap<>();
		winFirefox.put("browserName", "Firefox");
		winFirefox.put("browserVersion", "latest");
		winFirefox.put("bstack:options", Map.of(
				"os", "Windows",
				"osVersion", "11",
				"buildName", "ElPais BDD",
				"sessionName", "Firefox Win11"
		));
		list.add(winFirefox);

		// Safari on macOS
		Map<String, Object> macSafari = new HashMap<>();
		macSafari.put("browserName", "Safari");
		macSafari.put("browserVersion", "latest");
		macSafari.put("bstack:options", Map.of(
				"os", "OS X",
				"osVersion", "Sonoma",
				"buildName", "ElPais BDD",
				"sessionName", "Safari macOS"
		));
		list.add(macSafari);

		// Mobile - iPhone
		Map<String, Object> ios = new HashMap<>();
		ios.put("browserName", "iPhone");
		ios.put("bstack:options", Map.of(
				"deviceName", "iPhone 15",
				"osVersion", "17",
				"realMobile", "true",
				"buildName", "ElPais BDD",
				"sessionName", "iPhone 15 Safari"
		));
		list.add(ios);

		// Mobile - Android
		Map<String, Object> android = new HashMap<>();
		android.put("browserName", "Android");
		android.put("bstack:options", Map.of(
				"deviceName", "Samsung Galaxy S23",
				"osVersion", "13.0",
				"realMobile", "true",
				"buildName", "ElPais BDD",
				"sessionName", "Galaxy S23 Chrome"
		));
		list.add(android);

		return list;
	}
}


