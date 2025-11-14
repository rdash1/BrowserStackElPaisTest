package com.elpais.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

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

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}