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

### Automatic test users creation as part of KeyCloakSimulation
KeyCloakSimulation can create and delete test users as part of the run. 

Follow these steps for it to work:
1. Add "keyCloakAdminUser" & "keyCloakAdminPassword" config to application.conf(admin user should have atleast <i>manage-users,
   view-realm, view-users, view-clients</i> roles)
2. While running simulation pass the parameter <i>keyCloakCreateUsers=true</i>
```
../gradlew gatlingRun-simulations.KeyCloakSimulation -Denv=local -DkeyCloakCreateUsers=true
```

