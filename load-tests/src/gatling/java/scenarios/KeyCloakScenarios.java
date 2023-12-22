package scenarios;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import static config.KeyCloakConfig.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class KeyCloakScenarios {

    public static ScenarioBuilder accessTokenRequestScenario() {
        HttpRequestActionBuilder httpRequestToGetInitialAccessToken = http("POST for access token   ")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "password")
                .formParam("client_id", KEYCLOAK_CLIENT_ID)
                .formParam("client_secret", KEYCLOAK_CLIENT_SECRET)
                .formParam("scope", "openid")
                .formParam("username", KEYCLOAK_USERNAME)
                .formParam("password", KEYCLOAK_PASSWORD)
                .check(status().is(200))
                .check(jsonPath("$..refresh_token").exists().saveAs("refreshToken"));


        var httpRequestToGetNewAccessTokenUsingRefreshToken = http("POST for access token from refresh token")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "refresh_token")
                .formParam("client_id", KEYCLOAK_CLIENT_ID)
                .formParam("client_secret", KEYCLOAK_CLIENT_SECRET)
                .formParam("refresh_token", "#{refreshToken}")
                .check(status().is(200))
                .check(jsonPath("$..access_token").saveAs("accessTokenFromRefreshToken"))
                .check(bodyString().saveAs("responseBody"));

        return scenario("Request Access Token using Client Credentials")
                .exec(httpRequestToGetInitialAccessToken
                        .toChainBuilder().exec(httpRequestToGetNewAccessTokenUsingRefreshToken));
    }
}
