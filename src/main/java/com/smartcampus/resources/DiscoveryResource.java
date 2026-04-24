package com.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("version", "1.0");
        response.put("title", "Smart Campus API");
        response.put("description", "RESTful API for managing rooms and sensors across campus facilities");
        response.put("contact", "admin@smartcampus.local");

        // HATEOAS Links
        Map<String, Object> resources = new LinkedHashMap<>();
        
        Map<String, String> roomsLinks = new LinkedHashMap<>();
        roomsLinks.put("list", "/api/v1/rooms");
        roomsLinks.put("create", "POST /api/v1/rooms");
        roomsLinks.put("getById", "/api/v1/rooms/{roomId}");
        roomsLinks.put("delete", "DELETE /api/v1/rooms/{roomId}");
        resources.put("rooms", roomsLinks);

        Map<String, String> sensorsLinks = new LinkedHashMap<>();
        sensorsLinks.put("list", "/api/v1/sensors");
        sensorsLinks.put("listByType", "/api/v1/sensors?type=Temperature");
        sensorsLinks.put("create", "POST /api/v1/sensors");
        sensorsLinks.put("getById", "/api/v1/sensors/{sensorId}");
        sensorsLinks.put("delete", "DELETE /api/v1/sensors/{sensorId}");
        sensorsLinks.put("readings", "/api/v1/sensors/{sensorId}/readings");
        resources.put("sensors", sensorsLinks);

        Map<String, String> readingsLinks = new LinkedHashMap<>();
        readingsLinks.put("list", "/api/v1/sensors/{sensorId}/readings");
        readingsLinks.put("create", "POST /api/v1/sensors/{sensorId}/readings");
        readingsLinks.put("allReadings", "/api/v1/readings/all");
        resources.put("readings", readingsLinks);

        response.put("resources", resources);

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("apiBase", "/api/v1");
        metadata.put("currentTime", System.currentTimeMillis() + "");
        response.put("metadata", metadata);

        return response;
    }
}