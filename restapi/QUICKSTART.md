# REST API Extension - Quick Start Guide

Get up and running with the Krista REST API Extension in less than 10 minutes.

## Prerequisites

Before you begin, ensure you have:

- **Java 21** or higher installed
- **Krista Platform 1.0.118+** running
- **Gradle 8.x** (included via Gradle wrapper)
- Git for cloning the repository

## Step 1: Clone and Build (2 minutes)

```bash
# Clone the repository
git clone <repository-url>
cd krista-global-catalog/restapi

# Build the extension using Gradle wrapper
./gradlew clean build

# The built extension will be in: build/libs/RestAPI-<version>.jar
```

## Step 2: Deploy to Krista (1 minute)

1. Log into your Krista Platform instance
2. Navigate to **Extensions** → **Install Extension**
3. Upload the `RestAPI-<version>.jar` file from `build/libs/`
4. Wait for the extension to install and activate

## Step 3: Configure Authentication (3 minutes)

Choose one of the following authentication methods:

### Option A: Basic Authentication (Simplest)

1. Open the REST API Extension configuration
2. Navigate to the **Authentication** tab
3. Select **Basic Authentication**
4. Enter your credentials:
   - **Username**: Your API username
   - **Password**: Your API password
   - **API URL**: Base URL (e.g., `https://api.example.com`)
5. Click **Test Connection** to verify
6. Click **Save**

### Option B: OAuth 2.0 (Recommended for Production)

1. Open the REST API Extension configuration
2. Navigate to the **Authentication** tab
3. Select **OAuth 2.0**
4. Enter your OAuth credentials:
   - **Client ID**: From your OAuth provider
   - **Client Secret**: From your OAuth provider
   - **Authorization URL**: OAuth authorization endpoint
   - **Access Token URL**: OAuth token endpoint
   - **API URL**: Base URL (e.g., `https://api.example.com`)
5. Click **Authorize** and complete the OAuth flow
6. Click **Save**

> **Quick Setup Guides**: See [Microsoft OAuth Setup](src/main/resources/docs/pages/obtainingClientIDClientSecret.md) or [Google OAuth Setup](src/main/resources/docs/pages/gettingClientIDAndClientSecret.md)

## Step 4: Your First API Request (2 minutes)

Let's make a simple GET request to test the connection:

### Example: Fetch User List

1. In your Krista workflow, add a **REST API Extension** action
2. Select **Basic GET Request**
3. Configure the request:
   - **URL**: `https://jsonplaceholder.typicode.com/users`
4. Run the workflow

**Expected Response Structure:**
```json
{
  "Response Info": {
    "statusCode": 200,
    "statusMessage": "OK",
    "headers": {...},
    "responseTime": "245ms"
  },
  "Response": [
    {
      "id": 1,
      "name": "Leanne Graham",
      "username": "Bret",
      "email": "Sincere@april.biz"
    },
    ...
  ]
}
```

## Supported Features Overview

### HTTP Methods
- **GET** - Retrieve data (with filters, headers, pagination)
- **POST** - Create resources (with headers, query parameters)
- **PUT/PATCH** - Update resources (full or partial updates)
- **DELETE** - Remove resources (with conditional deletion)

### Authentication Types
- **Basic Auth** - Username/password
- **Token-Based** - Bearer tokens, API keys
- **OAuth 2.0** - Full OAuth flow with automatic token refresh

### Advanced Features
- **Pagination** - Handle large datasets efficiently
- **File Operations** - Upload/download files
- **Async Operations** - Long-running tasks with progress tracking
- **Custom Headers** - Add authentication, versioning, tracking headers
- **Query Parameters** - Filter, search, and paginate data

### Supported Formats
- JSON (default)
- XML
- Form data
- Multipart file uploads

## Response Structure Explained

Every API request returns two main components:

### 1. Response Info (Metadata)
Contains HTTP response details:
- `statusCode`: HTTP status (200, 404, 500, etc.)
- `statusMessage`: Status description
- `headers`: Response headers
- `responseTime`: Request duration

### 2. Response (Data)
The actual API response data:
- **Single object**: Returns as FreeForm composite
- **Array**: Returns as List of FreeForm
- **Empty**: Returns empty list or null

## Common Troubleshooting

### Connection Failed
- ✅ Verify the API URL is correct and starts with `https://`
- ✅ Check network connectivity and firewall settings
- ✅ Confirm the API endpoint is accessible

### Authentication Error (401)
- ✅ Verify credentials are correct
- ✅ Check if tokens have expired
- ✅ Re-authorize OAuth connections
- ✅ Use **Test Connection** to validate

### Empty Response
- ✅ Verify the endpoint URL is correct
- ✅ Check query parameters and filters
- ✅ Test the endpoint in Postman or browser

### Response Too Large
- ✅ Use **GET with Pagination** instead of Basic GET
- ✅ Add filters to reduce data volume
- ✅ Request specific fields only

## Next Steps

### 📚 Full Documentation
- [Complete Overview](src/main/resources/docs/pages/overview.md) - Detailed capabilities and use cases
- [Authentication Guide](src/main/resources/docs/pages/authentication.md) - All authentication methods
- [Supported Requests](src/main/resources/docs/pages/supportedRequests.md) - Complete API reference
- [Troubleshooting Guide](src/main/resources/docs/pages/troubleshooting.md) - Comprehensive problem-solving

### 🎯 Common Use Cases
- [Basic GET Request](src/main/resources/docs/pages/basicGet.md) - Simple data retrieval
- [GET with Filters](src/main/resources/docs/pages/getWithFilters.md) - Filtered queries
- [POST Request](src/main/resources/docs/pages/basicPost.md) - Create resources
- [Pagination](src/main/resources/docs/pages/getWithPagination.md) - Handle large datasets

### 🏗️ Architecture
- [Architecture Overview](docs/Architecture.md) - Technical architecture and design patterns

## Support

- **Documentation**: Check the `src/main/resources/docs/` directory
- **Issues**: Report bugs or request features via your issue tracker
- **Email**: support@kristasoft.com

---

**🎉 Congratulations!** You're now ready to integrate with any REST API using the Krista REST API Extension.

