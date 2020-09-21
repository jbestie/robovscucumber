*** Settings ***
Resource  custom.robot

*** Test Case ***
Scenario: Say Hello
  Say Hello
  Print some cool stuff

*** Keywords ***
Say Hello
  Log  Hello

