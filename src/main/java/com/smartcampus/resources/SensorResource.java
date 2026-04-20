package com.smartcampus.resources;

import com.smartcampus.models.Sensor;
import com.smartcampus.services.DataStore;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return DataStore.getAllSensors();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addSensor(Sensor sensor) {

        // Check if room exists
        boolean roomExists = false;

        for (var room : DataStore.getAllRooms()) {
            if (room.getId() == sensor.getRoomId()) {
                roomExists = true;
                break;
            }
        }

        if (!roomExists) {
            return "Invalid roomId: Room does not exist";
        }

        // Add sensor
        DataStore.addSensor(sensor);

        return "Sensor added successfully";
    }
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