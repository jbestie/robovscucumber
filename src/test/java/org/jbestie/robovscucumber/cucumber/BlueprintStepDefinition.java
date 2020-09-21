package org.jbestie.robovscucumber.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jbestie.robovscucumber.blueprint.TestBlueprintRoute;

public class BlueprintStepDefinition {
    private String message;
    private final TestBlueprintRoute testBlueprintRoute = new TestBlueprintRoute();

    @Given("^message (.*) to send$")
    public void givenMessageToSend(String message) {
        this.message = message;
    }

    @When("^user sends the message to queue (.*)$")
    public void whenUserWantsToSendMessage(String queueName) throws Exception {
        testBlueprintRoute.sendActiveMqMessage(queueName, message);
    }

    @Then("^message in queue (.*) is (.*)$")
    public void thenMessageInQueueIs(String queueName, String message) throws Exception {
        testBlueprintRoute.testRouteWithValidMessage(queueName, message);
    }
}
