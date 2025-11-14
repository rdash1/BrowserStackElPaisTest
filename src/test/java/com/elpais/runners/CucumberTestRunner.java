package com.elpais.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.elpais.steps",
        plugin = {
                "pretty",
                "summary",
                "html:output/cucumber-report.html",
                "json:output/cucumber-report.json"
        },
        tags = "@smoke or @regression"
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    @Parameters("browser")
    public void setBrowser(@Optional("chrome") String browser) {
        // Prefer an explicitly provided system property; otherwise use the TestNG parameter (or default)
        String current = System.getProperty("BROWSER");
        if ((current == null || current.isEmpty()) && browser != null && !browser.isEmpty()) {
            System.setProperty("BROWSER", browser);
            // Only set BROWSERSTACK if it isn't already set by the user/environment
            if (System.getProperty("BROWSERSTACK") == null && System.getenv("BROWSERSTACK") == null) {
                System.setProperty("BROWSERSTACK", "true");
            }
            System.out.println(">>> Setting browser to: " + browser);
        }
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}