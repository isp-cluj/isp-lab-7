# Lab 7 - Safe Home Access


## Exercise 1 - Java implementation
![Exercise 1 image](docs/ex1.jpg)

Implement a safe home access Java application based on the diagram above. Consider also following functional requirements in implementing your program:
1. If pin is wrong enterPin method shall throw an _InvalidPinException_ error
2. If 3 consecutive attempts are made to enter pin then door is locked forever and enterPin method shall throw TooManyAttemptsException 
3. If pin is correct door shall be opened or closed depending on current state (if open will be closed, if closed will be opened)
4. All attempts (successful or not) will be logged in access log list

**NOTE - Java project come with prefiled unit tests. You should make sure all tests pass. You are not allowed to change unit tests (except initialisation of Controller object in the _instantiateDoorController()_ method.**

## Exercise 2 - State machine diagram

For Safe Home Access application create UML state machine diagram.  

## Exercise 3 - Use case diagram

For Safe Home Access application create UML use case diagram. 

