package config;

import static helpers.ConfigurationHelper.environmentConfigValue;

public class KeyCloakConfig {
    public static final String KEYCLOAK_URL = environmentConfigValue("keyCloakUrl", "http://localhost:8080");
    public static final String KEYCLOAK_REALM = environmentConfigValue("keyCloakRealm", "testRealm");
    public static final String KEYCLOAK_CLIENT_ID = environmentConfigValue("keyCloakClientId", "testClientId");
    public static final String KEYCLOAK_CLIENT_SECRET = environmentConfigValue("keyCloakClientSecret", "testSecret");
    public static final String KEYCLOAK_USERNAME = environmentConfigValue("keyCloakUserName", "testUser");
    public static final String KEYCLOAK_PASSWORD = environmentConfigValue("keyCloakPassword", "testPassword");
}
