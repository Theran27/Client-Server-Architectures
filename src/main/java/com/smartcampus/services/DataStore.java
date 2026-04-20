package com.smartcampus.services;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;

import java.util.ArrayList;
import java.util.List;

public class DataStore {

    // Existing
    private static List<Room> rooms = new ArrayList<>();

    // NEW
    private static List<Sensor> sensors = new ArrayList<>();
    private static List<SensorReading> readings = new ArrayList<>();

    // Sample data
    static {
        rooms.add(new Room(1, "Lab 1", "Engineering"));
        rooms.add(new Room(2, "Lecture Hall A", "Main Building"));

        sensors.add(new Sensor(1, "Temperature", 1));
        sensors.add(new Sensor(2, "Humidity", 2));

        readings.add(new SensorReading(1, 1, 28.5, "2026-04-20T10:30:00"));
        readings.add(new SensorReading(2, 2, 65.0, "2026-04-20T10:31:00"));
    }

    // ROOM METHODS
    public static List<Room> getAllRooms() {
        return rooms;
    }

    public static Room addRoom(Room room) {
        rooms.add(room);
        return room;
    }

    // SENSOR METHODS
    public static List<Sensor> getAllSensors() {
        return sensors;
    }

    public static Sensor addSensor(Sensor sensor) {
        sensors.add(sensor);
        return sensor;
    }

    // READING METHODS
    public static List<SensorReading> getAllReadings() {
        return readings;
    }

    public static SensorReading addReading(SensorReading reading) {
        readings.add(reading);
        return reading;
    }
}