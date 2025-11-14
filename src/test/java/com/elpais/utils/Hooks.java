package com.elpais.utils;


import com.elpais.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @After
    public void updateBrowserStackStatus(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            return;
        }

        String status = scenario.isFailed() ? "failed" : "passed";
        String reason = scenario.isFailed() ? scenario.getName() : "Scenario passed";

        ((JavascriptExecutor) driver).executeScript(
            String.format(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"%s\",\"reason\": \"%s\"}}",
                status,
                reason.replace("\"", "'")
            )
        );

        driver.quit();
    }
}