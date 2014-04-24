# Task assignment

This repository contains a small example process with 2 user tasks (A and B).

Both tasks should be executed by the same user group but the user which completed
task A should not execute task B. For example task B is a review task of task A.

Therefore a task listener is added to the complete event of task A and calculates
all users which are allowed to execute task B and saves them in a process variable.

This process variable is used to determine the candidate users of task B.

## Running the test with maven

In order to run the testsuite with maven you can say:

```
mvn clean test
```

## Importing the project into eclipse.

If you use eclipse you can simply import the project by selecting `File / Import |-> Existing Maven Projects.

