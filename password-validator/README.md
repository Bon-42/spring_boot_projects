# Password Validator

## Description
This is a password validation rest service built with Spring Boot. The service is meant to check a text string for compliance to any number of password validation rules. The rules are as follows: 
 - Must consist of a mixture of lowercase letters and numerical digits only, with at least one of each.
 - Must be between 5 and 12 characters in length.
 - Must not contain any sequence of characters immediately followed by the same sequence.

## Requirements
- Java Development Kit (JDK), version 17

## Installation and usage
To use this Password Validator, clone or download the repository to your local machine. Once the download is done, run the following commands from the root folder of the project:
-> ```./gradlew clean build
-> ```./gradlew bootRun

You should be able to hit the endpoint with POST request now. "Postman" tool was used to test the endpoint during the development phase.

## Test Data Examples
The following are some examples of passwords you can test with the Password Validator:

- **Repeating Sequences:** 
  - `"abab"` (Invalid - contains a repeating sequence `ab`)
  - `"123123ab"` (Invalid - contains a repeating sequence `123`)
  - `"aaa"` (Invalid - contains a repeating sequence `a`)

- **No Repeating Sequences:** 
  - `"abcdef12"` (Valid)
  - `"123456789a"` (Valid)
  - `"abac4567"` (Valid)

- **Edge Cases:** 
  - `""` (Empty string - Valid)
  - `"a"` (Single character - Valid)
  - `"aa"` (Invalid - single repeating character)
