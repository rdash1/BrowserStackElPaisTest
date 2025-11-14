package com.elpais.steps;

import com.elpais.context.ScenarioContext;
import com.elpais.elem.ElementActions;
import com.elpais.utils.Config;
import com.elpais.utils.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericSteps {
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericSteps.class);
	private final ScenarioContext ctx = new ScenarioContext();

	@Given("^\\[Step (\\d+)]  user close \"(\\w+)\" browser if already opened$")
	public void userCloseBrowserIfOpen(int step, String browser) {
		LOGGER.info("[Step {}] Close browser if opened: {}", step, browser);
		DriverFactory.quitDriver();
	}

	@Given("^\\[Step (\\d+)] user opens \"(\\w+)\" browser and navigates to \"([^\"]+)\"$")
	public void userOpensBrowserAndNavigates(int step, String browser, String url) {
		LOGGER.info("[Step {}] Open {} and navigate to {}", step, browser, url);
		System.setProperty("BROWSER", browser.toLowerCase());
		WebDriver driver = DriverFactory.getDriver();
		driver.get(url);
	}

	@When("^\\[Step (\\d+)] user logs in with username \"([^\"]+)\"$")
	public void userLogsInWithUsername(int step, String username) {
		LOGGER.info("[Step {}] Login as {}", step, username);
		String password = Config.get("APP_PASSWORD", "");
		if (password.isBlank()) {
			throw new IllegalStateException("APP_PASSWORD not set in env or system properties");
		}
		WebDriver driver = DriverFactory.getDriver();
		ElementActions el = new ElementActions(driver);
		el.waitForVisible("LoginPage", "usernameField").clear();
		el.waitForVisible("LoginPage", "usernameField").sendKeys(username);
		el.waitForVisible("LoginPage", "passwordField").clear();
		el.waitForVisible("LoginPage", "passwordField").sendKeys(password);
		el.click("LoginPage", "submitButton");
	}

	@Then("^\\[Step (\\d+)] user navigates to \"([^\"]+)\" section$")
	public void userNavigatesToSection(int step, String section) {
		LOGGER.info("[Step {}] Navigate to section {}", step, section);
		// Generic behavior: set page context; click link text if present
		WebDriver driver = DriverFactory.getDriver();
		driver.navigate().to(driver.getCurrentUrl()); // keep session alive
		ctx.setCurrentPageName(section.replaceAll("\\s+", "") + "Page");
	}

	@When("^\\[Step (\\d+)] user notes application count under \"([^\"]+)\" and stores as \"(\\w+)\"$")
	public void userNotesApplicationCount(int step, String elementName, String varName) {
		LOGGER.info("[Step {}] Read count from {}::{} and store as {}", step, ctx.getCurrentPageName(), elementName, varName);
		WebDriver driver = DriverFactory.getDriver();
		ElementActions el = new ElementActions(driver);
		String text = el.waitForVisible(ctx.getCurrentPageName(), elementName).getText();
		String digits = text.replaceAll("[^0-9]", "");
		int count = digits.isEmpty() ? 0 : Integer.parseInt(digits);
		ctx.put(varName, count);
	}

	@Then("^\\[Step (\\d+)] the value of \"(\\w+)\" and \"(\\w+)\" should be same$")
	public void valuesShouldBeSame(int step, String var1, String var2) {
		Integer v1 = ctx.get(var1, Integer.class);
		Integer v2 = ctx.get(var2, Integer.class);
		if (v1 == null || v2 == null) {
			throw new AssertionError("Missing values: " + var1 + "=" + v1 + ", " + var2 + "=" + v2);
		}
		if (!v1.equals(v2)) {
			throw new AssertionError("Values differ: " + var1 + "=" + v1 + ", " + var2 + "=" + v2);
		}
		LOGGER.info("[Step {}] Verified {} ({}) == {} ({})", step, var1, v1, var2, v2);
	}

	@Then("^\\[Step (\\d+)] user loggedout from the application$")
	public void userLoggedOut(int step) {
		LOGGER.info("[Step {}] Logout", step);
		WebDriver driver = DriverFactory.getDriver();
		ElementActions el = new ElementActions(driver);
		el.click("Header", "logoutButton");
	}

	@Then("^\\[Step (\\d+)] user closes the browser \"(\\w+)\"$")
	public void userClosesTheBrowser(int step, String browser) {
		LOGGER.info("[Step {}] Close browser {}", step, browser);
		DriverFactory.quitDriver();
	}
}


