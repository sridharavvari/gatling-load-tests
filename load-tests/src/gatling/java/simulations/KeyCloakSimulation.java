package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.logging.Logger;

import static config.KeyCloakConfig.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class KeyCloakSimulation extends Simulation {
    private static Logger log = Logger.getLogger(KeyCloakSimulation.class.getName());

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(KEYCLOAK_URL)
            .disableFollowRedirect();


    // Scenarios
    ScenarioBuilder accessTokenRequest = scenario("Request Access Token using Client Credentials")
            .exec(http("POST for access token   ")
                    .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                    .formParam("grant_type", "password")
                    .formParam("client_id", KEYCLOAK_CLIENT_ID)
                    .formParam("client_secret", KEYCLOAK_CLIENT_SECRET)
                    .formParam("scope", "openid")
                    .formParam("username", KEYCLOAK_USERNAME)
                    .formParam("password", KEYCLOAK_PASSWORD)
                    .check(status().is(200))
                    .check(jsonPath("$..access_token").exists()));


    //Setup
    {
        log.info("KEYCLOAK_URL: " + KEYCLOAK_URL);

        setUp(
                accessTokenRequest.injectOpen(
                        constantUsersPerSec(10).during(Duration.ofSeconds(30)),
                        rampUsersPerSec(10).to(100).during(Duration.ofSeconds(30)),
                        constantUsersPerSec(100).during(Duration.ofSeconds(30))
                )
        ).protocols(httpProtocol);
    }
}
