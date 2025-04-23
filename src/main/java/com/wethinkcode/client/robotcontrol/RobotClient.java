package com.wethinkcode.client.robotcontrol;

import com.wethinkcode.client.networking.Client;
import java.io.IOException;

public class RobotClient {

    private Client client; // Reference to the Client instance to communicate with the server
    private int robotId;

    public RobotClient(Client client, int robotId) {
        this.client = client;
        this.robotId = robotId;
    }

    public int getRobotId() {
        return robotId;
    }

    public String look() throws IOException {
        return client.look(robotId);
    }

    public String getState() throws IOException {
        return client.getState(robotId);
    }
    // Add methods for other robot-specific commands as needed (e.g., move, fire, etc.)
}
