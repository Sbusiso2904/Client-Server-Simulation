package com.wethinkcode.server.command;

import com.wethinkcode.server.world.Robot;
import com.wethinkcode.server.world.World;
import java.util.List;

public class CommandHandler {

    private World world;

    public CommandHandler(World world) {
        this.world = world;
    }

    public String handleCommand(String command) {
        if (command.equals("QUIT")) {
            return "OK"; //  Signal that the server is done.
        } else if (command.equals("GET_ROBOTS")) {
            return handleGetRobots();
        } else if (command.equals("DUMP_WORLD")) {
            return handleDumpWorld();
        } else if (command.startsWith("LOOK ")) {
            return handleLook(command);
        } else if (command.startsWith("STATE ")) {
            return handleState(command);
        } else if (command.equals("LAUNCH_ROBOT")) {
            return handleLaunchRobot();
        } else {
            return "UNKNOWN COMMAND";
        }
    }

    private String handleLaunchRobot() {
        int newRobotId = world.addRobot();
        return "ROBOT_ID:" + newRobotId;
    }

    private String handleGetRobots() {
        List<Robot> robots = world.getRobots();
        if (robots.isEmpty()) {
            return "ROBOTS:"; // Return "ROBOTS:" to indicate empty list
        }
        StringBuilder sb = new StringBuilder("ROBOTS:");
        for (int i = 0; i < robots.size(); i++) {
            sb.append(robots.get(i).getId());
            if (i < robots.size() - 1) {
                sb.append(","); // Comma-separated
            }
        }
        return sb.toString();
    }

    private String handleDumpWorld() {
        return world.getWorldDump();
    }

    private String handleLook(String command) {
        try {
            int robotId = Integer.parseInt(command.substring(5).trim()); // Extract ID
            return world.look(robotId);
        } catch (NumberFormatException e) {
            return "INVALID COMMAND: Invalid robot ID";
        }
    }

    private String handleState(String command) {
        try {
            int robotId = Integer.parseInt(command.substring(6).trim()); // Extract ID
            return world.getRobotState(robotId);
        } catch (NumberFormatException e) {
            return "INVALID COMMAND: Invalid robot ID";
        }
    }
}
