Feature: Article
  This feature deals with the article availability in the application

  Scenario: Main Article
    Given I navigate to url straits times home page
    And close advertisement pop out
    And I click on login button
    And I enter ID and password
    When I click sign in button
    Then I should see the home page and my ID displayed
    And I should also see picture attached for main article
    And I click on main article
    Then I should see picture or video displayed in the article
    And I logout to prevent account lock for another login
