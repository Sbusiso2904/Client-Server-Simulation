// src/main/java/com/yourdomain/yourproject/server/ServerApp.java
package com.wethinkcode.server;

import com.wethinkcode.server.config.Configuration;
import com.wethinkcode.server.networking.Server;
import com.wethinkcode.server.world.World;
import java.io.IOException;

public class ServerApp {

    private static Configuration config;
    private static Server server;
    private static World world;

    public static void main(String[] args) {
        try {
            // Load configuration
            config = new Configuration();
            config.loadConfiguration();

            // Initialize the world
            world = new World(config.getWorldSize()); // Use world size from config
            world.initializeWorld(); // Setup initial obstacles, etc.

            // Initialize and start the server
            server = new Server(config.getServerPort(), world);
            server.start(); // Start the server in a separate thread

            System.out.println("Server started on port " + config.getServerPort() + ".  World size: " + config.getWorldSize());

            // The server runs in its own thread.  The main thread could be used
            // for server administration, or monitoring.  For this simple example,
            // we'll just keep the main thread alive.
            while (true) {
                try {
                    Thread.sleep(1000); // Sleep for a second.
                } catch (InterruptedException e) {
                    // Handle interruption, if needed.  For now, just continue.
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Clean up resources (though the server runs in its own thread)
            if (server != null) {
                server.stop(); //  Make sure the server can be stopped.
            }
        }
    }
}
