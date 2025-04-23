package com.wethinkcode.server.world;

public class Robot {
    private static int nextRobotId = 1; // Static variable to assign unique IDs
    private int id;
    private int x;
    private int y;
    //  Add other robot state, as needed (e.g., energy, health, etc.)

    public Robot(int x, int y) {
        this.id = nextRobotId++; // Assign a unique ID
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //  Add methods to update robot state (e.g., move, take damage, etc.)
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Robot [id=" + id + ", x=" + x + ", y=" + y + "]";
    }
}
