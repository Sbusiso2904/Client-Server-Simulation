package com.wethinkcode.client.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {

    // Example:  Parse a robot ID from a server response.
    public int parseRobotId(String response) {
        if (response.startsWith("ROBOT_ID:")) {
            try {
                return Integer.parseInt(response.substring(9)); // Extract the ID
            } catch (NumberFormatException e) {
                System.err.println("Error parsing robot ID: " + response);
                return -1; // Return -1 to indicate an error
            }
        } else {
            System.err.println("Invalid response for robot ID: " + response);
            return -1;
        }
    }

    // Example: Parse a list of robot IDs from a server response.
    public List<Integer> parseRobotList(String response) {
        if (response.startsWith("ROBOTS:")) {
            String idList = response.substring(7); // Get the part after "ROBOTS:"
            if (idList.isEmpty()) {
                return new ArrayList<>(); // Return an empty list
            }
            String[] ids = idList.split(","); // Split the string by commas
            List<Integer> robotIds = new ArrayList<>();
            for (String idStr : ids) {
                try {
                    robotIds.add(Integer.parseInt(idStr.trim())); // Parse each ID
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing robot ID: " + idStr);
                    // Consider how to handle this error: skip, return null, etc.
                    return null; // For this example, return null on error
                }
            }
            return robotIds;
        } else {
            System.err.println("Invalid response for robot list: " + response);
            return null;
        }
    }

    //  Example of parsing the result of a 'look' command.  This will need to be adapted
    //  to the *actual* format the server uses.  This is just an example.
    public String parseLookResult(String response) {
        if (response.startsWith("LOOK_RESULT:")) {
            return response.substring(12);
        }
        else {
            System.err.println("Invalid look result: " + response);
            return null;
        }
    }

    public String parseStateResult(String response) {
        if (response.startsWith("STATE_RESULT:")) {
            return response.substring(13);
        }
        else {
            System.err.println("Invalid state result: " + response);
            return null;
        }
    }
}


