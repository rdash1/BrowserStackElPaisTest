package com.elpais.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class DriverFactory {

	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
	private static final AtomicInteger INDEX = new AtomicInteger(0);
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private DriverFactory() {}

	public static WebDriver getDriver(String browser) {
		if (driver.get() == null) {
			try {
				DesiredCapabilities caps = new DesiredCapabilities();
				switch (browser.toLowerCase()) {
					case "chrome":
						caps.setCapability("browserName", "Chrome");
						caps.setCapability("os", "Windows");
						caps.setCapability("osVersion", "11");
						break;
					case "firefox":
						caps.setCapability("browserName", "Firefox");
						caps.setCapability("os", "OS X");
						caps.setCapability("osVersion", "Monterey");
						break;
					case "edge":
						caps.setCapability("browserName", "Edge");
						caps.setCapability("os", "Windows");
						caps.setCapability("osVersion", "10");
						break;
					case "iphone":
						caps.setCapability("deviceName", "iPhone 14");
						caps.setCapability("realMobile", "true");
						caps.setCapability("osVersion", "16");
						break;
					case "android":
						caps.setCapability("deviceName", "Samsung Galaxy S22");
						caps.setCapability("realMobile", "true");
						caps.setCapability("osVersion", "12.0");
						break;
				}
	
				String username = System.getenv("BROWSERSTACK_USERNAME");
				String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
	
				driver.set(new RemoteWebDriver(
					new URL("https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub"),
					caps
				));
			} catch (Exception e) {
				throw new RuntimeException("Failed to initialize BrowserStack driver", e);
			}
		}
		return driver.get();
	}
	

	public static WebDriver getDriver() {
		WebDriver existing = DRIVER.get();
		if (existing != null) return existing;

		boolean useRemote = Boolean.parseBoolean(Config.get("REMOTE", "false")) ||
				Boolean.parseBoolean(Config.get("BROWSERSTACK", "false"));
		WebDriver driver = useRemote ? createRemoteDriver() : createLocalDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		// Avoid maximize on real mobile devices (unsupported)
		try {
			boolean isMobile = false;
			if (driver instanceof org.openqa.selenium.remote.RemoteWebDriver) {
				Object bs = ((org.openqa.selenium.remote.RemoteWebDriver) driver).getCapabilities().getCapability("bstack:options");
				if (bs instanceof java.util.Map) {
					Object real = ((java.util.Map<?, ?>) bs).get("realMobile");
					isMobile = "true".equalsIgnoreCase(String.valueOf(real));
				}
			}
			if (!isMobile) {
				driver.manage().window().maximize();
			}
		} catch (Exception ignored) {
		}
		DRIVER.set(driver);
		LOGGER.info("Started {} WebDriver session {}", useRemote ? "remote" : "local", driver);
		return driver;
	}

	private static WebDriver createLocalDriver() {
		String browser = Config.get("BROWSER", "chrome").toLowerCase();
		switch (browser) {
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions ff = new FirefoxOptions();
				LOGGER.info("Starting local Firefox driver");
				return new FirefoxDriver(ff);
			case "edge":
				WebDriverManager.edgedriver().setup();
				EdgeOptions edge = new EdgeOptions();
				LOGGER.info("Starting local Edge driver");
				return new EdgeDriver(edge);
			default:
				WebDriverManager.chromedriver().setup();
				ChromeOptions chrome = new ChromeOptions();
				chrome.addArguments("--disable-gpu");
				chrome.addArguments("--no-sandbox");
				LOGGER.info("Starting local Chrome driver");
				return new ChromeDriver(chrome);
		}
	}

	private static WebDriver createRemoteDriver() {
		boolean isBrowserStack = Boolean.parseBoolean(Config.get("BROWSERSTACK", "false"));
		if (isBrowserStack) {
			String user = Config.get("BROWSERSTACK_USERNAME", "");
			String key = Config.get("BROWSERSTACK_ACCESS_KEY", "");
			String hub = Config.get("BROWSERSTACK_HUB", "https://hub.browserstack.com/wd/hub");
			if (user.isBlank() || key.isBlank()) {
				throw new IllegalStateException("Missing BrowserStack credentials. Set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY.");
			}
			List<Map<String, Object>> capsList = BrowserStackPresets.defaultCapabilities();
			int idx = Math.floorMod(INDEX.getAndIncrement(), capsList.size());
			Map<String, Object> selected = capsList.get(idx);
			MutableCapabilities caps = new MutableCapabilities();
			selected.forEach(caps::setCapability);
			try {
				URL remoteUrl = URI.create("https://" + user + ":" + key + "@" + hub.replace("https://", "")).toURL();
				LOGGER.info("Connecting to BrowserStack hub {} with capabilities {}", remoteUrl, selected);
				return new RemoteWebDriver(remoteUrl, caps);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid BrowserStack hub URL", e);
			}
		}
		try {
			String gridUrl = Config.get("REMOTE_URL", "http://localhost:4444/wd/hub");
			String browser = Config.get("BROWSER", "chrome");
			MutableCapabilities caps = new MutableCapabilities();
			caps.setCapability("browserName", browser);
			URL remoteUrl = URI.create(gridUrl).toURL();
			LOGGER.info("Connecting to remote grid {} with browser {}", remoteUrl, browser);
			return new RemoteWebDriver(remoteUrl, caps);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void quitDriver() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			try {
				LOGGER.info("Quitting WebDriver session {}", driver);
				driver.quit();
			} finally {
				DRIVER.remove();
			}
		}
	}
}


