package com.smartcampus.models;

public class Sensor {

    private int id;
    private String type;      // e.g., Temperature, Humidity
    private int roomId;       // ID of the room this sensor belongs to

    // Default constructor (required for JSON)
    public Sensor() {
    }

    // Parameterized constructor
    public Sensor(int id, String type, int roomId) {
        this.id = id;
        this.type = type;
        this.roomId = roomId;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for roomId
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}