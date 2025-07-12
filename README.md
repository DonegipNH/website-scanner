# Meta Tag Scanner Tool

A web-based tool for scanning websites and analyzing meta tags with advanced filtering capabilities. Built with Spring Boot (Java 11) backend and Vue.js frontend.

## Features

- **Website Scanning**: Scan any website URL and extract all meta tags
- **Advanced Filtering**: Filter meta tags by name, property, content, http-equiv, and charset
- **Multiple Export Formats**: Export results in JSON or CSV format
- **Modern UI**: Beautiful, responsive interface built with Element Plus
- **Real-time Results**: View all meta tags and matching results in organized tables
- **Performance Metrics**: Track scan time and result statistics

## Tech Stack

### Backend
- **Java 11**
- **Spring Boot 2.7.0**
- **JSoup** - HTML parsing and web scraping
- **Apache Commons CSV** - CSV export functionality
- **Jackson** - JSON processing

### Frontend
- **Vue.js 3.2**
- **Element Plus** - UI component library
- **Axios** - HTTP client
- **Vue CLI** - Build tool

## Project Structure

```
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/vts/websitescanner/
│   │       ├── controller/  # REST controllers
│   │       ├── model/       # Data models
│   │       ├── service/     # Business logic
│   │       └── MetaTagScannerApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                # Vue.js application
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── App.vue         # Main application component
│   │   └── main.js         # Application entry point
│   ├── package.json
│   └── vue.config.js
└── README.md
```

## Quick Start

### Prerequisites

- Java 11 or higher
- Node.js 14 or higher
- Maven 3.6 or higher

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run serve
   ```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Scan Website
- **POST** `/api/scan`
- **Body**: 
  ```json
  {
    "url": "https://example.com",
    "filters": [
      {
        "name": "description",
        "property": "",
        "content": "",
        "httpEquiv": "",
        "charset": ""
      }
    ],
    "outputFormat": "json"
  }
  ```

### Export Results
- **POST** `/api/export`
- **Body**: Same as scan endpoint
- **Response**: File download (CSV or JSON)

### Health Check
- **GET** `/api/health`
- **Response**: Application status

## Usage

1. **Enter Website URL**: Input the website URL you want to scan
2. **Add Filters (Optional)**: Click "Add Filter" to specify criteria for filtering meta tags
3. **Scan Website**: Click "Scan Website" to start the scanning process
4. **View Results**: Results are displayed in organized tables showing all meta tags and matching results
5. **Export Results**: Click "Export Results" to download the data in your preferred format

## Filter Options

- **Name**: Filter by meta tag name attribute
- **Property**: Filter by meta tag property attribute
- **Content**: Filter by meta tag content attribute
- **Http-Equiv**: Filter by meta tag http-equiv attribute
- **Charset**: Filter by meta tag charset attribute

All filters support partial matching and are case-insensitive.

## Deployment

### Backend Deployment

1. Build the JAR file:
   ```bash
   cd backend
   mvn clean package
   ```

2. Run the JAR file:
   ```bash
   java -jar target/meta-tag-scanner-1.0.0.jar
   ```

### Frontend Deployment

1. Build for production:
   ```bash
   cd frontend
   npm run build
   ```

2. Deploy the `dist` folder to your web server

### Docker Deployment

Create a `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
  
  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

## Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# CORS Configuration
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```

### Frontend Configuration

Edit `frontend/vue.config.js` to change the API proxy settings:

```javascript
module.exports = defineConfig({
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please open an issue in the repository. 