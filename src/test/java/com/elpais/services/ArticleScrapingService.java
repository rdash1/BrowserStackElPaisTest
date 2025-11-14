package com.elpais.services;

import com.elpais.model.ArticleData;
import com.elpais.pages.ArticlePage;
import com.elpais.pages.HomePage;
import com.elpais.pages.OpinionPage;
import com.elpais.utils.DriverFactory;
import com.elpais.utils.ImageDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ArticleScrapingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleScrapingService.class);

	private final WebDriver driver;
	private final HomePage homePage;
	private final OpinionPage opinionPage;
	private final ArticlePage articlePage;

	public ArticleScrapingService() {
		this.driver = DriverFactory.getDriver();
		this.homePage = new HomePage(driver);
		this.opinionPage = new OpinionPage(driver);
		this.articlePage = new ArticlePage(driver);
	}

	public void openHomePage(String expectedLanguage) {
		homePage.open();
		String langAttr = driver.findElement(By.tagName("html")).getAttribute("lang");
		if (expectedLanguage != null && !expectedLanguage.isBlank()) {
			if (langAttr == null || !langAttr.toLowerCase().startsWith(expectedLanguage.toLowerCase())) {
				LOGGER.warn("Expected language '{}', but page lang attribute is '{}'", expectedLanguage, langAttr);
			} else {
				LOGGER.info("Confirmed page language '{}'", langAttr);
			}
		} else {
			LOGGER.info("Home page lang attribute: {}", langAttr);
		}
	}

	public void navigateToSection(String sectionName) {
		if ("opinión".equalsIgnoreCase(sectionName) || "opinion".equalsIgnoreCase(sectionName)) {
			homePage.goToOpinion();
		} else {
			throw new UnsupportedOperationException("Section navigation not yet implemented for: " + sectionName);
		}
		LOGGER.info("Navigated to section '{}'. Current URL: {}", sectionName, driver.getCurrentUrl());
	}

	public List<ArticleData> scrapeArticles(int count, String imageDir, String screenshotDir) {
		List<ArticleData> scraped = new ArrayList<>();
		List<String> links = opinionPage.getFirstNArticleLinks(count);
		LOGGER.info("Found {} article links, requested {}", links.size(), count);
		Path imagePath = Path.of(imageDir);
		Path screenshotPath = Path.of(screenshotDir);
		try {
			Files.createDirectories(imagePath);
			Files.createDirectories(screenshotPath);
		} catch (IOException e) {
			throw new RuntimeException("Failed to create output directories", e);
		}

		int index = 1;
		for (String link : links) {
			driver.navigate().to(link);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
			ArticleData data = new ArticleData();
			data.url = link;
			data.titleEs = articlePage.getTitle();
			data.contentEs = articlePage.getContent();
			data.imageUrl = articlePage.getCoverImageUrl();
			if (data.imageUrl != null && !data.imageUrl.isBlank()) {
				data.imagePath = ImageDownloader.download(data.imageUrl, imageDir, "article_" + index);
			}
			try {
				byte[] shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				Path shotPath = screenshotPath.resolve("article_" + index + ".png");
				Files.write(shotPath, shot);
				data.screenshotPath = shotPath;
			} catch (Exception e) {
				LOGGER.warn("Failed to capture screenshot for {}: {}", link, e.getMessage());
			}
			LOGGER.info("Scraped article {} -> '{}'", index, data.titleEs);
			scraped.add(data);
			index++;
			if (scraped.size() >= count) {
				break;
			}
		}
		return scraped;
	}

	public void logSummary(List<ArticleData> articles, int snippetLength) {
		LOGGER.info("=== Article Scraping Summary ({} articles) ===", articles.size());
		for (ArticleData a : articles) {
			String snippet = a.contentEs == null ? "" :
					a.contentEs.substring(0, Math.min(snippetLength, a.contentEs.length()));
			LOGGER.info("Title (ES): {}", a.titleEs);
			LOGGER.info("Snippet: {}{}", snippet, a.contentEs != null && a.contentEs.length() > snippetLength ? "..." : "");
			LOGGER.info("Image path: {}", a.imagePath != null ? a.imagePath : "N/A");
			LOGGER.info("Screenshot path: {}", a.screenshotPath != null ? a.screenshotPath : "N/A");
			LOGGER.info("URL: {}", a.url);
		}
	}
}


