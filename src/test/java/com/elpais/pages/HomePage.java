package com.elpais.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class HomePage extends BasePage {

	private static final String URL = "https://elpais.com/";

	public HomePage(WebDriver driver) {
		super(driver, "HomePage");
	}

	public void open() {
		getDriver().get(URL);
		clickIfPresent("acceptCookiesButton", Duration.ofSeconds(8));
	}

	public void goToOpinion() {
		clickIfPresent("acceptCookiesButton", Duration.ofSeconds(2));
		try {
			click("opinionLink");
			return;
		} catch (Exception ignored) {
		}
		// Fallback for mobile header changes: scroll a bit and retry
		try {
			((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(0, 300);");
			try {
				click("opinionLink");
				return;
			} catch (Exception ignored) {
			}
		} catch (Exception ignored) {
		}
		// Final fallback: direct navigation
		getDriver().navigate().to("https://elpais.com/opinion/");
	}
}

