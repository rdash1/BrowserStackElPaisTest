package com.elpais.utils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TextAnalyzer {
	private static final Pattern NON_WORD = Pattern.compile("[^a-zA-Z0-9]+");
	private static final Set<String> STOPWORDS = Set.of(
			"the","a","an","and","or","but","to","of","in","on","for","with",
			"at","by","from","as","is","are","was","were","be","been","being",
			"that","this","these","those","it","its","into","over","under","after","before"
	);

	private TextAnalyzer() {}

	public static Map<String, Long> findRepeatedWords(List<String> texts, int minOccurrences) {
		Map<String, Long> counts =
				texts.stream()
						.filter(Objects::nonNull)
						.map(String::toLowerCase)
						.map(t -> NON_WORD.matcher(t).replaceAll(" ").trim())
						.flatMap(t -> Arrays.stream(t.split("\\s+")))
						.filter(w -> w.length() > 2 && !STOPWORDS.contains(w))
						.collect(Collectors.groupingBy(w -> w, Collectors.counting()));
		return counts.entrySet().stream()
				.filter(e -> e.getValue() >= minOccurrences)
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(a,b) -> a, LinkedHashMap::new));
	}
}


