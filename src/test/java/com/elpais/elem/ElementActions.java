package com.elpais.elem;

import com.elpais.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ElementActions {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private static final Logger LOGGER = LoggerFactory.getLogger(ElementActions.class);

	public ElementActions(WebDriver driver) {
		this.driver = driver;
		long timeout = Long.parseLong(Config.get("ELEMENT_TIMEOUT", "20"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
	}

	private By getBy(String page, String element) {
		ElementLocator locator = ElementRepository.getLocator(page, element);
		return locator.toBy();
	}

	public WebElement waitForVisible(String page, String element) {
		By by = getBy(page, element);
		LOGGER.debug("Waiting for visibility of {}::{}", page, element);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public List<WebElement> waitForAllVisible(String page, String element) {
		By by = getBy(page, element);
		LOGGER.debug("Waiting for all elements {}::{}", page, element);
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	public void click(String page, String element) {
		LOGGER.debug("Clicking {}::{}", page, element);
		waitForVisible(page, element).click();
	}

	public void clickIfPresent(String page, String element, Duration timeout) {
		By by = getBy(page, element);
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(by)).click();
			LOGGER.info("Clicked optional element {}::{}", page, element);
		} catch (TimeoutException e) {
			LOGGER.debug("Optional element {}::{} not present/clickable within {}s", page, element, timeout.toSeconds());
		}
	}

	public boolean isPresent(String page, String element) {
		By by = getBy(page, element);
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public List<WebElement> findElements(String page, String element) {
		By by = getBy(page, element);
		return driver.findElements(by);
	}
}


