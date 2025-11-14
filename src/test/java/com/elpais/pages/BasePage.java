package com.elpais.pages;

import com.elpais.elem.ElementActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
	private final WebDriver driver;
	private final ElementActions elements;
	private final String pageName;

	protected BasePage(WebDriver driver, String pageName) {
		this.driver = driver;
		this.pageName = pageName;
		this.elements = new ElementActions(driver);
	}

	protected WebDriver getDriver() {
		return driver;
	}

	protected ElementActions elements() {
		return elements;
	}

	protected WebElement waitForVisible(String elementName) {
		return elements.waitForVisible(pageName, elementName);
	}

	protected List<WebElement> waitForAllVisible(String elementName) {
		return elements.waitForAllVisible(pageName, elementName);
	}

	protected void click(String elementName) {
		elements.click(pageName, elementName);
	}

	protected void clickIfPresent(String elementName, Duration timeout) {
		elements.clickIfPresent(pageName, elementName, timeout);
	}

	protected List<WebElement> findElements(String elementName) {
		return elements.findElements(pageName, elementName);
	}
}


