# Troubleshooting Guide

This guide helps you resolve common issues with the RestApi Extension for Krista.

## Connection Issues

### Problem: Connection Timeout

**Symptoms:**
- Request hangs and eventually times out
- Error: "Connection timeout"

**Solutions:**
1. Check if the API endpoint is accessible
2. Verify the URL is correct
3. Check your network connection
4. Increase timeout settings if needed
5. Check if the API server is down

**Example:**
```
// Test connectivity
Read(
  url: "https://api.example.com/health",
  method: "GET"
)
```

### Problem: Connection Refused

**Symptoms:**
- Error: "Connection refused"
- Cannot reach the API endpoint

**Solutions:**
1. Verify the API URL is correct
2. Check if the API server is running
3. Verify firewall settings
4. Check if you need a VPN or proxy
5. Verify the port number is correct

## Authentication Issues

### Problem: 401 Unauthorized

**Symptoms:**
- Error: "401 Unauthorized"
- Authentication fails

**Solutions:**
1. Verify credentials are correct
2. Check if the token has expired
3. Verify authentication type is correct
4. Check if API key is valid
5. Ensure credentials are properly configured

**Example:**
```
// Verify authentication
Read(
  url: "https://api.example.com/auth/verify",
  method: "GET",
  authentication: BASIC
)
```

### Problem: 403 Forbidden

**Symptoms:**
- Error: "403 Forbidden"
- Access denied even with valid credentials

**Solutions:**
1. Verify user has required permissions
2. Check API scope/permissions
3. Verify API key has correct permissions
4. Check if account is active
5. Contact API provider for access

### Problem: Invalid Token

**Symptoms:**
- Error: "Invalid token" or "Token expired"
- OAuth 2.0 authentication fails

**Solutions:**
1. Refresh the OAuth token
2. Re-authenticate with the API
3. Check token expiration time
4. Verify token format is correct
5. Check if token has been revoked

## Request Issues

### Problem: 400 Bad Request

**Symptoms:**
- Error: "400 Bad Request"
- Invalid request parameters

**Solutions:**
1. Verify URL format is correct
2. Check payload JSON syntax
3. Verify required parameters are included
4. Check parameter data types
5. Verify headers are correct

**Example:**
```
// Validate JSON payload
Write(
  url: "https://api.example.com/users",
  method: "POST",
  payload: {
    "name": "John",
    "email": "john@example.com"
  }
)
```

### Problem: 404 Not Found

**Symptoms:**
- Error: "404 Not Found"
- Resource doesn't exist

**Solutions:**
1. Verify the resource ID is correct
2. Check if the resource has been deleted
3. Verify the endpoint path is correct
4. Check API documentation for correct endpoint
5. Verify the resource exists before accessing

### Problem: 405 Method Not Allowed

**Symptoms:**
- Error: "405 Method Not Allowed"
- HTTP method not supported

**Solutions:**
1. Verify the HTTP method is correct (GET, POST, PUT, DELETE)
2. Check API documentation for supported methods
3. Verify the endpoint supports the method
4. Try a different HTTP method if applicable

## Response Issues

### Problem: Empty Response

**Symptoms:**
- Request succeeds but returns no data
- Response is null or empty

**Solutions:**
1. Verify the resource exists
2. Check if the response format is correct
3. Verify query parameters are correct
4. Check if pagination is needed
5. Verify the API returns data for the request

### Problem: Malformed Response

**Symptoms:**
- Error parsing response
- Invalid JSON in response

**Solutions:**
1. Verify the API returns valid JSON
2. Check response headers (Content-Type)
3. Verify the response is not HTML error page
4. Check if the API is returning an error
5. Contact API provider if issue persists

## Rate Limiting

### Problem: 429 Too Many Requests

**Symptoms:**
- Error: "429 Too Many Requests"
- Rate limit exceeded

**Solutions:**
1. Implement request throttling
2. Add delays between requests
3. Check API rate limit documentation
4. Use caching to reduce requests
5. Request higher rate limit from API provider

**Example:**
```
// Add delay between requests
// Wait 1 second before next request
```

## Performance Issues

### Problem: Slow Requests

**Symptoms:**
- Requests take a long time to complete
- Performance degradation

**Solutions:**
1. Check network latency
2. Verify API server performance
3. Optimize request payload size
4. Use pagination for large datasets
5. Implement caching
6. Check for network issues

### Problem: Large Response Handling

**Symptoms:**
- Memory issues with large responses
- Timeout on large data transfers

**Solutions:**
1. Use pagination to fetch data in chunks
2. Filter response data if possible
3. Implement streaming for large files
4. Increase memory allocation
5. Use compression if supported

## Debugging Tips

### Enable Logging

```
// Check logs for detailed error information
// Enable DEBUG level logging in configuration
```

### Test with Simple Requests

```
// Start with a simple GET request
Read(
  url: "https://jsonplaceholder.typicode.com/posts/1",
  method: "GET"
)
```

### Verify API Endpoint

```
// Test API health endpoint
Read(
  url: "https://api.example.com/health",
  method: "GET"
)
```

### Check Headers

```
// Verify headers are correct
Read(
  url: "https://api.example.com/data",
  method: "GET",
  headers: {
    "Accept": "application/json",
    "User-Agent": "Krista-RestApi/2.0"
  }
)
```

## Getting Help

If you encounter issues not covered here:

1. Check the [API_REFERENCE.md](API_REFERENCE.md)
2. Review [EXAMPLES.md](EXAMPLES.md)
3. Check the [CONTRIBUTING.md](../CONTRIBUTING.md) for support channels
4. Open an issue on GitHub
5. Contact the Krista community

## See Also

- [API_REFERENCE.md](API_REFERENCE.md) - Complete API reference
- [EXAMPLES.md](EXAMPLES.md) - Usage examples
- [QUICKSTART.md](../QUICKSTART.md) - Quick start guide

