package com.wethinkcode.client.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Configuration {

    private String serverAddress;
    private int serverPort;
    private static final String CONFIG_FILE = "config.properties"; //added constant

    public Configuration() {
        // Default constructor does nothing, the loadConfiguration() does the work
    }

    public void loadConfiguration() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            serverAddress = properties.getProperty("server.address", "localhost"); // Default
            serverPort = Integer.parseInt(properties.getProperty("server.port", "8080")); // Default
        } catch (IOException e) {
            // Handle file not found, or error reading.
            System.err.println("Error loading configuration from " + CONFIG_FILE + ": " + e.getMessage());
            //  Consider throwing the exception, or using default values.  For this example, defaults are used.
            throw e; // rethrow the exception
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }
}
