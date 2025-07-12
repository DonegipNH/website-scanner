# Meta Tag Scanner - Frontend

Vue.js frontend application for the Meta Tag Scanner tool. Provides a modern, responsive interface for scanning websites and analyzing meta tags.

## Features

- **Modern UI**: Built with Element Plus UI library
- **Responsive Design**: Works on desktop and mobile devices
- **Real-time Scanning**: Live website scanning with progress indicators
- **Advanced Filtering**: Dynamic filter management for meta tag search
- **Export Functionality**: Download results in JSON or CSV format
- **Results Visualization**: Organized tables for all and matching meta tags
- **Performance Metrics**: Display scan time and result statistics

## Technology Stack

- **Vue.js 3.2** - Progressive JavaScript framework
- **Element Plus 2.2** - Vue 3 UI component library
- **Axios 0.27** - HTTP client for API communication
- **Vue CLI 5.0** - Build tool and development server
- **ESLint** - Code linting

## Project Structure

```
frontend/
├── public/
│   └── index.html              # Main HTML file
├── src/
│   ├── App.vue                 # Main application component
│   └── main.js                 # Application entry point
├── package.json                # Dependencies and scripts
├── vue.config.js              # Vue CLI configuration
└── README.md                  # This file
```

## Components

### App.vue
The main application component that includes:

- **Header**: Application title and description
- **Scanner Form**: URL input and filter management
- **Results Display**: Tables for all and matching meta tags
- **Export Controls**: Download functionality

### Key Features

1. **URL Input**: Validated input field for website URLs
2. **Dynamic Filters**: Add/remove filter criteria for meta tag search
3. **Real-time Results**: Live display of scan results
4. **Export Options**: Download results in multiple formats
5. **Responsive Tables**: Organized data display with sorting

## Installation and Setup

### Prerequisites

- **Node.js 14** or higher
- **npm** or **yarn** package manager

### Development Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start development server:**
   ```bash
   npm run serve
   ```

The application will be available at `http://localhost:3000`

### Production Build

1. **Build for production:**
   ```bash
   npm run build
   ```

2. **Deploy the `dist` folder** to your web server

## Configuration

### Vue Configuration

Edit `vue.config.js` to customize the build:

```javascript
const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/'
})
```

### API Configuration

The frontend is configured to proxy API requests to the backend:

- **Development**: Proxies to `http://localhost:8080`
- **Production**: Update API base URL in axios configuration

## Usage Guide

### 1. Website Scanning

1. **Enter URL**: Input the website URL you want to scan
2. **Add Filters (Optional)**: Click "Add Filter" to specify search criteria
3. **Configure Filters**: Set name, property, content, http-equiv, or charset filters
4. **Scan**: Click "Scan Website" to start the scanning process

### 2. Filter Management

- **Add Filter**: Click "Add Filter" button to create new filter criteria
- **Remove Filter**: Click "Remove" button on any filter card
- **Filter Fields**:
  - **Name**: Meta tag name attribute
  - **Property**: Meta tag property attribute
  - **Content**: Meta tag content attribute
  - **Http-Equiv**: Meta tag http-equiv attribute
  - **Charset**: Meta tag charset attribute

### 3. Results Viewing

- **All Meta Tags**: View all extracted meta tags from the website
- **Matching Meta Tags**: View only meta tags that match your filters
- **Statistics**: See scan time, total tags, and matching count
- **Table Features**: Sortable columns and responsive design

### 4. Export Results

- **Export Button**: Click "Export Results" to download data
- **Format Selection**: Choose between JSON and CSV formats
- **Automatic Download**: Files are automatically downloaded to your device

## API Integration

### Endpoints Used

1. **POST /api/scan** - Scan website and return meta tags
2. **POST /api/export** - Export results in specified format
3. **GET /api/health** - Health check (not used in UI)

### Request Format

```javascript
// Scan request
{
  url: "https://example.com",
  filters: [
    {
      name: "description",
      property: "",
      content: "",
      httpEquiv: "",
      charset: ""
    }
  ],
  outputFormat: "json"
}
```

### Response Handling

- **Success**: Display results in organized tables
- **Error**: Show error messages using Element Plus notifications
- **Loading**: Display loading indicators during API calls

## Styling and Theming

### CSS Architecture

- **Global Styles**: Applied to the entire application
- **Component Styles**: Scoped to individual components
- **Element Plus**: Pre-built UI components with consistent styling

### Custom Styling

```css
/* Gradient background */
#app {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* Card styling */
.scanner-card {
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

/* Responsive design */
@media (max-width: 768px) {
  .header-content h1 {
    font-size: 2rem;
  }
}
```

## Browser Support

- **Chrome**: 80+
- **Firefox**: 75+
- **Safari**: 13+
- **Edge**: 80+

## Performance Optimization

### Build Optimizations

- **Code Splitting**: Automatic code splitting by Vue Router
- **Tree Shaking**: Unused code elimination
- **Minification**: Production builds are minified
- **Gzip Compression**: Enable on web server

### Runtime Optimizations

- **Lazy Loading**: Components loaded on demand
- **Virtual Scrolling**: For large result sets (future enhancement)
- **Debounced Input**: URL input validation

## Testing

### Manual Testing

1. **URL Validation**: Test with various URL formats
2. **Filter Functionality**: Test adding/removing filters
3. **Export Features**: Test JSON and CSV downloads
4. **Responsive Design**: Test on different screen sizes

### Automated Testing

```bash
# Run linting
npm run lint

# Run tests (if configured)
npm run test
```

## Deployment

### Static Hosting

1. **Build the application:**
   ```bash
   npm run build
   ```

2. **Deploy `dist` folder** to:
   - **Netlify**: Drag and drop deployment
   - **Vercel**: Git integration
   - **AWS S3**: Static website hosting
   - **Nginx**: Traditional web server

### Docker Deployment

```dockerfile
FROM node:16-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## Troubleshooting

### Common Issues

1. **CORS Errors**:
   - Check backend CORS configuration
   - Verify proxy settings in development

2. **API Connection Issues**:
   - Ensure backend is running on port 8080
   - Check network connectivity

3. **Build Errors**:
   - Clear node_modules and reinstall
   - Check Node.js version compatibility

4. **Export Not Working**:
   - Check browser download settings
   - Verify file permissions

### Debug Mode

Enable Vue DevTools for debugging:

1. Install Vue DevTools browser extension
2. Open browser developer tools
3. Navigate to Vue tab for component inspection

## Contributing

1. Follow Vue.js style guide
2. Use Element Plus components when possible
3. Test on multiple browsers
4. Ensure responsive design
5. Update documentation for new features 