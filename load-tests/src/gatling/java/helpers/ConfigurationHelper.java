package helpers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Optional;

public class ConfigurationHelper {
    private static Config conf = ConfigFactory.load();
    private static String environment = System.getProperty("env", "local");

    public static String environmentConfigValue(String configName) {
        return systemProperty(configName)
                .orElse(appConfig("testEnv", environment, configName)
                        .orElseThrow(() -> new RuntimeException("Configuration value not found")));
    }

    public static String environmentConfigValue(String configName, String defaultValue) {
        return systemProperty(configName)
                .orElse(appConfig("testEnv", environment, configName)
                        .orElse(defaultValue));
    }

    public static Optional<String> systemProperty(String configName) {
        return Optional.ofNullable(System.getProperty(configName));
    }

    public static Optional<String> appConfig(String context, String subContext, String configName) {
        try {
            return Optional.ofNullable(conf.getString(String.format("%s.%s.%s", context, subContext, configName)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
