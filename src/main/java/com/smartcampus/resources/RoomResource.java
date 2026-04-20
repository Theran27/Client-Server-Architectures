package com.smartcampus.resources;

import com.smartcampus.models.Room;
import com.smartcampus.services.DataStore;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;

import java.util.List;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return DataStore.getAllRooms();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Room addRoom(Room room) {
        return DataStore.addRoom(room);
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteRoom(@PathParam("id") int id) {

        // Check if room exists
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

        // Remove room
        DataStore.getAllRooms().remove(roomToRemove);

        return "Room deleted successfully";
    }
}