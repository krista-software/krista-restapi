# Basic GET Request

## Overview

The Basic GET request is the simplest form of data retrieval operation. It fetches data from a specified API endpoint without any additional parameters, filters, or custom headers.

## Use Cases

- Retrieve all records from an endpoint
- Fetch basic resource information
- Test API connectivity
- Access public endpoints that don't require parameters

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata (status, headers, timing) |
| Response | List Of FreeForm | Retrieved data from the API |

## HTTP Request Format

```http
GET {URL} HTTP/1.1
Host: api.example.com
User-Agent: Krista-REST-Extension/2.0
```

## Example Usage

### Simple User List Retrieval

**Configuration:**
- URL: `https://jsonplaceholder.typicode.com/users`

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz"
  },
  {
    "id": 2,
    "name": "Ervin Howell",
    "username": "Antonette",
    "email": "Shanna@melissa.tv"
  }
]
```

### API Health Check

**Configuration:**
- URL: `https://api.example.com/health`

**Expected Response:**
```json
{
  "status": "healthy",
  "timestamp": "2024-01-15T10:30:00Z",
  "version": "1.2.3"
}
```

## Response Information Details

The `Response Info` parameter contains comprehensive metadata about the HTTP response:

### Status Information
- **Status Code**: HTTP response code (200, 404, 500, etc.)
- **Status Message**: Human-readable status description
- **Success**: Boolean indicating if request was successful

### Headers
- **Content-Type**: Response content format
- **Content-Length**: Size of response body
- **Server**: Server information
- **Date**: Response timestamp

### Timing
- **Response Time**: Total request duration
- **Connection Time**: Time to establish connection
- **Processing Time**: Server processing duration

## Error Handling

### Common Error Scenarios

#### 404 Not Found
```json
{
  "error": "Resource not found",
  "code": 404,
  "message": "The requested endpoint does not exist"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "code": 500,
  "message": "An unexpected error occurred"
}
```

#### Network Timeout
```json
{
  "error": "Request timeout",
  "code": "TIMEOUT",
  "message": "Request exceeded maximum timeout duration"
}
```

## Best Practices

### URL Construction
- Use HTTPS for secure communication
- Ensure URLs are properly encoded
- Validate endpoint accessibility before deployment

### Error Handling
- Always check the Response Info for status codes
- Implement retry logic for transient failures
- Log errors for debugging and monitoring

### Performance
- Use appropriate timeouts for your use case
- Consider caching for frequently accessed data
- Monitor response times and optimize as needed

## Security Considerations

- **Public Endpoints**: Ensure endpoints don't expose sensitive data
- **Rate Limiting**: Respect API rate limits to avoid blocking
- **SSL/TLS**: Always use HTTPS in production environments
- **Monitoring**: Log and monitor API access patterns

## Troubleshooting

### Request Fails
1. Verify the URL is correct and accessible
2. Check network connectivity
3. Confirm the endpoint exists and is operational
4. Review any authentication requirements

### Unexpected Response
1. Validate the endpoint returns expected data format
2. Check for API version compatibility
3. Review API documentation for changes
4. Verify response parsing logic

## Related Operations

- [GET with Query Parameters](pages/getWithFilters.md) - Add filtering and search
- [GET with Headers](pages/getWithHeaders.md) - Include authentication and custom headers
- [GET with Pagination](pages/getWithPagination.md) - Handle large datasets
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

Once you've successfully implemented basic GET requests, consider:
1. Adding query parameters for filtering
2. Including authentication headers
3. Implementing pagination for large datasets
4. Setting up error handling and retry logic
