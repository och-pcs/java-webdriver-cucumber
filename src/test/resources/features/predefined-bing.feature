@predefined
Feature: Smoke steps

  @predefined_bing
  Scenario: Predefined steps for Bing
    Given I open url "https://www.bing.com/"
    Then I should see page title as "Bing"
    Then element with xpath "//input[@name='q']" should be present
    When I type "Behavior Driven Development" into element with xpath "//input[@name='q']"
    Then I click on element using JavaScript with xpath "//form[//input/@name='q']//input[@type='submit'][@name='search']"
    Then I wait for element with xpath "//*[@id='b_results']" to be present
    Then element with xpath "//*[@id='b_results']" should contain text "Cucumber"
