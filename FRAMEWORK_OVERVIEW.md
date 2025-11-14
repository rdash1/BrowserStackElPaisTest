# ElPais BDD Test Framework Overview

## What It Does
Automated testing framework for El País Opinion section that:
- Scrapes 5 articles (titles, content, images)
- Translates Spanish → English
- Analyzes repeated words (frequency ≥ 2)
- Captures screenshots and diagnostics
- Reports results in multiple formats

## Architecture

### Technology Stack
- **Language:** Java 21
- **Test Framework:** TestNG + Cucumber (BDD)
- **Browser Automation:** Selenium 4.25.0
- **Cloud Testing:** BrowserStack (optional)
- **Build Tool:** Maven 3.5.0

### Key Components

**1. Page Objects** (`src/test/java/com/elpais/pages/`)
- `HomePage.java` - Homepage navigation
- `OpinionPage.java` - Opinion section scraping
- `ArticlePage.java` - Individual article details
- `BasePage.java` - Common web interactions

**2. Step Definitions** (`src/test/java/com/elpais/steps/`)
- `HomeSteps.java` - Website navigation
- `OpinionSteps.java` - Article scraping
- `TranslationAnalysisSteps.java` - Translation & word analysis
- `DiagnosticsSteps.java` - Logging & snapshots

**3. Services** (`src/test/java/com/elpais/services/`)
- `ArticleScrapingService.java` - Article data extraction
- `TranslationService.java` - Spanish→English translation (Google Translate API)
- `AnalysisService.java` - Word frequency analysis
- `ImageDownloader.java` - Image download & storage

**4. Utils** (`src/test/java/com/elpais/utils/`)
- `DriverFactory.java` - WebDriver creation (local/remote)
- `BrowserStackPresets.java` - Cloud browser capabilities
- `BrowserStackListener.java` - Session status tracking
- `Config.java` - Configuration management

**5. Element Locators** (`src/test/resources/elements/*.csv`)
- `HomePage.csv` - Homepage element selectors
- `OpinionPage.csv` - Opinion page selectors
- `ArticlePage.csv` - Article page selectors
- `Header.csv` - Common header elements

**6. Feature Files** (`src/test/resources/features/`)
- `el_pais_scrape.feature` - Cucumber test scenarios

## Execution Flow

```
1. Start Test (CucumberTestRunner)
   ↓
2. Create WebDriver (Local Chrome or BrowserStack)
   ↓
3. Navigate to El País website
   ↓
4. Open Opinion section
   ↓
5. Scrape 5 articles (title, content, images)
   ↓
6. Download images → output/images/
   ↓
7. Capture screenshots → output/screenshots/
   ↓
8. Translate titles (Spanish → English)
   ↓
9. Analyze word frequency
   ↓
10. Generate reports:
    - Cucumber JSON: output/cucumber-report.json
    - Cucumber HTML: output/cucumber-report.html
    - TestNG HTML: target/surefire-reports/
   ↓
11. Close browser
   ↓
12. Update BrowserStack session status (if used)
```

## Artifacts & Storage

**Screenshots:** `output/screenshots/`
- Captured before closing browser
- Stored with timestamp

**Images:** `output/images/`
- Article cover images downloaded
- Stored with article title as filename

**Reports:** `output/` & `target/surefire-reports/`
- Cucumber JSON for CI/CD integration
- Cucumber HTML for visual inspection
- TestNG XML for detailed test metrics

**Logs:** Console output + SLF4J logging
- INFO: Major operations
- WARN: Fallback scenarios
- ERROR: Failures with details

## BrowserStack Integration

### Capabilities Configured
```
Chrome latest - Windows 11
Firefox latest - Windows 11
Safari latest - macOS Sonoma
iPhone 15 - iOS 17
Samsung Galaxy S23 - Android 13
```

### How It Works
1. **Check Flag:** If `-DBROWSERSTACK=true`
2. **Load Credentials:** Read `BROWSERSTACK_USERNAME`, `BROWSERSTACK_ACCESS_KEY`, `BROWSERSTACK_HUB` from environment
3. **Get Capability:** First browser from `BrowserStackPresets.defaultCapabilities()` (Chrome on Win11)
4. **Create Session:** Connect to BrowserStack hub with capability
5. **Log Session:** Print session ID for dashboard tracking
6. **Execute Test:** Run on cloud browser
7. **Update Status:** Send pass/fail to BrowserStack via `browserstack_executor` script
8. **Fallback:** If session creation fails → use local Chrome

### Listener Integration
- `BrowserStackListener` implements `ITestListener`
- On test success → executes `browserstack_executor: {"action": "setSessionStatus", "arguments": {"status": "passed"}}`
- On test failure → sets status to "failed" with error reason
- Handles null sessions gracefully (no NPE)

## What We Tried

✅ **Working:**
- Local Chrome execution (100% success)
- Article scraping & translation
- Screenshot/image storage
- Report generation
- Null session guard in listener

❌ **Issues Encountered:**
- BrowserStack "Automate testing time expired" error
- Root cause: Free plan lacks Automate access
- Credentials valid but account cannot create sessions
- Fallback to local execution works perfectly

## Command Reference

**Run locally:**
```powershell
mvn clean test
```

**Run with BrowserStack (requires paid plan):**
```powershell
$env:BROWSERSTACK_USERNAME = "rasmiranjandash_ibBsth"
$env:BROWSERSTACK_ACCESS_KEY = "QdywxBfmSRqiJvkrpyGz"
$env:BROWSERSTACK_HUB = "https://hub.browserstack.com/wd/hub"
mvn clean test -DBROWSERSTACK=true
```

**Run specific tag:**
```powershell
mvn clean test -Dcucumber.filter.tags="@smoke"
```

## Test Results Location
- **HTML Reports:** `output/cucumber-report.html`, `target/surefire-reports/index.html`
- **Screenshots:** `output/screenshots/`
- **Article Images:** `output/images/`
- **JSON Report:** `output/cucumber-report.json`

## Status
✅ **Fully Functional** - Tests run successfully with local Chrome fallback. All assertions pass, artifacts generated correctly. Ready for production use.
