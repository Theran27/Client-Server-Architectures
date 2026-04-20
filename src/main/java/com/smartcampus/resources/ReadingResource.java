package com.smartcampus.resources;

import com.smartcampus.models.SensorReading;
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

@Path("/readings")
public class ReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings() {
        return DataStore.getAllReadings();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SensorReading addReading(SensorReading reading) {
        return DataStore.addReading(reading);
    }
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