package com.elpais.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.List;
import java.util.stream.Collectors;

public class ArticlePage extends BasePage {

	public ArticlePage(WebDriver driver) {
		super(driver, "ArticlePage");
	}

	public String getTitle() {
		return waitForVisible("title").getText();
	}

	public String getContent() {
		List<WebElement> body = findElements("bodyParagraphs");
		if (body.isEmpty()) {
			body = findElements("fallbackParagraphs");
		}
		return body.stream()
				.map(el -> {
					try {
						return el.getText();
					} catch (StaleElementReferenceException e) {
						return "";
					}
				})
				.filter(text -> text != null && !text.isBlank())
				.collect(Collectors.joining("\n\n"));
	}

	public String getCoverImageUrl() {
		return findElements("metaOgImage").stream()
				.findFirst()
				.map(el -> el.getAttribute("content"))
				.orElse(null);
	}
}


