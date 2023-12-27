package config;

import static helpers.ConfigurationHelper.environmentConfigValue;

public class KeyCloakConfig {
    public static final String KEYCLOAK_URL = environmentConfigValue("keyCloakUrl", "http://localhost:8080");
    public static final String KEYCLOAK_REALM = environmentConfigValue("keyCloakRealm", "testRealm");
    public static final String KEYCLOAK_CLIENT_ID = environmentConfigValue("keyCloakClientId", "testClientId");
    public static final String KEYCLOAK_CLIENT_SECRET = environmentConfigValue("keyCloakClientSecret", "testClientSecret");

    public static final String KEYCLOAK_USERS_CSV = environmentConfigValue("keyCloakUsersCSV", "dev/keycloak-users.csv");
    public static final Boolean KEYCLOAK_CREATE_NEW_USERS = Boolean.valueOf(environmentConfigValue("keyCloakCreateUsers", "false"));
    public static final Integer KEYCLOAK_USERS_COUNT = Integer.valueOf(environmentConfigValue("keyCloakUsersCount", "10"));
}
