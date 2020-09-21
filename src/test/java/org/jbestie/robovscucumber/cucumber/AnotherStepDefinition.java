package org.jbestie.robovscucumber.cucumber;

import cucumber.api.java.en.Then;

public class AnotherStepDefinition {

    @Then("print some stuff")
    public void printSomeStuff() {
        System.out.println("Printed some stuff");
    }
}
