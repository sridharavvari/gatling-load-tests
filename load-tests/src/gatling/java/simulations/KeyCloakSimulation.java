package simulations;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import keycloak.KeyCloakAdminClient;

import java.time.Duration;
import java.util.logging.Logger;

import static config.KeyCloakConfig.KEYCLOAK_CREATE_NEW_USERS;
import static config.KeyCloakConfig.KEYCLOAK_URL;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static scenarios.KeyCloakScenarios.accessTokenRequestScenario;

public class KeyCloakSimulation extends Simulation {
    private static Logger log = Logger.getLogger(KeyCloakSimulation.class.getName());

    private static HttpProtocolBuilder httpProtocol = http
            .baseUrl(KEYCLOAK_URL)
            .disableFollowRedirect();

    @Override
    public void after() {
        if (KEYCLOAK_CREATE_NEW_USERS) {
            log.info("Deleting users created part of this simulation");
            KeyCloakAdminClient.get().deleteTestUsers();
        }
    }

    {
        log.info("KEYCLOAK_URL: " + KEYCLOAK_URL);
        setUp(
                accessTokenRequestScenario().injectOpen(
                        constantUsersPerSec(5).during(Duration.ofMinutes(5)),
                        rampUsersPerSec(5).to(10).during(Duration.ofMinutes(1)),
                        constantUsersPerSec(10).during(Duration.ofMinutes(5))
                )
        ).protocols(httpProtocol);
    }
}
