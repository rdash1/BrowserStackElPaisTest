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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DriverFactory {

private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

private DriverFactory() {}

public static WebDriver getDriver() {
WebDriver existing = DRIVER.get();
if (existing != null) return existing;

boolean useRemote = Boolean.parseBoolean(Config.get("REMOTE", "false")) ||
Boolean.parseBoolean(Config.get("BROWSERSTACK", "false"));
WebDriver driver = useRemote ? createRemoteDriver() : createLocalDriver();
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

try {
boolean isMobile = false;
if (driver instanceof RemoteWebDriver) {
Object bs = ((RemoteWebDriver) driver).getCapabilities().getCapability("bstack:options");
if (bs instanceof Map) {
Object real = ((Map<?, ?>) bs).get("realMobile");
isMobile = "true".equalsIgnoreCase(String.valueOf(real));
}
}
if (!isMobile) {
driver.manage().window().maximize();
}
} catch (Exception ignored) {
}

DRIVER.set(driver);
LOGGER.info("Started {} WebDriver session", useRemote ? "remote" : "local");
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
LOGGER.warn("BrowserStack credentials not found. Falling back to local browser.");
return createLocalDriver();
}

try {
List<Map<String, Object>> capsList = BrowserStackPresets.defaultCapabilities();
Map<String, Object> selectedCaps = capsList.get(0);

Map<String, Object> selected = new HashMap<>();
selectedCaps.forEach((k, v) -> {
if (v instanceof Map) {
selected.put(k, new HashMap<>((Map<?, ?>) v));
} else {
selected.put(k, v);
}
});

MutableCapabilities caps = new MutableCapabilities();
selected.forEach(caps::setCapability);

URL remoteUrl = URI.create("https://" + user + ":" + key + "@" + hub.replace("https://", "")).toURL();
LOGGER.info("Connecting to BrowserStack hub");

try {
RemoteWebDriver remoteDriver = new RemoteWebDriver(remoteUrl, caps);
String sessionId = remoteDriver.getSessionId().toString();
LOGGER.info("BrowserStack session created. Session ID: {}", sessionId);
return remoteDriver;
} catch (Exception e) {
LOGGER.warn("Failed to create BrowserStack session: {}. Using local browser.", e.getMessage());
return createLocalDriver();
}
} catch (MalformedURLException e) {
LOGGER.error("Failed to construct BrowserStack URL. Using local browser.", e);
return createLocalDriver();
}
}

try {
String gridUrl = Config.get("REMOTE_URL", "http://localhost:4444/wd/hub");
String browser = Config.get("BROWSER", "chrome");
MutableCapabilities caps = new MutableCapabilities();
caps.setCapability("browserName", browser);
URL remoteUrl = URI.create(gridUrl).toURL();
LOGGER.info("Connecting to remote grid");
return new RemoteWebDriver(remoteUrl, caps);
} catch (MalformedURLException e) {
throw new RuntimeException(e);
}
}

public static void quitDriver() {
WebDriver driver = DRIVER.get();
if (driver != null) {
try {
LOGGER.info("Quitting WebDriver");
driver.quit();
} finally {
DRIVER.remove();
}
}
}
}
