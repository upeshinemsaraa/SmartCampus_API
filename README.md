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
curl http://localhost:8080/SmartCampusAPI/api/v1/
**2. Get all rooms:**
curl http://localhost:8080/SmartCampusAPI/api/v1/rooms
**3. Create a new room:**
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"CS-101","name":"Computer Science Lab","capacity":40}'
**4. Try to delete a room with sensors (shows 409 error):**
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
**5. Register a sensor with invalid roomId (shows 422 error):**
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"FAKE-ROOM"}'
**6. Register a valid sensor:**
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"CO2-001","type":"CO2","status":"ACTIVE","currentValue":400,"roomId":"LAB-101"}'
**7. Filter sensors by type:**
curl http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2
**8. Add a sensor reading:**
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":25.5}'
  
