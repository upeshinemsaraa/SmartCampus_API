# Smart Campus Sensor & Room Management API
**Developer:** Upeshi Sorajapathi
**Student ID:** 20250155 / w2153580
IIT: University of Westminster
**Module:** 5COSC022W - Client-Server Architecture

## Overview
This is a RESTful API built using JAX-RS (Jersey) and deployed on Apache Tomcat.
It manages Rooms, Sensors, and Sensor Readings for a university Smart Campus system.
All data is stored in-memory using ConcurrentHashMap (no database used).

## Technology Stack
- Java (JAX-RS javax.ws.rs)
- Jersey 2.35 (JAX-RS Implementation)
- Apache Tomcat 9
- Maven
- Jackson (JSON support)

## How to Build and Run
1. Clone the repository:
git clone https://github.com/upeshinemsaraa/SmartCampus_API
2. Open the project in NetBeans as a Maven Web Application
3. Make sure Apache Tomcat 9 is configured in NetBeans
4. Right-click the project → Clean and Build
5. Right-click the project → Run
6. Open browser and go to: http://localhost:8080/SmartCampusAPI/

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/ | Discovery endpoint |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a new room |
| GET | /api/v1/rooms/{id} | Get a specific room |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| GET | /api/v1/sensors | Get all sensors |
| GET | /api/v1/sensors?type=CO2 | Filter sensors by type |
| POST | /api/v1/sensors | Register a new sensor |
| GET | /api/v1/sensors/{id} | Get a specific sensor |
| GET | /api/v1/sensors/{id}/readings | Get all readings for a sensor |
| POST | /api/v1/sensors/{id}/readings | Add a new reading |


## Sample curl Commands
**1. Get API Discovery:**
```
curl http://localhost:8080/SmartCampusAPI/api/v1/
```

**2. Get all rooms:**
```
curl http://localhost:8080/SmartCampusAPI/api/v1/rooms
```

**3. Create a new room:**
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"CS-101","name":"Computer Science Lab","capacity":40}'
```

**4. Try to delete a room with sensors (shows 409 error):**
```
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```

**5. Register a sensor with invalid roomId (shows 422 error):**
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"FAKE-ROOM"}'
```

**6. Register a valid sensor:**
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"LAB-101"}'
```

**7. Filter sensors by type:**
```
curl http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2
```

**8. Add a sensor reading:**
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":25.5}'
```

  
## Report: Answers to Coursework Questions
### Part 1.1 - JAX-RS Resource Lifecycle
By default, JAX-RS creates a new instance of each resource class for every incoming HTTP request. This is called request scoped lifecycle. It means you cannot store shared data inside instance variables of a resource class because each request gets a completely fresh object with no memory of previous requests.
To share data across all requests, static data structures must be used. In this project, a centralised `DataStore` class holds all data using `static ConcurrentHashMap` fields. A `ConcurrentHashMap` is used instead of a regular `HashMap` because multiple requests can arrive at the same time from different users. Without thread-safe storage, two simultaneous requests could corrupt the data, causing race conditions or data loss. `ConcurrentHashMap` handles this safely by allowing concurrent reads and writes without crashing.

### Part 1.2 - HATEOAS (Hypermedia as the Engine of Application State)
HATEOAS is the practice of including links inside API responses that tell the client what actions or resources are available next. Instead of requiring developers to read separate documentation to know what URLs exist, the API itself guides them through its structure dynamically.
This benefits client developers because they do not need to hardcode URLs. If the API changes its URL structure in a future version, clients following the links will automatically adapt. It also makes the API self-documenting — a developer can start at the discovery endpoint and navigate the entire API just by following the links provided in responses, without ever consulting external documentation.

### Part 2.1 - Returning IDs vs Full Objects in Lists
When returning a list of rooms, returning only IDs keeps the response very small and fast, which is good for network bandwidth. However, the client then needs to make a separate request for each ID to get the full details, which creates an N+1 problem if there are 100 rooms, the client must make 101 requests total.
Returning full objects increases the payload size but eliminates those extra round trips. The client gets everything it needs in one request. The best approach depends on the use case. For a list/summary view, lightweight responses with key fields are preferred. For detail views, full objects are returned. This project returns full objects in the list for simplicity and performance.

### Part 2.2 - Idempotency of DELETE
Yes, DELETE is idempotent in this implementation. The first DELETE request for a room removes it from the data store and returns HTTP 204 No Content. If the same DELETE request is sent again, the room no longer exists, so the API returns HTTP 404 Not Found. In both cases the server state is identical after the request the room does not exist. The result is consistent regardless of how many times the request is repeated, which satisfies the REST definition of idempotency.

### Part 3.1 - @Consumes and Content-Type Mismatch
The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells JAX-RS that this endpoint only accepts requests with `Content-Type: application/json`. If a client sends data with a different content type such as `text/plain` or `application/xml`, JAX-RS automatically rejects the request before the method even executes and returns **HTTP 415 Unsupported Media Type**. This protects the method from receiving data it cannot deserialize into a Java object and prevents unexpected errors inside the business logic.

### Part 3.2 - Query Parameters vs Path Parameters for Filtering
Query parameters such as `/sensors?type=CO2` are the correct approach for filtering because they modify the representation of a collection rather than identifying a specific resource. A path parameter such as `/sensors/type/CO2` implies that a unique resource physically exists at that path, which is misleading for a filter operation.
Query parameters are also naturally optional — if no type is provided, all sensors are returned. They support multiple filters easily, for example `?type=CO2&status=ACTIVE`, without changing the URL structure. They are the widely accepted REST convention for search and filter operations on collections.

### Part 4.1 - Sub-Resource Locator Pattern Benefits
The sub-resource locator pattern delegates the handling of nested paths to a separate dedicated class. In this project, `SensorResource` delegates `/sensors/{sensorId}/readings` to `SensorReadingResource`. This keeps each class focused on a single responsibility.
In large APIs with many nested resources, placing all logic in one controller creates an unmaintainable class. Separate resource classes are independently readable, testable, and maintainable. Different developers can work on different resource classes simultaneously without conflicts. It also makes the codebase easier to extend adding new nested resources only requires creating a new class and a new locator method, without touching existing code.

### Part 5.1 - Why HTTP 422 Instead of 404
HTTP 404 Not Found means the requested URL does not exist on the server. HTTP 422 Unprocessable Entity means the URL is valid and the JSON payload is syntactically correct, but the data inside it references something that does not exist in this case a roomId that has no matching room.
The URL `/api/v1/sensors` exists and works correctly, so 404 would be misleading. The problem is specifically with the content of the payload, not the URL. HTTP 422 more precisely communicates that the server understood the request but cannot process it due to a semantic error in the data. This helps client developers immediately understand that they need to fix the data they are sending, not the URL they are calling.

### Part 5.2 - Security Risks of Exposing Stack Traces
Exposing Java stack traces to external API consumers is a serious security risk for several reasons. Stack traces reveal internal file paths and package names, showing attackers the exact structure of the codebase. They expose the names and versions of third-party libraries being used, allowing attackers to look up known vulnerabilities for those specific versions. They reveal method names and line numbers, helping attackers understand the internal logic and flow of the application. They can also expose database connection details or configuration information if the exception originated from those layers.
The 'GlobalExceptionMapper' in this project prevents all of this by catching every unexpected error and returning only a generic HTTP 500 message to the client. The real error is logged securely on the server side only, where only authorised developers can see it.

### Part 5.3 - Benefits of Using Filters for Logging
Using JAX-RS filters for cross-cutting concerns like logging follows the DRY (Don't Repeat Yourself) principle. A single filter class automatically applies to every single endpoint in the API without any extra code. There is no risk of forgetting to add logging when a new endpoint is created.
Manually inserting `Logger.info()` calls inside every resource method scatters the logging concern across the entire codebase, creates inconsistency if some methods are missed, and mixes infrastructure code with business logic. Filters keep the resource classes clean and focused purely on their business logic, while the filter handles logging transparently in the background for every request and response.
