# API Reference

## Overview

The RestApi Extension for Krista provides a comprehensive set of catalog requests for interacting with REST APIs. This document provides detailed API reference for all available requests.

## Catalog Requests

### 1. Read Request

**Purpose:** Retrieve data from a REST API endpoint

**Syntax:**
```
Read(
  url: String,
  method: String = "GET",
  headers: Map<String, String> = {},
  queryParams: Map<String, String> = {},
  authentication: AuthType = NONE
)
```

**Parameters:**
- `url` (required) - The REST API endpoint URL
- `method` (optional) - HTTP method (GET, POST, PUT, DELETE, PATCH). Default: GET
- `headers` (optional) - Custom HTTP headers
- `queryParams` (optional) - Query parameters to append to URL
- `authentication` (optional) - Authentication type (NONE, BASIC, OAUTH2, TOKEN)

**Returns:** Response data from the API

**Example:**
```
Read(
  url: "https://api.example.com/users",
  method: "GET",
  headers: {"Accept": "application/json"},
  authentication: BASIC
)
```

### 2. Write Request

**Purpose:** Send data to a REST API endpoint

**Syntax:**
```
Write(
  url: String,
  method: String = "POST",
  payload: Map<String, Object>,
  headers: Map<String, String> = {},
  authentication: AuthType = NONE
)
```

**Parameters:**
- `url` (required) - The REST API endpoint URL
- `method` (optional) - HTTP method (POST, PUT, PATCH). Default: POST
- `payload` (required) - Data to send to the API
- `headers` (optional) - Custom HTTP headers
- `authentication` (optional) - Authentication type

**Returns:** Response from the API

**Example:**
```
Write(
  url: "https://api.example.com/users",
  method: "POST",
  payload: {"name": "John", "email": "john@example.com"},
  authentication: BASIC
)
```

### 3. Modify Request

**Purpose:** Update existing data via REST API

**Syntax:**
```
Modify(
  url: String,
  method: String = "PUT",
  payload: Map<String, Object>,
  headers: Map<String, String> = {},
  authentication: AuthType = NONE
)
```

**Parameters:**
- `url` (required) - The REST API endpoint URL
- `method` (optional) - HTTP method (PUT, PATCH). Default: PUT
- `payload` (required) - Updated data
- `headers` (optional) - Custom HTTP headers
- `authentication` (optional) - Authentication type

**Returns:** Updated resource from the API

### 4. Delete Request

**Purpose:** Delete a resource via REST API

**Syntax:**
```
Delete(
  url: String,
  headers: Map<String, String> = {},
  authentication: AuthType = NONE
)
```

**Parameters:**
- `url` (required) - The REST API endpoint URL
- `headers` (optional) - Custom HTTP headers
- `authentication` (optional) - Authentication type

**Returns:** Confirmation of deletion

### 5. Download Request

**Purpose:** Download files from a REST API endpoint

**Syntax:**
```
Download(
  url: String,
  method: String = "GET",
  headers: Map<String, String> = {},
  authentication: AuthType = NONE
)
```

**Parameters:**
- `url` (required) - The file download URL
- `method` (optional) - HTTP method. Default: GET
- `headers` (optional) - Custom HTTP headers
- `authentication` (optional) - Authentication type

**Returns:** Downloaded file

## Authentication Types

### BASIC
Username and password authentication

### OAUTH2
OAuth 2.0 token-based authentication

### TOKEN
Bearer token authentication

### NONE
No authentication

## Response Format

All responses follow this format:

```json
{
  "Data": { /* Response data */ },
  "Response Info": {
    "Status Code": 200,
    "Status Message": "OK"
  }
}
```

## Error Handling

Errors are returned in the following format:

```json
{
  "Error Message": "Description of the error",
  "Response Info": {
    "Status Code": 400,
    "Status Message": "Bad Request"
  }
}
```

## Common HTTP Status Codes

- `200` - OK - Request successful
- `201` - Created - Resource created successfully
- `204` - No Content - Request successful, no content to return
- `400` - Bad Request - Invalid request parameters
- `401` - Unauthorized - Authentication required
- `403` - Forbidden - Access denied
- `404` - Not Found - Resource not found
- `500` - Internal Server Error - Server error

## Best Practices

1. **Always validate inputs** - Check URL format and required parameters
2. **Handle errors gracefully** - Implement proper error handling
3. **Use appropriate authentication** - Secure sensitive endpoints
4. **Set reasonable timeouts** - Prevent hanging requests
5. **Log API interactions** - For debugging and monitoring
6. **Rate limiting** - Respect API rate limits
7. **Cache responses** - When appropriate, to reduce API calls

## See Also

- [QUICKSTART.md](../QUICKSTART.md) - Quick start guide
- [EXAMPLES.md](EXAMPLES.md) - Usage examples
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - Common issues and solutions

