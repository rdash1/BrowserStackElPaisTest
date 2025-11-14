package com.elpais.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * BrowserStack Test Listener for reporting pass/fail status
 */
public class BrowserStackListener implements ITestListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrowserStackListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		LOGGER.info("Test started: {}", result.getTestName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		LOGGER.info("Test passed: {}", result.getTestName());
		updateBrowserStackStatus(result, "passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		LOGGER.error("Test failed: {}", result.getTestName(), result.getThrowable());
		updateBrowserStackStatus(result, "failed");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		LOGGER.warn("Test skipped: {}", result.getTestName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		LOGGER.warn("Test failed but within success percentage: {}", result.getTestName());
		updateBrowserStackStatus(result, "failed");
	}

	@Override
	public void onStart(ITestContext context) {
		LOGGER.info("Test context started: {}", context.getName());
	}

	@Override
	public void onFinish(ITestContext context) {
		LOGGER.info("Test context finished: {}. Passed: {}, Failed: {}", 
			context.getName(), 
			context.getPassedTests().size(), 
			context.getFailedTests().size());
	}

	/**
	 * Updates BrowserStack with the test result status
	 */
	private void updateBrowserStackStatus(ITestResult result, String status) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			if (driver instanceof RemoteWebDriver) {
				RemoteWebDriver remoteDriver = (RemoteWebDriver) driver;
				// Guard against null session id (remote driver may have been closed or failed to create)
				if (remoteDriver.getSessionId() == null) {
					LOGGER.warn("Remote driver sessionId is null for test: {}. Skipping BrowserStack status update.", result.getTestName());
					return;
				}
				String sessionId = remoteDriver.getSessionId().toString();
				
				// Build the proper BrowserStack executor script
				String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test completed successfully";
				String script = String.format(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"%s\", \"reason\": \"%s\"}}",
					status, reason.replace("\"", "\\\"")
				);
				
				try {
					remoteDriver.executeScript(script);
					LOGGER.info("BrowserStack status updated to: {} for session: {} test: {}", 
						status, sessionId, result.getTestName());
				} catch (org.openqa.selenium.NoSuchSessionException e) {
					LOGGER.warn("Session already closed for test: {}. Status {} recorded automatically by BrowserStack.", 
						result.getTestName(), status);
				} catch (Exception e) {
					LOGGER.warn("Could not update BrowserStack status directly. Status will be determined by session completion.", e);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error updating BrowserStack status", e);
		}
	}
}
