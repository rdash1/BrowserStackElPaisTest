## El País BDD Selenium Framework (Java + Cucumber + TestNG)

Modular Selenium framework using Cucumber (BDD) to:
- Scrape first 5 articles from El País Opinión, ensuring Spanish content
- Download cover images, translate titles (ES ➜ EN), analyze repeated words
- Run locally and on BrowserStack with 5 parallel threads

### Project Structure
- `src/test/resources/features` — Gherkin features
- `src/test/resources/elements` — Page-specific locator CSVs
- `src/test/java/com/elpais/pages` — Page Objects
- `src/test/java/com/elpais/elem` — Shared element access helpers
- `src/test/java/com/elpais/services` — Reusable scraping, translation, analysis services
- `src/test/java/com/elpais/steps` — Step Definitions
- `src/test/java/com/elpais/utils` — Utilities (DriverFactory, Translator, etc.)
- `src/test/java/com/elpais/runners` — TestNG Cucumber runner
- `output/` — Reports, screenshots, images

### Prerequisites
- **Java 21 LTS** (Microsoft OpenJDK 21.0.7)
- Maven 3.8.9+
- Selenium 4.25.0
- Cucumber 7.18.1
- TestNG 7.10.2

### Install Dependencies
```bash
mvn -q -e -DskipTests=true clean verify
```

### Run Locally (default Chrome)
```bash
mvn test -Dthreads=5 -DBROWSER=chrome
```
Or Firefox/Edge:
```bash
mvn test -DBROWSER=firefox
mvn test -DBROWSER=edge
```

**Latest Test Results (November 14, 2025):**
```
BUILD SUCCESS
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
Total time: 2 minutes 2 seconds

✅ Chrome (Windows 11) - 1m55s
✅ Firefox (Windows 11) - 1m44s  
✅ Safari (macOS Sonoma) - 1m45s
✅ iPhone 15 - 1m50s
✅ Samsung Galaxy S23 - 1m59s
```

Artifacts:
- Reports: `output/cucumber-report.html`, `output/cucumber-report.json`
- Screenshots: `output/screenshots/`
- Images: `output/images/`
- Diagnostics: `output/diagnostics/`

### Run on BrowserStack (5 parallel threads)
Set credentials:
```bash
setx BROWSERSTACK_USERNAME your_user
setx BROWSERSTACK_ACCESS_KEY your_key
```
Run:
```bash
mvn test -Dsurefire.suiteXmlFiles=testng-browserstack.xml -DBROWSERSTACK=true
```
Notes:
- Capabilities cover a mix of desktop and mobile (see `BrowserStackPresets`).
- Parallel is handled by TestNG DataProvider and distributed to capabilities.

### Translation API
Uses MyMemory public API (`https://api.mymemory.translated.net`) for ES ➜ EN title translation.

---

## Framework Best Practices Implemented ✅

### 1. **Page Object Model (POM)**
- **Location**: `src/test/java/com/elpais/pages/`
- Each page encapsulates its elements and interactions
- `BasePage` provides common functionality
- Eliminates code duplication across tests

**Example:**
```java
// HomePage.java
public class HomePage extends BasePage {
    public void navigateToOpinionSection() {
        actions.click(OPINION_SECTION);
    }
}
```

### 2. **Service Layer Pattern**
- **Location**: `src/test/java/com/elpais/services/`
- Business logic separated from step definitions
- Reusable across multiple scenarios
- Easy to test and maintain

**Services Implemented:**
- `ArticleScrapingService` - Web scraping logic
- `TranslationService` - API integration
- `AnalysisService` - Text processing and analysis

### 3. **Element Repository (CSV-based)**
- **Location**: `src/test/resources/elements/`
- Centralized element locators
- Easy to update without code changes
- Supports multiple locator strategies (xpath, css, id)

**Format:**
```csv
ElementName,Locator,LocatorType
acceptCookiesButton,//button[@aria-label='Accept']
opinionLink,//a[@href='/opinion/']
```

### 4. **Clean Step Definitions**
- **Location**: `src/test/java/com/elpais/steps/`
- One responsibility per step
- Delegation to service layer
- Readable Gherkin → Code mapping

### 5. **Thread-Safe Driver Management**
- `ThreadLocal<WebDriver>` for parallel execution
- Safe concurrent access across 5 threads
- Proper resource cleanup

### 6. **Configuration Management**
- Environment-based configuration
- Support for local and remote execution
- BrowserStack credentials via environment variables
- Graceful fallback mechanisms

### 7. **Comprehensive Logging**
- SLF4J with detailed log levels
- Track test flow and debugging information
- Integration with test reports

### 8. **Error Handling**
- BrowserStack fallback to local browser
- Translation API error handling
- Element not found resilience
- Meaningful error messages

### 9. **Parallel Test Execution**
- 5 threads for faster feedback
- TestNG DataProvider for parameterization
- No race conditions or shared state issues

### 10. **Artifact Collection**
- Screenshots for each test
- Article images downloaded
- Diagnostic snapshots
- Test reports in multiple formats

---

## Cucumber Feature Scenarios

**File**: `src/test/resources/features/el_pais_scrape.feature`

```gherkin
@smoke @regression
Scenario: Scrape, Translate, and Analyze El País Opinion Articles
  Given I open the "elpais.com" website in "es" language
  When I navigate to the "Opinión" section
  And I scrape the first 5 articles with titles, content, and cover images
  And I translate each article title from "es" to "en"
  Then I print translated titles and words repeated more than 2 times
```

---

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 21 LTS |
| Build Tool | Maven | 3.8.9 |
| WebDriver | Selenium | 4.25.0 |
| BDD Framework | Cucumber | 7.18.1 |
| Test Runner | TestNG | 7.10.2 |
| WebDriver Manager | WebDriverManager | 5.9.2 |
| Logging | SLF4J + Logback | 2.0.13 |
| JSON Processing | java-json | 20240205 |

---

### What gets printed
- Spanish titles and content snippet
- Translated titles in English
- Words repeated ≥ 3 times across translated titles

### GitHub + Build Screenshot

#### 1. Initialize Git Repository
```bash
git init
git add .
git commit -m "Initial commit: El País BDD scraping and analysis framework"
git branch -M master
git remote add origin https://github.com/<your-user>/<your-repo>.git
git push -u origin master
```

#### 2. Verify Build Works
```bash
mvn clean test -Dcucumber.filter.tags=@smoke
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

#### 3. Capture Screenshots
- Screenshot 1: Build execution in terminal showing BUILD SUCCESS
- Screenshot 2: Output folder with generated reports and artifacts
- Screenshot 3: Cucumber HTML report in browser

#### 4. Upload to Google Drive
1. Create a folder: "El País Automation - Build Screenshots"
2. Upload all 3 screenshots
3. Set sharing: "Anyone with the link can view"
4. Copy the folder link

#### 5. Update Project Links

**Google Drive Screenshot Link:**
```
https://drive.google.com/drive/folders/YOUR_FOLDER_ID?usp=sharing
```

**BrowserStack Build Link:**
```
https://app.browserstack.com/automate/builds/YOUR_BUILD_ID
```

---

### BrowserStack Dashboard

After running tests on BrowserStack:
1. Visit: https://app.browserstack.com/automate
2. Build Name: "ElPais BDD Automation"
3. Features:
   - ✅ Video recordings of each test
   - 📸 Screenshots at each step
   - 🖥️ Console logs
   - 📊 Network logs
   - ⏱️ Performance metrics

---


