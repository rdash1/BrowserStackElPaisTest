# BrowserStack Test Runner Script
# This script sets up environment variables and runs tests on BrowserStack

# Set BrowserStack credentials
$env:BROWSERSTACK_USERNAME = "rasmiranjandash_ibBsth"
$env:BROWSERSTACK_ACCESS_KEY = "QdywxBfmSRqiJvkrpyGz"
$env:BROWSERSTACK_HUB = "https://hub.browserstack.com/wd/hub"

# Optional: Enable debugging
# $env:DEBUG = "true"

Write-Host "==================================================" -ForegroundColor Green
Write-Host "BrowserStack Test Execution" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Username: $env:BROWSERSTACK_USERNAME" -ForegroundColor Cyan
Write-Host "Hub: $env:BROWSERSTACK_HUB" -ForegroundColor Cyan
Write-Host ""

# Run Maven tests with BrowserStack configuration
Write-Host "Running tests with Cucumber @smoke and @regression tags..." -ForegroundColor Yellow
Write-Host ""

$command = 'mvn test "-Dsurefire.suiteXmlFiles=testng-browserstack.xml" -DBROWSERSTACK=true "-Dcucumber.filter.tags=@smoke or @regression"'

Write-Host "Executing: $command" -ForegroundColor Gray
Write-Host ""

# Execute the command
Invoke-Expression $command

$exitCode = $LASTEXITCODE

Write-Host ""
Write-Host "==================================================" -ForegroundColor Green
Write-Host "Test Execution Completed" -ForegroundColor Green
Write-Host "Exit Code: $exitCode" -ForegroundColor $(if ($exitCode -eq 0) { 'Green' } else { 'Red' })
Write-Host "==================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Visit https://app.browserstack.com to view detailed test reports" -ForegroundColor Cyan

exit $exitCode
