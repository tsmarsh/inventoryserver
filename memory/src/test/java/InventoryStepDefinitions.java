import cucumber.api.PendingException;
import cucumber.api.java8.En;

public class InventoryStepDefinitions implements En {
    public InventoryStepDefinitions() {
        Given("^I have persisted an inventory (\\w+)$", (String inventoryJson) -> {
            throw new PendingException();
        });



        When("^I get all inventories$", () -> {
            throw new PendingException();
        });


        Then("^the list should have size (\\d+)$", (Integer listSize) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });


    }
}
