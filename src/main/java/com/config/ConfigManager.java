package com.config;//C:\Users\ombul\appknox-cli-automation\src\main\java\com\config

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties", e);
        }
    }

    public String getValidHost() {
        return properties.getProperty("appknox.api.host");
    }

    public String getValidToken() {
        return properties.getProperty("appknox.access.token");
    }

    public String getInvalidToken() {
        return properties.getProperty("appknox.invalid.token");
    }

    public String getInvalidHost() {
        return properties.getProperty("appknox.invalid.host");
    }

    public String getHostWithoutSlash() {
        return properties.getProperty("appknox.host.without.slash");
    }

    public String getCliPath() {
        return properties.getProperty("appknox.cli.path");
    }

    public int getCommandTimeout() {
        return Integer.parseInt(properties.getProperty("command.timeout", "60"));
    }

    public String getProperty(String key, String defaultValue) 
    {
    return properties.getProperty(key, defaultValue);
    
    }
}