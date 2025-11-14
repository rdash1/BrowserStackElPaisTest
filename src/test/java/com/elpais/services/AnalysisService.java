package com.elpais.services;

import com.elpais.utils.TextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AnalysisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisService.class);

	public Map<String, Long> findRepeatedWords(List<String> texts, int minOccurrences) {
		return TextAnalyzer.findRepeatedWords(texts, minOccurrences);
	}

	public void logRepeatedWords(Map<String, Long> repeated, int minOccurrences) {
		LOGGER.info("=== Repeated words (>= {} occurrences) ===", minOccurrences);
		if (repeated.isEmpty()) {
			LOGGER.info("No repeated words found meeting the threshold.");
		} else {
			repeated.forEach((word, count) -> LOGGER.info("{} -> {}", word, count));
		}
	}
}


