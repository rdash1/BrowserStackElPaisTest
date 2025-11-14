package com.elpais.services;

import com.elpais.model.ArticleData;
import com.elpais.utils.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TranslationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TranslationService.class);

	public void translateTitles(List<ArticleData> articles, String fromLang, String toLang) {
		for (ArticleData article : articles) {
			String translated = Translator.translate(article.titleEs, fromLang, toLang);
			article.translatedTitleEn = translated;
			LOGGER.info("Translated '{}' [{} -> {}] -> '{}'", article.titleEs, fromLang, toLang, translated);
		}
	}

	public List<String> getTranslatedTitles(List<ArticleData> articles) {
		return articles.stream()
				.map(a -> a.translatedTitleEn)
				.collect(Collectors.toList());
	}
}


