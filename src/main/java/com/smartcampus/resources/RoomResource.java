package com.smartcampus.resources;

import com.smartcampus.models.Room;
import com.smartcampus.services.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return DataStore.getAllRooms();
    }

    //  VALIDATION + HTTP RESPONSE
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addRoom(Room room) {

        if (room.getName() == null || room.getName().isEmpty()) {
            return Response.status(400).entity("Room name is required").build();
        }

        if (room.getBuilding() == null || room.getBuilding().isEmpty()) {
            return Response.status(400).entity("Building is required").build();
        }

        DataStore.addRoom(room);

        return Response.status(201).entity("Room added successfully").build();
    }

    //  DELETE
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteRoom(@PathParam("id") int id) {

        Room roomToRemove = null;

        for (Room room : DataStore.getAllRooms()) {
            if (room.getId() == id) {
                roomToRemove = room;
                break;
            }
        }

        if (roomToRemove == null) {
            return "Room not found";
        }

        DataStore.getAllRooms().remove(roomToRemove);

        return "Room deleted successfully";
    }
}