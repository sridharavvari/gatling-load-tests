# Gatling Tests

##Running all simulations in load-tests module
We can run all simulations in load-tests module using the following commands:
```
cd load-tests
../gradlew gatlingRun -Denv=local
```

To run the simulations targeting a specific environment change the value of parameter "env". For  example
to run the tests against <b>dev</b> environment you can use this command
```
../gradlew gatlingRun -Denv=dev
```

For running a specific simulation use the following command
```
../gradlew gatlingRun-simulations.KeyCloakSimulation -Denv=local
```