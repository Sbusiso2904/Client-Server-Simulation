package com.wethinkcode.server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private int serverPort;
    private int worldSize;
    private static final String CONFIG_FILE = "server.properties";

    public Configuration() {
        // Default constructor does nothing, the loadConfiguration() does the work
    }

    public void loadConfiguration() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            serverPort = Integer.parseInt(properties.getProperty("server.port", "8080")); // Default port
            worldSize = Integer.parseInt(properties.getProperty("world.size", "100"));   // Default world size
        } catch (IOException e) {
            System.err.println("Error loading configuration from " + CONFIG_FILE + ": " + e.getMessage());
            throw e;
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getWorldSize() {
        return worldSize;
    }
}
