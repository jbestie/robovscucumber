*** Test Cases ***
Set Variable Camel Exchange Test Case
     ${myvar} =    Set Variable    ${body}
     Should Be Equal    Hello Robot    ${body}