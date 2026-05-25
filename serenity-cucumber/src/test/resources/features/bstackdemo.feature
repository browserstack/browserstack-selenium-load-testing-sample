Feature: BrowserStack Demo cart and checkout

  Scenario: Add a product to the cart
    Given I open the BrowserStack demo store
    When I add the product at index "3" to my cart
    Then the same product should appear in my cart

  Scenario: Complete a checkout
    Given I open the BrowserStack demo store
    And I sign in to BrowserStack demo
    When I add the product at index "1" to my cart
    And I close the cart panel
    And I add the product at index "2" to my cart
    And I proceed to checkout
    And I fill in the shipping address
    Then I should see the confirmation message "Your Order has been successfully placed."
