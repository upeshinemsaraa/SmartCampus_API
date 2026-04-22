package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET /api/v1/rooms - returns all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    // POST /api/v1/rooms - creates a new room
    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Room ID is required\"}")
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Room with this ID already exists\"}")
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    // GET /api/v1/rooms/{roomId} - get one specific room
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Room not found: " + roomId + "\"}")
                    .build();
        }
        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - delete a room
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Room not found: " + roomId + "\"}")
                    .build();
        }
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}
