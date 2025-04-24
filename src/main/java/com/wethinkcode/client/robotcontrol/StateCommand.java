package com.wethinkcode.client.robotcontrol;

import java.util.Arrays;
import java.util.List;

public class StateCommand {
    private int x;
    private int y;
    private int sheilds;
    private int shots;
    private String direction;
    private static final List<String> V_DIRECTIONS = Arrays.asList(
            "north", "south", "east", "west"
    );

    public StateCommand() {
        this(0, 0, "north");
    }

    public StateCommand(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        setDirection(direction);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getSheilds(){
        return sheilds;
    }
    public int getShots(){
        return shots;
    }

    public String getDirection() {
        return direction;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(String direction) {
        String lowerDirection = direction.toLowerCase();
        if (V_DIRECTIONS.contains(lowerDirection)) {
            this.direction = lowerDirection;
        } else {
            throw new IllegalArgumentException(
                    "Invalid direction. Must be one of: " + V_DIRECTIONS
            );
        }
    }

    public String getState() {
        return String.format(
                "Position: (%d, %d), Direction: %s",
                x, y, direction
        );
    }

    @Override
    public String toString() {
        return getState();
    }

    public static void main(String[] args) {
        StateCommand state = new StateCommand(5, 3, "east");
        System.out.println("Initial state: " + state);


        state.setPosition(7, 2);
        System.out.println("Updated position: " + state);

        state.setDirection("south");
        System.out.println("Updated direction: " + state);
    }
}

