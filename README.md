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
- Java 11+
- Maven 3.8+

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

Artifacts:
- Reports: `output/cucumber-report.html`, `output/cucumber-report.json`
- Screenshots: `output/screenshots/`
- Images: `output/images/`

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

### What gets printed
- Spanish titles and content snippet
- Translated titles in English
- Words repeated ≥ 3 times across translated titles

### GitHub + Build Screenshot
1) Initialize git, create repo, push:
```bash
git init
git add .
git commit -m "Initial commit: El País BDD scraping and analysis"
git branch -M master
git remote add origin https://github.com/<your-user>/<your-repo>.git
git push -u origin master
```
2) Run locally and capture a screenshot of the console and `output/` folder
3) Upload screenshot to Google Drive, set link to public, and add the link to this README

### Google Drive Screenshot Link
- Add your build screenshot link here: <ADD_PUBLIC_DRIVE_LINK>

### BrowserStack Logs/Screenshots
- BrowserStack automatically captures session logs and screenshots.
- See the build “ElPais BDD” in the BrowserStack Automate dashboard.


"# BrowserStackElPaisTest" 
"# BrowserStackElPaisTest" 
