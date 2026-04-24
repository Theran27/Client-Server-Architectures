package com.smartcampus.resources;

import com.smartcampus.models.SensorReading;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This endpoint provides read-only access to all readings across all sensors.
 * For sensor-specific readings with POST capability, use /sensors/{sensorId}/readings
 */
@Path("/readings/all")
public class ReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getAllReadings() {
        return DataStore.getAllReadings();
    }
}