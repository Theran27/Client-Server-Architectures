# Smart Campus Sensor & Room Management API (Final)

## Overview

The **Smart Campus API** is a RESTful web service built using **JAX-RS (Jersey)** to manage rooms, sensors, and sensor readings in a university smart-campus environment.

This project is aligned with the Client-Server Architectures coursework requirements and demonstrates:
- RESTful resource design
- Nested sub-resources
- Custom exception mapping
- Request/response logging
- In-memory data management (no database)

---

## Technology Stack

- **Language:** Java 17
- **Framework:** JAX-RS (Jersey 2.35)
- **Build Tool:** Maven
- **Deployment:** Apache Tomcat 9
- **Runtime Port:** `8081`
- **Storage:** In-memory collections (`ArrayList`, `HashMap`-style usage)

---

## Project Structure

```text
smart-campus-api/
  pom.xml
  src/
    main/
      java/com/smartcampus/
        AppConfig.java
        models/
          Room.java
          Sensor.java
          SensorReading.java
        resources/
          DiscoveryResource.java
          RoomResource.java
          SensorResource.java
          SensorReadingResource.java
          ReadingResource.java
          TestResource.java
        services/
          DataStore.java
        exceptions/
          RoomNotEmptyException.java
          RoomNotEmptyExceptionMapper.java
          LinkedResourceNotFoundException.java
          LinkedResourceNotFoundExceptionMapper.java
          SensorUnavailableException.java
          SensorUnavailableExceptionMapper.java
          GlobalExceptionMapper.java
        filters/
          LoggingFilter.java
      webapp/WEB-INF/web.xml
```

---

## Build and Run (Tomcat on 8081)

## Prerequisites

- JDK 17+
- Maven 3.6+
- Apache Tomcat 9.x configured on port `8081`

## 1) Build the project

```powershell
cd "C:\Users\Thiviru\Desktop\Thiviru\CSA-Final\smart-campus-api"
mvn clean package
```

Output WAR:
- `target/smart-campus-api-1.0-SNAPSHOT.war`

## 2) Deploy to Tomcat

### Option A: Copy WAR to webapps (recommended)

```powershell
Copy-Item ".\target\smart-campus-api-1.0-SNAPSHOT.war" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\smart-campus-api.war" -Force
```

### Option B: Tomcat Manager

- Open: `http://localhost:8081/manager/html`
- Deploy WAR through UI

## 3) Restart Tomcat (PowerShell)

```powershell
cd "C:\Program Files\Apache Software Foundation\Tomcat 9.0\bin"
.\shutdown.bat
.\startup.bat
```

## 4) Verify API

```powershell
curl.exe "http://localhost:8081/smart-campus-api/"
curl.exe "http://localhost:8081/smart-campus-api/api/v1/test"
```

---

## Key API Endpoints (Port 8081)

Base URL:
- `http://localhost:8081/smart-campus-api`

Examples:
- Discovery: `GET /` or `GET /api/v1`
- Rooms: `GET /api/v1/rooms`
- Sensors: `GET /api/v1/sensors`
- Readings: `GET /api/v1/sensors/{sensorId}/readings`

---

## Sample cURL Commands (8081)

### 1) Discovery

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1"
```

### 2) Get all rooms

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1/rooms"
```

### 3) Create room

```powershell
curl.exe -X POST "http://localhost:8081/smart-campus-api/api/v1/rooms" -H "Content-Type: application/json" -d "{\"id\":\"ENG-405\",\"name\":\"Engineering Lab 4A\",\"capacity\":40}"
```

### 4) Get room by ID

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1/rooms/LIB-301"
```

### 5) Get sensors by type

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1/sensors?type=Temperature"
```

### 6) Create sensor

```powershell
curl.exe -X POST "http://localhost:8081/smart-campus-api/api/v1/sensors" -H "Content-Type: application/json" -d "{\"id\":\"CO2-002\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":400.0,\"roomId\":\"LIB-301\"}"
```

### 7) Get readings for sensor

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1/sensors/TEMP-001/readings"
```

### 8) Add reading to sensor

```powershell
curl.exe -X POST "http://localhost:8081/smart-campus-api/api/v1/sensors/TEMP-001/readings" -H "Content-Type: application/json" -d "{\"value\":27.8}"
```

### 9) Delete sensor

```powershell
curl.exe -X DELETE "http://localhost:8081/smart-campus-api/api/v1/sensors/CO2-002"
```

### 10) Delete room with sensors (expected 409)

```powershell
curl.exe -i -X DELETE "http://localhost:8081/smart-campus-api/api/v1/rooms/LIB-301"
```

### 11) Health check

```powershell
curl.exe "http://localhost:8081/smart-campus-api/api/v1/test"
```

---

## Error Handling Summary

| Status | Meaning | Implemented Mapper |
|---|---|---|
| 409 | Room has active sensors | `RoomNotEmptyExceptionMapper` |
| 422 | Invalid linked room reference | `LinkedResourceNotFoundExceptionMapper` |
| 403 | Sensor unavailable (MAINTENANCE/OFFLINE) | `SensorUnavailableExceptionMapper` |
| 500 | Unexpected server error | `GlobalExceptionMapper` |

---

## Coursework Questions and Answers

## Coursework Questions and Answers

---

# Part 1: Service Architecture & Setup

## Coursework Questions and Answers

---

# Part 1: Service Architecture & Setup

## 1. Project & Application Configuration

**Question:**  
In your report, explain the default lifecycle of a JAX-RS resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

Returning only room IDs reduces the amount of data sent over the network, so it is more lightweight and uses less bandwidth. This can be useful when a client only needs identifiers and will fetch details separately when necessary.  
However, returning only IDs also increases client-side work because the client must make additional requests to retrieve details for each room. This creates more round trips between the client and server and can slow down the application, especially if many rooms are being processed.

Returning full room objects, on the other hand, gives the client complete information in a single request. This reduces the need for extra API calls and simplifies client logic. In my implementation, returning the full room objects is more practical for management dashboards or admin interfaces because the client can immediately display the room name, capacity, and sensor IDs.

So the trade-off is  
IDs only: less bandwidth, but more client-side requests and processing  
Full objects: more bandwidth, but simpler client usage and fewer requests

---

## 2. The ”Discovery” Endpoint

**Question:**  
Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

Yes, the DELETE operation is idempotent in my implementation. In REST, idempotency means that making the same request multiple times results in the same final server state.  
In my RoomResource, if a room exists and has no assigned sensors, the first DELETE request removes it successfully. If the client sends the same DELETE request again for the same room, the room is no longer present, so the second request will return a “Room not found” response instead of deleting anything again. The final outcome remains the same: the room does not exist.  
If the room still contains sensors, the delete request is blocked and a custom RoomNotEmptyException is thrown, which is mapped to HTTP 409 Conflict. In that case, repeating the same DELETE request also produces the same result until the room becomes empty.

So the operation is idempotent because:  
the first successful DELETE removes the resource  
repeated DELETE requests do not change the state further  
repeated requests either return not found or the same conflict rule  
This is consistent with REST design, where DELETE is expected to be idempotent.

---

# Part 2: Room Management

## 1. Room Resource Implementation

**Question:**  
When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

When a POST method is annotated with @Consumes(MediaType.APPLICATION_JSON), it means the endpoint is designed to accept JSON only. If a client sends a different content type such as text/plain or application/xml, JAX-RS will not match that request to the method’s expected media type.

The result is usually:

HTTP 415 Unsupported Media Type  
This happens because the framework cannot find a suitable message body reader to convert the incoming data into the Java object expected by the resource method.  
In my project, this is important because sensor creation and room creation are designed to receive JSON payloads. If a client sends XML or plain text, the request is rejected before the business logic runs. This protects the API from invalid or unexpected formats and ensures that the server processes only supported requests.

So the consequences are:  
the request is rejected early  
the resource method is not executed  
JAX-RS returns an error like 415 Unsupported Media Type  
the API stays strict about JSON input

---

## 2. Room Deletion & Safety Logic

**Question:**  
Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Yes, the DELETE operation is idempotent in my implementation. In REST, idempotency means that making the same request multiple times results in the same final server state.  
In my RoomResource, if a room exists and has no assigned sensors, the first DELETE request removes it successfully. If the client sends the same DELETE request again for the same room, the room is no longer present, so the second request will return a “Room not found” response instead of deleting anything again. The final outcome remains the same: the room does not exist.  
If the room still contains sensors, the delete request is blocked and a custom RoomNotEmptyException is thrown, which is mapped to HTTP 409 Conflict. In that case, repeating the same DELETE request also produces the same result until the room becomes empty.

So the operation is idempotent because:  
the first successful DELETE removes the resource  
repeated DELETE requests do not change the state further  
repeated requests either return not found or the same conflict rule

---

# Part 3: Sensor Operations & Linking

## 1. Sensor Resource & Integrity

**Question:**  
We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

When a POST method is annotated with @Consumes(MediaType.APPLICATION_JSON), it means the endpoint is designed to accept JSON only. If a client sends a different content type such as text/plain or application/xml, JAX-RS will not match that request to the method’s expected media type.  
The result is usually:  
HTTP 415 Unsupported Media Type  
This happens because the framework cannot find a suitable message body reader to convert the incoming data into the Java object expected by the resource method.  
In my project, this is important because sensor creation and room creation are designed to receive JSON payloads. If a client sends XML or plain text, the request is rejected before the business logic runs. This protects the API from invalid or unexpected formats and ensures that the server processes only supported requests.

So the consequences are:  
the request is rejected early  
the resource method is not executed  
JAX-RS returns an error like 415 Unsupported Media Type  
the API stays strict about JSON input

---

## 2. Filtered Retrieval & Search

**Question:**  
You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

Using @QueryParam is generally better for filtering and searching collections because filtering is an optional search criterion, not a unique resource identity.  
For example, /api/v1/sensors?type=CO2 clearly expresses that the client wants to search or filter the sensor collection by type. This is more RESTful for collection filtering than using a path segment like /api/v1/sensors/type/CO2, which can make the URL structure look like a separate resource hierarchy instead of a query operation.

The advantages of @QueryParam are:

It is more semantically appropriate for searching/filtering  
It supports optional parameters easily  
Multiple filters can be combined, such as ?type=CO2&status=ACTIVE  
The base collection endpoint remains clean and reusable  
It is easier to extend later without changing the URL structure

In contrast, path-based filtering is better for identifying specific resources or nested resource relationships. For filtering a collection, query parameters are usually the cleaner and more flexible design.

---

# Part 4: Deep Nesting with Sub- Resources

## 1. The Sub-Resource Locator Pattern

**Question:**  
Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

The Sub-Resource Locator pattern is useful when a resource contains nested or related sub-resources. In my project, SensorResource uses a sub-resource locator to delegate /sensors/{sensorId}/readings to SensorReadingResource.

The benefits are:  
It keeps the code modular and organized  
Each class has a single responsibility  
The API structure matches the real-world domain hierarchy  
It is easier to maintain and extend  
Nested logic does not overload one large controller class

If I put all nested paths such as sensors/{id}/readings/{rid} inside one massive controller, the class would become harder to read, test, and modify. As the API grows, such a controller would become complex and error-prone.  
By separating the reading logic into SensorReadingResource, I made the design cleaner:

SensorResource handles sensor collection and sensor-level operations  
SensorReadingResource handles historical readings for one sensor

This improves maintainability and is much more scalable for large APIs.

---

# Part 5: Advanced Error Handling, Exception Mapping & Logging

## 2. Dependency Validation (422 Unprocessable Entity)

**Question:**  
Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP 422 Unprocessable Entity is more accurate because the request itself is syntactically valid, but it cannot be processed due to a semantic problem in the payload.  
For example, if a client sends a valid JSON sensor object, but the roomId field points to a room that does not exist, then the problem is not that the endpoint is missing. The endpoint exists and the JSON format is valid. The issue is that the data inside the payload refers to a non-existent linked resource.  
A 404 Not Found usually means the requested URL or resource itself does not exist. In this case, the URL is valid; the issue is in the relationship between the sensor and the room. Therefore, 422 is the better status code because it tells the client that the request was understood but cannot be processed because of invalid business data.

---

## 4. The Global Safety Net (500)

**Question:**  
From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing raw Java stack traces to external clients is dangerous because stack traces reveal internal implementation details that attackers can use to exploit the system.

A stack trace may expose:  
class names and package names  
file names and source structure  
method names  
line numbers where the failure occurred  
framework and library versions  
database or query-related details if they are included in the exception  
environment or server configuration hints

An attacker can use this information to identify weak points in the application, understand how the code is structured, and look for known vulnerabilities in the technologies being used. Stack traces also help attackers craft more precise malicious requests.  
For that reason, my global exception mapper catches unexpected runtime errors and returns a generic HTTP 500 response without exposing the internal stack trace. This improves security and prevents information leakage.

---

## 5. API Request & Response Logging Filters

**Question:**  
Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single re source method?

Using JAX-RS filters for logging is better because logging is a cross-cutting concern. That means it applies to all endpoints, not just one resource method.

The advantages are:  
Logging logic is centralized in one place  
There is no need to repeat logging code in every method  
All requests and responses are logged consistently  
Resource methods stay focused on business logic  
It is easier to maintain and change logging behavior later  
It reduces the chance of forgetting to log in some endpoints

In my project, the LoggingFilter logs:

the HTTP method  
the request URI  
the final response status

This gives good observability without cluttering the resource classes. If I had manually added logging to every resource method, the code would become repetitive, harder to maintain, and more error-prone.

---

## Video Demonstration Checklist (Postman)

- Confirm camera and voice are clear.
- Show deployed app on Tomcat (`8081`).
- Run success calls and error scenarios (409, 422, 403).
- Briefly explain each call and expected status code.
- Keep recording under 10 minutes.

---

## Submission Notes

- Public GitHub repository link is required.
- Include this report content in GitHub README.
- Submit video directly to Blackboard.
- Do not submit ZIP files.

---

## License

This project is created for educational assessment under University of Westminster (5COSC022C/5COSC022W Client-Server Architectures).

