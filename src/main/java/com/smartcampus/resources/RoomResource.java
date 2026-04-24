package com.smartcampus.resources;

import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.models.Room;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return DataStore.getAllRooms();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRoomById(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Room not found"))
                    .build();
        }
        return Response.ok(room).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        if (room.getName() == null || room.getName().isEmpty()) {
            return Response.status(400)
                    .entity(createErrorResponse("Room name is required"))
                    .build();
        }

        if (room.getCapacity() <= 0) {
            return Response.status(400)
                    .entity(createErrorResponse("Room capacity must be greater than 0"))
                    .build();
        }

        DataStore.addRoom(room);
        return Response.status(201)
                .entity(createSuccessResponse("Room created successfully", room))
                .build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRoomById(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(createErrorResponse("Room not found"))
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room cannot be deleted as it still contains " + room.getSensorIds().size() + " active sensor(s)"
            );
        }

        DataStore.deleteRoom(roomId);
        return Response.ok(createSuccessResponse("Room deleted successfully", null)).build();
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