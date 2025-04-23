package com.wethinkcode.server.networking;

import com.wethinkcode.server.command.CommandHandler;
import com.wethinkcode.server.world.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private World world;
    private CommandHandler commandHandler;
    private Server server; // Reference to the Server instance

    public ClientHandler(Socket clientSocket, World world, Server server) {
        this.clientSocket = clientSocket;
        this.world = world;
        this.commandHandler = new CommandHandler(world);
        this.server = server;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println("Received command from " + clientSocket.getInetAddress() + ": " + inputLine);
                String response = commandHandler.handleCommand(inputLine);
                writer.println(response);
                if ("QUIT".equals(inputLine)) {
                    break; // Exit the loop if the client sends QUIT
                }
            }
            System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
        } catch (IOException e) {
            System.err.println("Error handling client " + clientSocket.getInetAddress() + ": " + e.getMessage());
        } finally {
            disconnect(); // Clean up resources
            server.removeClient(this); // Remove this client from the server's list
        }
    }

    public void disconnect() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error disconnecting client: " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
    }
}
