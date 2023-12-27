package keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static config.KeyCloakConfig.*;

public class KeyCloakAdminClient {
    private static Logger log = Logger.getLogger(KeyCloakAdminClient.class.getName());
    private static KeyCloakAdminClient instance;

    private final Keycloak keyCloak;
    private final RealmResource realmResource;
    private final UsersResource usersResource;

    private final List<Map<String, Object>> testUsers = new ArrayList<>();

    private KeyCloakAdminClient() {
        this.keyCloak = KeycloakBuilder.builder()
                .serverUrl(KEYCLOAK_URL)
                .realm(KEYCLOAK_REALM)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(KEYCLOAK_CLIENT_ID)
                .clientSecret(KEYCLOAK_CLIENT_SECRET)
                .username(KEYCLOAK_ADMIN_USER)
                .password(KEYCLOAK_ADMIN_PASSWORD)
                .build();

        this.realmResource = keyCloak.realm(KEYCLOAK_REALM);
        this.usersResource = realmResource.users();
    }

    public static KeyCloakAdminClient get() {
        if (instance == null) {
            instance = new KeyCloakAdminClient();
        }

        return instance;
    }

    public void createTestUsers() {
        for (int i = 0; i < KEYCLOAK_USERS_COUNT; i++) {
            String username = "testUser" + i;
            String password = "testUserPassword" + i;

            String userId = createKeyCloakUser(username, password);

            testUsers.add(new HashMap<>() {{
                put("userId", userId);
                put("username", username);
                put("password", password);
            }});
        }
    }

    public void deleteTestUsers() {
        testUsers.forEach(userMap -> {
            usersResource.delete((String) userMap.get("userId"));
        });
    }

    private String createKeyCloakUser(String username, String password) {
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);

        // Create user (requires manage-users role)
        Response response = usersResource.create(user);
        String userId = CreatedResponseUtil.getCreatedId(response);
        log.info(String.format("User created with userId: %s%n", userId));

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);

        return userId;
    }

    public List<Map<String, Object>> getTestUsers() {
        return testUsers;
    }
}
