package config;

import static helpers.ConfigurationHelper.environmentConfigValue;

public class KeyCloakConfig {
    public static final String KEYCLOAK_URL = environmentConfigValue("keyCloakUrl", "http://localhost:8080");
    public static final String KEYCLOAK_REALM = environmentConfigValue("keyCloakRealm", "testRealm");
    public static final String KEYCLOAK_CLIENTS_CSV = environmentConfigValue("keyCloakClientsCSV", "dev/keycloak-clients.csv");
    public static final Boolean KEYCLOAK_CREATE_NEW_USERS = Boolean.valueOf(environmentConfigValue("keyCloakCreateUsers", "false"));
    public static final Integer KEYCLOAK_CREATE_USERS_COUNT = Integer.valueOf(environmentConfigValue("keyCloakCreateUsersCount", "10"));
}
