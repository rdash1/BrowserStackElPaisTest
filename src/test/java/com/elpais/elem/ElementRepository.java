package com.elpais.elem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ElementRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElementRepository.class);
	private static final Map<String, Map<String, ElementLocator>> CACHE = new ConcurrentHashMap<>();

	private ElementRepository() {}

	public static ElementLocator getLocator(String page, String element) {
		Map<String, ElementLocator> pageElements = CACHE.computeIfAbsent(page, ElementRepository::loadPage);
		ElementLocator locator = pageElements.get(element);
		if (locator == null) {
			throw new IllegalArgumentException("Element '" + element + "' not found for page '" + page + "'");
		}
		return locator;
	}

	private static Map<String, ElementLocator> loadPage(String page) {
		String path = "elements/" + page + ".csv";
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new IllegalArgumentException("Elements CSV not found for page: " + page + " (expected at " + path + ")");
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				Map<String, ElementLocator> elements = new HashMap<>();
				String line;
				boolean headerSkipped = false;
				while ((line = reader.readLine()) != null) {
					if (line.isBlank()) continue;
					if (!headerSkipped) {
						headerSkipped = true;
						continue;
					}
					String[] parts = line.split(",", 3);
					if (parts.length < 3) {
						LOGGER.warn("Skipping invalid element definition '{}' in {}", line, path);
						continue;
					}
					String name = parts[0].trim();
					String type = parts[1].trim();
					String value = parts[2].trim();
					elements.put(name, new ElementLocator(name, type, value));
				}
				LOGGER.info("Loaded {} elements for page '{}'", elements.size(), page);
				return Collections.unmodifiableMap(elements);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load elements for page: " + page, e);
		}
	}
}


