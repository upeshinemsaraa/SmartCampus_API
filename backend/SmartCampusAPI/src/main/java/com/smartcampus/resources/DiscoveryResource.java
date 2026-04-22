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
    info.put("name", "Smart Campus Sensor & Room Management API");
    info.put("version", "v1");
    info.put("description", "A RESTful API for managing campus rooms, sensors and sensor readings.");

    Map<String, String> contact = new HashMap<>();
    contact.put("owner", "Upeshi");
    contact.put("studentId", "w2153580");
    contact.put("email", "w2153580@westminster.ac.uk");
    info.put("contact", contact);

    Map<String, String> links = new HashMap<>();
    links.put("self", "/api/v1/");
    links.put("rooms", "/api/v1/rooms");
    links.put("sensors", "/api/v1/sensors");
    links.put("readings", "/api/v1/sensors/{sensorId}/readings");
    info.put("resources", links);

    Map<String, String> status = new HashMap<>();
    status.put("server", "Running");
    status.put("storage", "In-Memory");
    info.put("status", status);

    return Response.ok(info).build();
} 
}
