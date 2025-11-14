package com.elpais.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
	private final Map<String, Object> store = new HashMap<>();
	private String currentPageName;

	public void put(String key, Object value) {
		store.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) {
		Object v = store.get(key);
		if (v == null) return null;
		return (T) v;
	}

	public String getCurrentPageName() {
		return currentPageName;
	}

	public void setCurrentPageName(String currentPageName) {
		this.currentPageName = currentPageName;
	}
}


