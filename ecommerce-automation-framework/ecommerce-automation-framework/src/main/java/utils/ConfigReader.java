package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads and exposes values from config.properties.
 * Centralizing config means environment/browser changes happen in
 * one file instead of scattered across test classes.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/main/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
        }
    }

    private ConfigReader() {
        // utility class — prevent instantiation
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
