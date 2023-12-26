package scenarios;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.KeyCloakConfig.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class KeyCloakScenarios {

    public static ScenarioBuilder accessTokenRequestScenario() {
        FeederBuilder<Object> feeder = getKeyCloakClientsFeeder();

        HttpRequestActionBuilder httpRequestToGetInitialAccessToken = http("POST for access token   ")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "password")
                .formParam("client_id", "#{clientId}")
                .formParam("client_secret", "#{clientSecret}")
                .formParam("scope", "openid")
                .formParam("username", "#{username}")
                .formParam("password", "#{password}")
                .check(status().is(200))
                .check(jsonPath("$..refresh_token").exists().saveAs("refreshToken"));

        var httpRequestToGetNewAccessTokenUsingRefreshToken = http("POST for access token from refresh token")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "refresh_token")
                .formParam("client_id", "#{clientId}")
                .formParam("client_secret", "#{clientSecret}")
                .formParam("refresh_token", "#{refreshToken}")
                .check(status().is(200))
                .check(jsonPath("$..access_token").saveAs("accessTokenFromRefreshToken"))
                .check(bodyString().saveAs("responseBody"));

        return scenario("Request Access Token using Client Credentials")
                .feed(feeder)
                .exec(httpRequestToGetInitialAccessToken,
                        httpRequestToGetNewAccessTokenUsingRefreshToken);
    }


    private static FeederBuilder<Object> getKeyCloakClientsFeeder() {
        if (KEYCLOAK_CREATE_NEW_USERS) {
            return listFeeder(createKeyCloakClientUsers()).circular();
        } else {
            return listFeeder(csv(KEYCLOAK_CLIENTS_CSV).readRecords()).circular();
        }
    }

    private static List<Map<String, Object>> createKeyCloakClientUsers() {
        List<Map<String, Object>> keyCloakUserClients = new ArrayList<>();

        for (int i = 0; i < KEYCLOAK_CREATE_USERS_COUNT; i++) {
            String clientId = "testClientId" + i;
            String clientSecret = "testClientSecret" + i;
            String username = "username" + i;
            String password = "password" + i;

            //api to create user
            //createKeyCloakUser(clientId, clientSecret, username, password);
            HashMap<String, Object> keyCloakUserMap = new HashMap<>() {{
                put("clientId", clientId);
                put("clientSecret", clientSecret);
                put("username", username);
                put("password", password);
            }};

            keyCloakUserClients.add(keyCloakUserMap);
        }

        return keyCloakUserClients;
    }
}
