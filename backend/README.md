# Meta Tag Scanner - Backend

Spring Boot backend application for scanning websites and extracting meta tags.

## Features

- RESTful API for website scanning
- Meta tag extraction using JSoup
- Advanced filtering capabilities
- Export functionality (JSON/CSV)
- CORS support for frontend integration
- Health check endpoint

## Technology Stack

- **Java 11**
- **Spring Boot 2.7.0**
- **JSoup 1.15.3** - HTML parsing
- **Apache Commons CSV 1.9.0** - CSV export
- **Jackson** - JSON processing
- **Maven** - Build tool

## Project Structure

```
src/main/java/com/vts/websitescanner/
├── MetaTagScannerApplication.java    # Main application class
├── controller/
│   └── MetaTagController.java        # REST API endpoints
├── model/
│   ├── MetaTag.java                  # Meta tag data model
│   ├── MetaTagFilter.java            # Filter criteria model
│   ├── ScanRequest.java              # Request model
│   └── ScanResponse.java             # Response model
└── service/
    ├── MetaTagScannerService.java    # Core scanning logic
    └── ExportService.java            # Export functionality
```

## API Endpoints

### 1. Scan Website
**POST** `/api/scan`

Scans a website and returns all meta tags with optional filtering.

**Request Body:**
```json
{
  "url": "https://example.com",
  "filters": [
    {
      "name": "description",
      "property": "og:title",
      "content": "example",
      "httpEquiv": "",
      "charset": ""
    }
  ],
  "outputFormat": "json"
}
```

**Response:**
```json
{
  "url": "https://example.com",
  "allMetaTags": [
    {
      "name": "description",
      "property": "",
      "content": "Example website description",
      "httpEquiv": "",
      "charset": ""
    }
  ],
  "matchingMetaTags": [...],
  "scanTime": 1250,
  "error": null
}
```

### 2. Export Results
**POST** `/api/export`

Exports scan results in specified format (JSON or CSV).

**Request Body:** Same as scan endpoint

**Response:** File download with appropriate headers

### 3. Health Check
**GET** `/api/health`

Returns application status.

**Response:** `"Meta Tag Scanner is running!"`

## Configuration

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.com.vts.websitescanner=DEBUG
logging.level.org.springframework.web=DEBUG

# CORS Configuration
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# Jackson Configuration
spring.jackson.default-property-inclusion=non_null
spring.jackson.serialization.write-dates-as-timestamps=false
```

## Building and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Development

1. **Clone and navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Production

1. **Build JAR file:**
   ```bash
   mvn clean package
   ```

2. **Run JAR file:**
   ```bash
   java -jar target/meta-tag-scanner-1.0.0.jar
   ```

## Docker Support

### Dockerfile
```dockerfile
FROM openjdk:11-jre-slim
COPY target/meta-tag-scanner-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Build and Run
```bash
# Build image
docker build -t meta-tag-scanner-backend .

# Run container
docker run -p 8080:8080 meta-tag-scanner-backend
```

## Testing

### Manual Testing

1. **Health Check:**
   ```bash
   curl http://localhost:8080/api/health
   ```

2. **Scan Website:**
   ```bash
   curl -X POST http://localhost:8080/api/scan \
     -H "Content-Type: application/json" \
     -d '{"url": "https://example.com", "filters": [], "outputFormat": "json"}'
   ```

3. **Export Results:**
   ```bash
   curl -X POST http://localhost:8080/api/export \
     -H "Content-Type: application/json" \
     -d '{"url": "https://example.com", "filters": [], "outputFormat": "csv"}' \
     --output results.csv
   ```

## Error Handling

The application handles various error scenarios:

- **Invalid URLs**: Returns 400 Bad Request
- **Network errors**: Returns 500 Internal Server Error with error message
- **Parsing errors**: Returns 500 Internal Server Error with error details

## Performance Considerations

- **Timeout**: 10 seconds for website requests
- **User Agent**: Uses modern browser user agent to avoid blocking
- **Caching**: No caching implemented (stateless design)
- **Memory**: Efficient memory usage with streaming for large responses

## Security

- **CORS**: Configured for cross-origin requests
- **Input Validation**: URL validation and sanitization
- **Error Messages**: Generic error messages to avoid information disclosure

## Monitoring

### Logging
- Application logs at DEBUG level
- Web request logs enabled
- Error logging with stack traces

### Health Check
- Simple health endpoint for monitoring
- Returns application status 