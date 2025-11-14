package com.elpais.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BrowserStackPresets {
	private BrowserStackPresets() {}

	public static List<Map<String, Object>> defaultCapabilities() {
		List<Map<String, Object>> list = new ArrayList<>();

		// Chrome - Latest
		Map<String, Object> chrome = new HashMap<>();
		chrome.put("browserName", "chrome");
		chrome.put("browserVersion", "latest");
		Map<String, Object> chromeOpts = new HashMap<>();
		chromeOpts.put("os", "Windows");
		chromeOpts.put("osVersion", "11");
		chrome.put("bstack:options", chromeOpts);
		list.add(chrome);

		// Firefox - Latest
		Map<String, Object> firefox = new HashMap<>();
		firefox.put("browserName", "firefox");
		firefox.put("browserVersion", "latest");
		Map<String, Object> firefoxOpts = new HashMap<>();
		firefoxOpts.put("os", "Windows");
		firefoxOpts.put("osVersion", "11");
		firefox.put("bstack:options", firefoxOpts);
		list.add(firefox);

		// Safari - Latest
		Map<String, Object> safari = new HashMap<>();
		safari.put("browserName", "safari");
		safari.put("browserVersion", "latest");
		Map<String, Object> safariOpts = new HashMap<>();
		safariOpts.put("os", "OS X");
		safariOpts.put("osVersion", "Sonoma");
		safari.put("bstack:options", safariOpts);
		list.add(safari);

		// iPhone
		Map<String, Object> iphone = new HashMap<>();
		iphone.put("deviceName", "iPhone 15");
		Map<String, Object> iphoneOpts = new HashMap<>();
		iphoneOpts.put("realMobile", "true");
		iphoneOpts.put("osVersion", "17");
		iphone.put("bstack:options", iphoneOpts);
		list.add(iphone);

		// Android
		Map<String, Object> android = new HashMap<>();
		android.put("deviceName", "Samsung Galaxy S23");
		Map<String, Object> androidOpts = new HashMap<>();
		androidOpts.put("realMobile", "true");
		androidOpts.put("osVersion", "13.0");
		android.put("bstack:options", androidOpts);
		list.add(android);

		return list;
	}
}


