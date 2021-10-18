@predefined
Feature: Smoke steps

  @predefined_yahoo
  Scenario: Predefined steps for Yahoo
    Given I open url "https://www.yahoo.com/"
    Then I should see page title as "Yahoo | Mail, Weather, Search, Politics, News, Finance, Sports & Videos"
    Then element with xpath "//input[@name='p']" should be present
    When I type "Behavior Driven Development" into element with xpath "//input[@name='p']"
    Then I click on element using JavaScript with xpath "//form[//input/@name='p']//input[@type='submit']"
    Then I wait for element with xpath "//*[@id='results']" to be present
    Then element with xpath "//*[@id='results']" should contain text "Cucumber"
