package scenarios;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;

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

        var getInitialAccessToken = http("POST to get an access token")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "password")
                .formParam("client_id", KEYCLOAK_CLIENT_ID)
                .formParam("client_secret", KEYCLOAK_CLIENT_SECRET)
                .formParam("scope", "openid")
                .formParam("username", "#{username}")
                .formParam("password", "#{password}")
                .check(status().is(200))
                .check(jsonPath("$..refresh_token").exists().saveAs("refreshToken"));

        var getNewAccessTokenUsingRefreshToken = http("POST to get an access token from a refresh token")
                .post(String.format("/realms/%s/protocol/openid-connect/token", KEYCLOAK_REALM))
                .formParam("grant_type", "refresh_token")
                .formParam("client_id", KEYCLOAK_CLIENT_ID)
                .formParam("client_secret", KEYCLOAK_CLIENT_SECRET)
                .formParam("refresh_token", "#{refreshToken}")
                .check(status().is(200))
                .check(jsonPath("$..access_token").saveAs("accessTokenFromRefreshToken"))
                .check(bodyString().saveAs("responseBody"));

        return scenario("Request Access Token using Client Credentials")
                .feed(feeder)
                .exec(getInitialAccessToken)
                .exec(getNewAccessTokenUsingRefreshToken);
    }


    private static FeederBuilder<Object> getKeyCloakClientsFeeder() {
        if (KEYCLOAK_CREATE_NEW_USERS) {
            return listFeeder(createKeyCloakUsers()).circular();
        } else {
            return listFeeder(csv(KEYCLOAK_USERS_CSV).readRecords()).circular();
        }
    }

    private static List<Map<String, Object>> createKeyCloakUsers() {
        List<Map<String, Object>> keyCloakUsers = new ArrayList<>();

        for (int i = 0; i < KEYCLOAK_USERS_COUNT; i++) {
            String username = "username" + i;
            String password = "password" + i;

            //api to create user
            //createKeyCloakUser(username, password);
            HashMap<String, Object> keyCloakUserMap = new HashMap<>() {{
                put("username", username);
                put("password", password);
            }};

            keyCloakUsers.add(keyCloakUserMap);
        }

        return keyCloakUsers;
    }
}
