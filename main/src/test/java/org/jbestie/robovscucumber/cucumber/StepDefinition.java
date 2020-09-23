package org.jbestie.robovscucumber.cucumber;

import cucumber.api.java.en.Then;

public class StepDefinition {
    @Then("^Say Hello$")
    public void sayHello() {
        System.out.println("Hello");
    }
}
