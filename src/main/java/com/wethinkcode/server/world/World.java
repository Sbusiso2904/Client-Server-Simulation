package com.wethinkcode.server.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private int size;
    private List<Obstacle> obstacles;
    private List<Robot> robots;
    private Random random = new Random();

    public World(int size) {
        this.size = size;
        this.obstacles = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

    public int getSize() {
        return size;
    }

    public void initializeWorld() {
        // Create a hardcoded obstacle for now.  In a real game, this would
        // likely be loaded from a file, or generated procedurally.
        obstacles.add(new Obstacle(size / 2, size / 2)); // Example obstacle at center
    }

    public int addRobot() {
        //  Find a random empty spot.
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (isOccupied(x, y)); // Keep looking until we find an empty spot.

        Robot robot = new Robot(x, y);
        robots.add(robot);
        return robot.getId(); // Return the robot's ID.
    }

    private boolean isOccupied(int x, int y) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getX() == x && obstacle.getY() == y) {
                return true;
            }
        }
        for (Robot robot : robots) {
            if (robot.getX() == x && robot.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public String getWorldDump() {
        StringBuilder sb = new StringBuilder();
        sb.append("World Size: ").append(size).append("\n");
        sb.append("Obstacles:\n");
        for (Obstacle obstacle : obstacles) {
            sb.append("  ").append(obstacle).append("\n");
        }
        sb.append("Robots:\n");
        for (Robot robot : robots) {
            sb.append("  ").append(robot).append("\n");
        }
        return sb.toString();
    }

    public String look(int robotId) {
        //  Find the robot.
        Robot sourceRobot = null;
        for (Robot robot : robots) {
            if (robot.getId() == robotId) {
                sourceRobot = robot;
                break;
            }
        }
        if (sourceRobot == null) {
            return "Robot with ID " + robotId + " not found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Visible objects for Robot ").append(robotId).append(":\n");

        //  For now, the robot sees everything.  Later, this will involve
        //  line-of-sight calculations, and be limited by obstacles.
        for (Robot otherRobot : robots) {
            if (otherRobot.getId() != robotId) { // Don't include the looking robot
                sb.append("  Robot ").append(otherRobot.getId()).append(" at (").append(otherRobot.getX()).append(", ").append(otherRobot.getY()).append(")\n");
            }
        }
        for (Obstacle obstacle : obstacles) {
            sb.append("  Obstacle at (").append(obstacle.getX()).append(", ").append(obstacle.getY()).append(")\n");
        }
        return sb.toString();
    }

    public String getRobotState(int robotId) {
        for (Robot robot : robots) {
            if (robot.getId() == robotId) {
                return robot.toString(); //  Delegate to the Robot class.
            }
        }
        return "Robot with ID " + robotId + " not found.";
    }
}
