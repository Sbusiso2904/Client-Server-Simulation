package com.wethinkcode.client.networking;

import com.wethinkcode.client.util.MessageParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private MessageParser messageParser;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.messageParser = new MessageParser();
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
    }

    public void disconnect() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

    // Example method to send a command and receive a response.  Refactored to use MessageSender
    private String sendCommand(String command) throws IOException {
        writer.println(command);
        String response = reader.readLine();
        if (response == null) {
            throw new IOException("Server disconnected unexpectedly.");
        }
        return response;
    }

    public int launchRobot() throws IOException {
        String response = sendCommand("LAUNCH_ROBOT"); // Example command
        //  Use the MessageParser to get the robot ID, handle errors
        return messageParser.parseRobotId(response);

    }

    public List<Integer> getRobots() throws IOException {
        String response = sendCommand("GET_ROBOTS");
        return messageParser.parseRobotList(response);
    }

    public String dumpWorld() throws IOException {
        return sendCommand("DUMP_WORLD");
    }

    public String look(int robotId) throws IOException {
        String command = "LOOK " + robotId;
        return sendCommand(command);
    }

    public String getState(int robotId) throws IOException {
        String command = "STATE " + robotId;
        return sendCommand(command);
    }

    public void quit() throws IOException {
        sendCommand("QUIT");
    }
}
