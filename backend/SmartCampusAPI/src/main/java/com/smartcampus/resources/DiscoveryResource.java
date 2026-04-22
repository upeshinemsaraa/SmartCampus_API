package com.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {
        Map<String, Object> info = new HashMap<>();
        info.put("api", "Smart Campus API");
        info.put("version", "1.0");
        info.put("contact", "admin@smartcampus.ac.uk");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        links.put("readings", "/api/v1/sensors/{sensorId}/readings");
        info.put("resources", links);

        return Response.ok(info).build();
    }
}
