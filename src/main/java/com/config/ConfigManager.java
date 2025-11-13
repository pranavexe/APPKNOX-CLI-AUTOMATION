// package com.config;//C:\Users\ombul\appknox-cli-automation\src\main\java\com\config

// import java.io.IOException;
// import java.io.InputStream;
// import java.util.Properties;

// public class ConfigManager {
//     private static ConfigManager instance;
//     private Properties properties;

//     private ConfigManager() {
//         properties = new Properties();
//         loadProperties();
//     }

//     public static ConfigManager getInstance() {
//         if (instance == null) {
//             instance = new ConfigManager();
//         }
//         return instance;
//     }

//     private void loadProperties() {
//         try (InputStream input = getClass().getClassLoader()
//                 .getResourceAsStream("config.properties")) {
//             if (input == null) {
//                 throw new RuntimeException("Unable to find config.properties");
//             }
//             properties.load(input);
//         } catch (IOException e) {
//             throw new RuntimeException("Error loading config.properties", e);
//         }
//     }

//     public String getValidHost() {
//         return properties.getProperty("appknox.api.host");
//     }

//     public String getValidToken() {
//         return properties.getProperty("appknox.access.token");
//     }

//     public String getInvalidToken() {
//         return properties.getProperty("appknox.invalid.token");
//     }

//     public String getInvalidHost() {
//         return properties.getProperty("appknox.invalid.host");
//     }

//     public String getHostWithoutSlash() {
//         return properties.getProperty("appknox.host.without.slash");
//     }

//     public String getCliPath() {
//         return properties.getProperty("appknox.cli.path");
//     }

//     public int getCommandTimeout() {
//         return Integer.parseInt(properties.getProperty("command.timeout", "60"));
//     }

//     public String getProperty(String key, String defaultValue) 
//     {
//     return properties.getProperty(key, defaultValue);
    
//     }
// }





package com.config;

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
                System.out.println("Warning: config.properties not found, will use environment variables only");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    
     //Get valid host - Priority: Environment Variable
     
    public String getValidHost() {
        // First try environment variable (CI/CD)
        String envHost = System.getenv("APPKNOX_API_HOST");
        if (envHost != null && !envHost.isEmpty()) {
            System.out.println("Using APPKNOX_API_HOST from environment variable");
            return envHost;
        }
        
        // Fall back to config.properties (local development)
        String propHost = properties.getProperty("appknox.api.host");
        if (propHost != null && !propHost.isEmpty()) {
            //System.out.println("Using appknox.api.host from config.properties");
            return propHost;
        }
        
        throw new RuntimeException("APPKNOX_API_HOST not found in environment or config.properties");
    }

    // Get valid token - Priority: Environment Variable > config.properties
     
    public String getValidToken() {
        // First try environment variable (CI/CD)
        String envToken = System.getenv("APPKNOX_ACCESS_TOKEN");
        if (envToken != null && !envToken.isEmpty()) {
            System.out.println("Using APPKNOX_ACCESS_TOKEN from environment variable");
            return envToken;
        }
        
        // Fall back to config.properties (local development)
        String propToken = properties.getProperty("appknox.access.token");
        if (propToken != null && !propToken.isEmpty()) {
           // System.out.println("Using appknox.access.token from config.properties");
            return propToken;
        }
        
        throw new RuntimeException("APPKNOX_ACCESS_TOKEN not found in environment or config.properties");
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
        return properties.getProperty("appknox.cli.path", "appknox");
    }

    public int getCommandTimeout() {
        return Integer.parseInt(properties.getProperty("command.timeout", "60"));
    }

    public String getProperty(String key, String defaultValue) {
        // Check environment variable first (uppercase with underscores)
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        
        // Fall back to properties file
        return properties.getProperty(key, defaultValue);
    }
}