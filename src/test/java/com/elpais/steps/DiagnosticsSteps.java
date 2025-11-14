package com.elpais.steps;

import com.elpais.context.World;
import com.elpais.model.ArticleData;
import com.elpais.services.ArticleScrapingService;
import com.elpais.utils.DriverFactory;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiagnosticsSteps {
	private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticsSteps.class);
	private final ArticleScrapingService service = new ArticleScrapingService();

	@When("I log the article scraping summary with first {int} characters")
	public void logArticleSummary(int snippetLength) {
		@SuppressWarnings("unchecked")
		List<ArticleData> articles = World.get().get("articles", java.util.List.class);
		if (articles == null || articles.isEmpty()) {
			LOGGER.warn("No articles available to log. Ensure scraping step executed successfully.");
			return;
		}
		service.logSummary(articles, snippetLength);
	}

	@When("^\\[Step (\\d+)] I log the article scraping summary with first (\\d+) characters$")
	public void logArticleSummaryNumbered(int step, int snippetLength) {
		logArticleSummary(snippetLength);
	}

	@When("I log the article scraping summary")
	public void logArticleSummaryNoArg() {
		logArticleSummary(300);
	}

	@When("^\\[Step (\\d+)] I log the article scraping summary$")
	public void logArticleSummaryNoArgNumbered(int step) {
		logArticleSummary(300);
	}

	@When("I capture a diagnostic snapshot")
	public void captureDiagnosticSnapshot() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			String url = driver.getCurrentUrl();
			String title = driver.getTitle();
			byte[] shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			Path diagDir = Path.of("output", "diagnostics");
			Files.createDirectories(diagDir);
			String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
			Path out = diagDir.resolve("diag_" + ts + ".png");
			Files.write(out, shot);
			LOGGER.info("Diagnostic snapshot captured. URL='{}', title='{}', screenshot='{}'", url, title, out);
		} catch (Exception e) {
			LOGGER.warn("Failed to capture diagnostic snapshot: {}", e.getMessage());
		}
	}

	@When("^\\[Step (\\d+)] I capture a diagnostic snapshot$")
	public void captureDiagnosticSnapshotNumbered(int step) {
		captureDiagnosticSnapshot();
	}
}


