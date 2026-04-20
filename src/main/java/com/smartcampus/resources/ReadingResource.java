package com.smartcampus.resources;

import com.smartcampus.models.SensorReading;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/readings")
public class ReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings() {
        return DataStore.getAllReadings();
    }

    // VALIDATION (sensor must exist)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addReading(SensorReading reading) {

        boolean sensorExists = false;

        for (var sensor : DataStore.getAllSensors()) {
            if (sensor.getId() == reading.getSensorId()) {
                sensorExists = true;
                break;
            }
        }

        if (!sensorExists) {
            return Response.status(400).entity("Invalid sensorId").build();
        }

        DataStore.addReading(reading);

        return Response.status(201).entity("Reading added successfully").build();
    }

    // FILTER BY SENSOR
    @GET
    @Path("/sensor/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadingsBySensor(@PathParam("sensorId") int sensorId) {

        List<SensorReading> result = new ArrayList<>();

        for (SensorReading reading : DataStore.getAllReadings()) {
            if (reading.getSensorId() == sensorId) {
                result.add(reading);
            }
        }

        return result;
    }
}