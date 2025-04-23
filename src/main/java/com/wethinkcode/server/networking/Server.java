package com.wethinkcode.server.networking;

import com.wethinkcode.server.world.World;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private World world;
    private List<ClientHandler> clients; // Keep track of connected clients

    public Server(int port, World world) {
        this.port = port;
        this.world = world;
        this.executorService = Executors.newCachedThreadPool(); // Use a thread pool
        this.clients = new ArrayList<>();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        //  moved the accept loop to its own method.
        new Thread(this::acceptConnections).start(); // Start accepting connections in a separate thread

    }

    private void acceptConnections() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, world, this); // Pass the Server instance
                clients.add(clientHandler); // Add to the list of clients
                executorService.submit(clientHandler); // Handle client in a thread
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                System.err.println("Error accepting connections: " + e.getMessage());
            } // else: Server socket was closed.
        }
    }

    // Method to remove a client.  This is important for cleanup.
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown(); // Stop the thread pool
            // Close all client connections.
            for (ClientHandler client : clients) {
                client.disconnect();
            }
            clients.clear();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
}
