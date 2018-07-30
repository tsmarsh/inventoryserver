Feature: Inventories
  Scenario: Empty database
    When I get all inventories
    Then the list should have size 0

  Scenario: One inventory
    Given I have persisted an inventory
      """
      {"category" : "test.category"}
      """
    When I get all inventories
    Then the list should have size 1
     And the list should validate against inventoryList.schema.json
     And the value "test.category" is found at "inventories.[0] | {:category}"

  Scenario: No users
    When I get all users
    Then the list should have size 0

  Scenario: One user
    Given I have persisted a user
      """
      {"name" : "Tilda"}
      """
    When I get all users
    Then the list should have size 1
     And the list should validate against userList.schema.json
     And the value "Tilda" is found at "users.[0] | {:name}"

  Scenario: Give a user an inventory
    Given I have persisted an inventory
      """
        {
          "category" : "test.flarp",
          "metrics" : [
            {"Arrows" : "34"}
          ]
        }
      """
      And I have persisted a user "Archer"
      """
      {"name" : "Archer"}
      """
    When I update the the user with the inventory
     And I get latest user "Archer"
    Then the value "test.flarp" is found at "inventories.[0] | {:category}"

  Scenario: Return multiple inventories for a user
    Given I have persisted an inventory "arrows"
      """
        {
          "category" : "test.flarp",
          "metrics" : [
            {"Arrows" : "34"}
          ]
        }
      """
    And I have persisted an inventory "bows"
  """
        {
          "category" : "test.floop",
          "metrics" : [
            {"Bows" : "q"}
          ]
        }
      """
    And I have persisted a user "Archer"
      """
      {"name" : "Archer"}
      """
    When I update the user with the inventory "arrows"
     And I update the user with the inventory "bows"
    And I get latest user "Archer"
    Then "inventories.[] | select(.category=="test.flarp")" exists
     And "inventories.[] | select(.category=="test.floop")" exists
