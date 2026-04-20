package com.smartcampus.resources;

import com.smartcampus.models.Sensor;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return DataStore.getAllSensors();
    }

    // VALIDATION (room must exist)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addSensor(Sensor sensor) {

        boolean roomExists = false;

        for (var room : DataStore.getAllRooms()) {
            if (room.getId() == sensor.getRoomId()) {
                roomExists = true;
                break;
            }
        }

        if (!roomExists) {
            return Response.status(400).entity("Invalid roomId").build();
        }

        DataStore.addSensor(sensor);

        return Response.status(201).entity("Sensor added successfully").build();
    }

    // FILTER BY ROOM
    @GET
    @Path("/room/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensorsByRoom(@PathParam("roomId") int roomId) {

        List<Sensor> result = new ArrayList<>();

        for (Sensor sensor : DataStore.getAllSensors()) {
            if (sensor.getRoomId() == roomId) {
                result.add(sensor);
            }
        }

        return result;
    }
}