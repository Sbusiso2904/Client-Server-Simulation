package com.wethinkcode.client;

import com.wethinkcode.client.config.Configuration;
import com.wethinkcode.client.networking.Client;
import com.wethinkcode.client.robotcontrol.RobotClient;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ClientApp {

    private static Configuration config;
    private static Client client;
    private static RobotClient[] robotClients; // Array to hold multiple robots
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            // Load configuration
            config = new Configuration();
            config.loadConfiguration();

            // Initialize client and scanner
            client = new Client(config.getServerAddress(), config.getServerPort());
            scanner = new Scanner(System.in);

            // Connect to the server
            client.connect();

            // Get the number of robots to launch from the user
            int numRobots = getNumberOfRobotsToLaunch();
            robotClients = new RobotClient[numRobots]; // Initialize the array

            // Launch robots
            launchRobots(numRobots);

            // Main interaction loop
            mainLoop();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Clean up resources
            if (client != null) {
                client.disconnect();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    // Method to get the number of robots from user input
    private static int getNumberOfRobotsToLaunch() {
        int numRobots = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print("Enter the number of robots to launch: ");
            try {
                numRobots = scanner.nextInt();
                if (numRobots > 0) {
                    valid = true;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
            }
        }
        return numRobots;
    }

    // Method to launch multiple robots
    private static void launchRobots(int numRobots) throws IOException {
        for (int i = 0; i < numRobots; i++) {
            // Send a command to the server to launch a new robot.  The server
            // should return the new robot's ID.
            int robotId = client.launchRobot(); // Assuming Client has launchRobot()
            if (robotId != -1) { //  -1 indicates an error, for example
                System.out.println("Launched robot " + (i + 1) + " with ID: " + robotId);
                robotClients[i] = new RobotClient(client, robotId); // Store RobotClient
            } else {
                System.err.println("Failed to launch robot " + (i + 1));
                // Handle the error appropriately,  Perhaps stop the program, or try again.
            }
        }
    }

    // Main interaction loop
    private static void mainLoop() throws IOException {
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an action:");
            System.out.println("1. List Robots");
            System.out.println("2. Dump World");
            System.out.println("3. Look (for a robot)");
            System.out.println("4. Get State (of a robot)");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try{
                choice = scanner.nextInt();
            } catch(InputMismatchException e){
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.next(); // consume the non-int
                continue; // Restart the loop
            }
            scanner.nextLine(); // Consume the newline character after reading the integer.

            switch (choice) {
                case 1:
                    listRobots();
                    break;
                case 2:
                    dumpWorld();
                    break;
                case 3:
                    look();
                    break;
                case 4:
                    getState();
                    break;
                case 5:
                    running = false;
                    client.quit();
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to list robots
    private static void listRobots() throws IOException {
        List<Integer> robotIds = client.getRobots();
        if (robotIds != null && !robotIds.isEmpty()) {
            System.out.println("Robots in the world:");
            for (Integer id : robotIds) {
                System.out.println("Robot ID: " + id);
            }
        } else if (robotIds != null) {
            System.out.println("No robots in the world.");
        }
        else{
            System.out.println("Error getting robot list from server.");
        }
    }

    // Method to dump world
    private static void dumpWorld() throws IOException {
        String worldDump = client.dumpWorld();
        if (worldDump != null) {
            System.out.println("World Dump:\n" + worldDump);
        } else {
            System.out.println("Error getting world dump from server.");
        }
    }

    // Method to look
    private static void look() throws IOException {
        if (robotClients.length == 0) {
            System.out.println("No robots have been launched.  Launch robots first.");
            return;
        }
        int robotId = getRobotIdFromUser();
        if (robotId == -1){
            return; // error occurred in getRobotIdFromUser
        }

        String lookResult = client.look(robotId);
        if (lookResult != null) {
            System.out.println("Look result for robot " + robotId + ":\n" + lookResult);
        } else {
            System.out.println("Error getting look result from server.");
        }
    }

    // Method to get state of a robot
    private static void getState() throws IOException {
        if (robotClients.length == 0) {
            System.out.println("No robots have been launched. Launch a robot first.");
            return;
        }

        int robotId = getRobotIdFromUser();
        if (robotId == -1){
            return; // error occurred in getRobotIdFromUser
        }
        String robotState = client.getState(robotId);
        if (robotState != null) {
            System.out.println("State for robot " + robotId + ":\n" + robotState);
        } else {
            System.out.println("Error getting robot state from server.");
        }
    }

    // Helper method to get a valid robot ID from the user.
    private static int getRobotIdFromUser() {
        System.out.println("Available Robots:");
        for (int i = 0; i < robotClients.length; i++) {
            if (robotClients[i] != null) {
                System.out.println((i + 1) + ". Robot ID: " + robotClients[i].getRobotId());
            }
        }
        System.out.print("Enter the number of the robot: ");
        int robotNumber = 0;
        try {
            robotNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid robot number.  Please enter a number.");
            scanner.next(); // consume the non-int
            return -1;
        }
        scanner.nextLine(); // Consume the newline

        if (robotNumber > 0 && robotNumber <= robotClients.length && robotClients[robotNumber-1] != null) {
            return robotClients[robotNumber - 1].getRobotId();
        } else {
            System.out.println("Invalid robot number.");
            return -1;
        }
    }
}
