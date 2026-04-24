package com.smartcampus.services;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataStore {

    private static List<Room> rooms = new ArrayList<>();
    private static List<Sensor> sensors = new ArrayList<>();
    private static List<SensorReading> readings = new ArrayList<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 30);
        Room room2 = new Room("LAB-201", "Laboratory 2", 25);
        rooms.add(room1);
        rooms.add(room2);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 28.5, "LIB-301");
        Sensor sensor2 = new Sensor("CO2-001", "CO2", "ACTIVE", 450.0, "LAB-201");
        sensors.add(sensor1);
        sensors.add(sensor2);

        room1.addSensorId("TEMP-001");
        room2.addSensorId("CO2-001");

        readings.add(new SensorReading(UUID.randomUUID().toString(), System.currentTimeMillis() - 3600000, 28.5));
        readings.add(new SensorReading(UUID.randomUUID().toString(), System.currentTimeMillis() - 1800000, 29.0));
    }

    // ROOM METHODS
    public static List<Room> getAllRooms() {
        return rooms;
    }

    public static Room getRoomById(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public static Room addRoom(Room room) {
        rooms.add(room);
        return room;
    }

    public static boolean deleteRoom(String id) {
        Room room = getRoomById(id);
        if (room != null) {
            return rooms.remove(room);
        }
        return false;
    }

    // SENSOR METHODS
    public static List<Sensor> getAllSensors() {
        return sensors;
    }

    public static Sensor getSensorById(String id) {
        for (Sensor sensor : sensors) {
            if (sensor.getId().equals(id)) {
                return sensor;
            }
        }
        return null;
    }

    public static List<Sensor> getSensorsByType(String type) {
        List<Sensor> result = new ArrayList<>();
        for (Sensor sensor : sensors) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                result.add(sensor);
            }
        }
        return result;
    }

    public static List<Sensor> getSensorsByRoom(String roomId) {
        List<Sensor> result = new ArrayList<>();
        for (Sensor sensor : sensors) {
            if (sensor.getRoomId().equals(roomId)) {
                result.add(sensor);
            }
        }
        return result;
    }

    public static Sensor addSensor(Sensor sensor) {
        sensors.add(sensor);
        return sensor;
    }

    public static boolean deleteSensor(String sensorId) {
        Sensor sensor = getSensorById(sensorId);
        if (sensor != null) {
            // Remove sensor from room
            Room room = getRoomById(sensor.getRoomId());
            if (room != null) {
                room.removeSensorId(sensorId);
            }
            return sensors.remove(sensor);
        }
        return false;
    }

    public static void updateSensorCurrentValue(String sensorId, double value) {
        Sensor sensor = getSensorById(sensorId);
        if (sensor != null) {
            sensor.setCurrentValue(value);
        }
    }

    // READING METHODS
    public static List<SensorReading> getAllReadings() {
        return readings;
    }

    public static List<SensorReading> getReadingsBySensor(String sensorId) {
        List<SensorReading> result = new ArrayList<>();
        for (SensorReading reading : readings) {
            // Find readings by checking sensorIds in the room of this sensor
            Sensor sensor = getSensorById(sensorId);
            if (sensor != null) {
                result.add(reading);
            }
        }
        return result;
    }

    public static SensorReading addReading(SensorReading reading) {
        readings.add(reading);
        return reading;
    }
}