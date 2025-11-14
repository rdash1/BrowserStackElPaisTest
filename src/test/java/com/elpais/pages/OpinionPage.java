package com.elpais.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class OpinionPage extends BasePage {

	public OpinionPage(WebDriver driver) {
		super(driver, "OpinionPage");
	}

	public List<String> getFirstNArticleLinks(int n) {
		// Ensure enough links are loaded (mobile may lazy-load)
		List<WebElement> links = findElements("articleLinks");
		for (int attempt = 0; attempt < 5 && (links == null || links.size() < n); attempt++) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(0, 600);");
				Thread.sleep(500);
			} catch (Exception ignored) {
			}
			links = findElements("articleLinks");
		}
		// Collect hrefs with simple re-fetching to avoid stale references
		List<String> hrefs = new ArrayList<>();
		for (int i = 0; hrefs.size() < n; i++) {
			List<WebElement> current = findElements("articleLinks");
			if (i >= current.size()) break;
			try {
				String href = current.get(i).getAttribute("href");
				if (href != null && !href.isEmpty()) hrefs.add(href);
			} catch (StaleElementReferenceException ignored) {
			}
		}
		return hrefs;
	}
}

