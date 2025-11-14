package com.elpais.steps;

import com.elpais.services.ArticleScrapingService;
import com.elpais.utils.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class HomeSteps {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeSteps.class);
	private ArticleScrapingService scrapingService;
	private WebDriver driver;  


	@Given("I open the {string} website in {string} language")
	public void openWebsiteInLanguage(String siteName, String languageCode) {
		scrapingService = new ArticleScrapingService();
		scrapingService.openHomePage(languageCode);
		driver = DriverFactory.getDriver();
		Assert.assertTrue(driver.getCurrentUrl().contains("elpais.com"),
				"Not on expected homepage for " + siteName);
		LOGGER.info("Opened {} homepage. Current URL: {}", siteName, driver.getCurrentUrl());
	}

	@Given("^\\[Step (\\d+)] I open the \"([^\"]+)\" website in \"([^\"]+)\" language$")
	public void openWebsiteInLanguageNumbered(int step, String siteName, String languageCode) {
		openWebsiteInLanguage(siteName, languageCode);
	}

	@When("I navigate to the {string} section")
	public void navigateToSection(String section) {
		ensureService();
		scrapingService.navigateToSection(section);
	}

	@When("^\\[Step (\\d+)] I navigate to the \"([^\"]+)\" section$")
	public void navigateToSectionNumbered(int step, String section) {
		navigateToSection(section);
	}
	@Then("[Step {int}] I close the {string} browser")
	public void closeBrowser(Integer stepNumber, String browser) {
		if (driver != null) {
			driver.quit();          // terminate the browser session
			driver = null;          // reset the reference so it can’t be reused
			LOGGER.info("Step {}: {} browser closed successfully.", stepNumber, browser);
		} else {
			LOGGER.warn("Step {}: No browser instance found to close for {}.", stepNumber, browser);
		}
	}
	private void ensureService() {
		if (scrapingService == null) {
			scrapingService = new ArticleScrapingService();
		}
	}
}


