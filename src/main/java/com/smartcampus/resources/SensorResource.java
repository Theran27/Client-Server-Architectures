package com.smartcampus.resources;

import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Sensor;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type != null && !type.isEmpty()) {
            return DataStore.getSensorsByType(type);
        }
        return DataStore.getAllSensors();
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Sensor not found"))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {
        if (sensor.getRoomId() == null || sensor.getRoomId().isEmpty()) {
            return Response.status(400)
                    .entity(createErrorResponse("Room ID is required"))
                    .build();
        }

        if (DataStore.getRoomById(sensor.getRoomId()) == null) {
            throw new LinkedResourceNotFoundException(
                    "The specified room with ID '" + sensor.getRoomId() + "' does not exist"
            );
        }

        if (sensor.getType() == null || sensor.getType().isEmpty()) {
            return Response.status(400)
                    .entity(createErrorResponse("Sensor type is required"))
                    .build();
        }

        DataStore.addSensor(sensor);
        
        // Add sensor to room
        if (sensor.getId() != null) {
            DataStore.getRoomById(sensor.getRoomId()).addSensorId(sensor.getId());
        }

        return Response.status(201)
                .entity(createSuccessResponse("Sensor created successfully", sensor))
                .build();
    }

    @DELETE
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Sensor not found"))
                    .build();
        }

        DataStore.deleteSensor(sensorId);
        return Response.ok(createSuccessResponse("Sensor deleted successfully", null)).build();
    }

    // Sub-Resource Locator: Delegates to SensorReadingResource for nested /readings paths
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadings(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensorById(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor with ID '" + sensorId + "' not found");
        }
        return new SensorReadingResource(sensorId);
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