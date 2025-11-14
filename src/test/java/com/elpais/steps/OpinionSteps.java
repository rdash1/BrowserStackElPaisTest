package com.elpais.steps;

import com.elpais.context.World;
import com.elpais.model.ArticleData;
import com.elpais.services.ArticleScrapingService;
import io.cucumber.java.en.When;

import java.util.List;

public class OpinionSteps {
	private ArticleScrapingService scrapingService;

	@When("I scrape the first {int} articles with titles, content, and cover images into {string} images and {string} screenshots")
	public void scrapeArticles(int count, String imageDir, String screenshotDir) {
		ensureService();
		List<ArticleData> articles = scrapingService.scrapeArticles(count, imageDir, screenshotDir);
		World.get().put("articles", articles);
	}

	@When("^\\[Step (\\d+)] I scrape the first (\\d+) articles with titles, content, and cover images into \"([^\"]+)\" images and \"([^\"]+)\" screenshots$")
	public void scrapeArticlesNumbered(int step, int count, String imageDir, String screenshotDir) {
		scrapeArticles(count, imageDir, screenshotDir);
	}

	private void ensureService() {
		if (scrapingService == null) {
			scrapingService = new ArticleScrapingService();
		}
	}
}


