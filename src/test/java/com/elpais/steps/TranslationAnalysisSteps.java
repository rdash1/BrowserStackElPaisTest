package com.elpais.steps;

import com.elpais.context.World;
import com.elpais.model.ArticleData;
import com.elpais.services.AnalysisService;
import com.elpais.services.TranslationService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TranslationAnalysisSteps {
	private static final Logger LOGGER = LoggerFactory.getLogger(TranslationAnalysisSteps.class);
	private final TranslationService translationService = new TranslationService();
	private final AnalysisService analysisService = new AnalysisService();

	@When("I translate each article title from {string} to {string}")
	public void translateTitles(String fromLang, String toLang) {
		@SuppressWarnings("unchecked")
		List<ArticleData> articles = World.get().get("articles", java.util.List.class);
		if (articles == null || articles.isEmpty()) {
			LOGGER.warn("No articles loaded to translate.");
			return;
		}
		translationService.translateTitles(articles, fromLang, toLang);
		List<String> translated = translationService.getTranslatedTitles(articles);
		World.get().put("translatedTitles", translated);
	}

	@When("^\\[Step (\\d+)] I translate each article title from \"([^\"]+)\" to \"([^\"]+)\"$")
	public void translateTitlesNumbered(int step, String fromLang, String toLang) {
		translateTitles(fromLang, toLang);
	}

	@Then("I print translated titles and words repeated more than {int} times")
	public void printRepeatedWords(int minOccurrences) {
		@SuppressWarnings("unchecked")
		List<String> translatedTitles = World.get().get("translatedTitles", java.util.List.class);
		if (translatedTitles == null) {
			translatedTitles = new ArrayList<>();
		}
		if (translatedTitles.isEmpty()) {
			LOGGER.warn("No translated titles available to analyze.");
			return;
		}
		Map<String, Long> repeated = analysisService.findRepeatedWords(translatedTitles, minOccurrences);
		analysisService.logRepeatedWords(repeated, minOccurrences);
	}

	@Then("^\\[Step (\\d+)] I print translated titles and words repeated more than (\\d+) times$")
	public void printRepeatedWordsNumbered(int step, int minOccurrences) {
		printRepeatedWords(minOccurrences);
	}
}


