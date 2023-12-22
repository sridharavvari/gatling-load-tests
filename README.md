# Gatling Tests

## Running load-tests

#### Running simulations locally

1. Run a local instance of KeyCloak
```
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:23.0.3 start-dev
```

2. Configure realms, users, client, etc.,

3. Update the configuration in <i>src/gatling/resources/application.conf</i> for local environment

3. Run simulations locally

We can run all simulations in load-tests module from a terminal using the following commands:
```
cd load-tests
../gradlew gatlingRun -Denv=local
```

For running a specific simulation use the following command
```
../gradlew gatlingRun-simulations.KeyCloakSimulation -Denv=local
```

### Running simulations targeting a specific environment

1. Update the configuration in <i>src/gatling/resources/application.conf</i> for the target environment.
2. We can pass "env" parameter for running simulations targeting a specific ennvironment. 
For  example to run the tests against <b>dev</b> environment, you can use this command
```
../gradlew gatlingRun -Denv=dev
```

