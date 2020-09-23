*** Settings ***
Library    oxygen.OxygenLibrary

*** Test Case ***
JUnit unit tests should pass
    Run JUnit    output/resuls.xml    mvn -f ../../../../pom.xml clean test
