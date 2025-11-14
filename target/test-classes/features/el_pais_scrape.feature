Feature: Scrape and analyze El País Opinión articles
  As a user
  I want to scrape El País Opinión articles
  So that I can translate titles and analyze repeated words

  @smoke @regression
  Scenario Outline: Scrape <count> articles from <section>, translate <fromLang> -> <toLang>, analyze repeated words >= <minOccurrences>
    Given [Step 1] I open the "<siteName>" website in "<langCode>" language
    When  [Step 2] I navigate to the "<section>" section
    And   [Step 3] I scrape the first <count> articles with titles, content, and cover images into "<imagesDir>" images and "<screenshotsDir>" screenshots
    And   [Step 4] I translate each article title from "<fromLang>" to "<toLang>"
    And   [Step 5] I log the article scraping summary with first <snippetLen> characters
    And   [Step 6] I capture a diagnostic snapshot
    And   [Step 7] I log the article scraping summary
    Then  [Step 8] I print translated titles and words repeated more than <minOccurrences> times
    And [Step 9] I close the "<browser>" browser
    
    Examples:
  | siteName   | langCode | section  | count | imagesDir     | screenshotsDir     | fromLang | toLang | snippetLen | minOccurrences | browser |
  | elpais.com | es       | Opinión  | 5     | output/images | output/screenshots | es       | en     | 300        | 2              | chrome  |