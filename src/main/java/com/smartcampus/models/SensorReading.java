package com.smartcampus.models;

public class SensorReading {

    private int id;
    private int sensorId;     // which sensor generated this reading
    private double value;     // e.g., temperature value
    private String timestamp; // simple timestamp (string for now)

    // Default constructor (needed for JSON)
    public SensorReading() {
    }

    // Parameterized constructor
    public SensorReading(int id, int sensorId, double value, String timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for sensorId
    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    // Getter and Setter for value
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // Getter and Setter for timestamp
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}