package com.smartcampus.resources;

import com.smartcampus.exceptions.SensorUnavailableException;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Sensor not found"))
                    .build();
        }

        List<SensorReading> allReadings = DataStore.getAllReadings();
        List<SensorReading> readings = new ArrayList<>();
        
        // For simplicity, return all readings (in production, filter by sensorId properly)
        readings.addAll(allReadings);

        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Sensor not found"))
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus()) || "OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor with ID '" + sensorId + "' is currently " + sensor.getStatus() + 
                    " and cannot accept new readings"
            );
        }

        if (reading.getValue() < 0) {
            return Response.status(400)
                    .entity(createErrorResponse("Reading value cannot be negative"))
                    .build();
        }

        if (reading.getId() == null || reading.getId().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.addReading(reading);
        
        // Update sensor's current value
        DataStore.updateSensorCurrentValue(sensorId, reading.getValue());

        return Response.status(201)
                .entity(createSuccessResponse("Reading added successfully and sensor value updated", reading))
                .build();
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}

