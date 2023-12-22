package simulations;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.logging.Logger;

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

    {
        log.info("KEYCLOAK_URL: " + KEYCLOAK_URL);

        setUp(
                accessTokenRequestScenario().injectOpen(
                        constantUsersPerSec(10).during(Duration.ofSeconds(10)),
                        rampUsersPerSec(10).to(100).during(Duration.ofSeconds(10)),
                        constantUsersPerSec(100).during(Duration.ofSeconds(30))
                )
        ).protocols(httpProtocol);
    }
}
