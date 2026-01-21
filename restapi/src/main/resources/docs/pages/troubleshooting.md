# Troubleshooting Guide

This guide helps you diagnose and resolve common issues when using the REST API Extension.

## Table of Contents

- [Connection Errors](#connection-errors)
- [Authentication Errors](#authentication-errors)
- [Request/Response Errors](#requestresponse-errors)
- [Data Size Errors](#data-size-errors)
- [Validation Errors](#validation-errors)
- [Performance Issues](#performance-issues)

---

## Connection Errors

### Unable to Connect to API Endpoint

**Error Message:**
```
Unable to connect to the API endpoint: https://api.example.com/endpoint

Possible causes:
• The API server may be down or unreachable
• The URL might be incorrect
• Network connectivity issues
• Firewall or proxy blocking the connection
```

**Solutions:**

1. **Verify the URL**
   - Check for typos in the API endpoint URL
   - Ensure the URL starts with `http://` or `https://`
   - Confirm the endpoint exists by testing it in a browser or Postman

2. **Check Network Connectivity**
   - Verify your internet connection is working
   - Try accessing other websites to confirm connectivity
   - Check if you're behind a corporate firewall or proxy

3. **Firewall/Proxy Settings**
   - Contact your IT administrator to whitelist the API endpoint
   - Configure proxy settings if required
   - Check if VPN is required to access the API

4. **API Server Status**
   - Check the API provider's status page for outages
   - Contact the API provider's support team
   - Try the request again after a few minutes

---

### Request Timeout

**Error Message:**
```
Request timed out while connecting to: https://api.example.com/endpoint

Possible causes:
• The API server is taking too long to respond
• Network latency or slow connection
• The request payload is too large
• Server is overloaded or experiencing issues
```

**Solutions:**

1. **Retry the Request**
   - Wait a few moments and try again
   - The server may be temporarily overloaded

2. **Reduce Payload Size**
   - If sending large data, split it into smaller requests
   - Remove unnecessary fields from the request
   - Compress data if the API supports it

3. **Use Pagination**
   - For GET requests, use pagination to retrieve data in chunks
   - Use the "Get with Pagination" request type
   - Reduce the page size parameter

4. **Check API Performance**
   - Contact your API provider about performance issues
   - Check if there are rate limits affecting your requests
   - Consider upgrading your API plan if available

---

## Authentication Errors

### No Authentication Configured

**Error Message:**
```
No authentication type selected.

Recommended actions:
1. Open the Authentication tab in the extension configuration
2. Select an authentication type (Basic, OAuth 2.0, or Token)
3. Provide the required credentials
4. Save the configuration and try again
```

**Solutions:**

1. **Configure Authentication**
   - Open the REST API Extension configuration
   - Navigate to the **Authentication** tab
   - Select the appropriate authentication type:
     - **Basic Authentication**: Username and password
     - **OAuth 2.0**: Client ID, Client Secret, Authorization URL, Token URL
     - **Token-based**: API token or bearer token

2. **Save Credentials**
   - Fill in all required fields
   - Click **Save** to store the credentials
   - Test the connection using the **Test Connection** button

3. **Verify Credentials**
   - Ensure credentials are correct and not expired
   - Check with your API provider if unsure about credentials
   - Regenerate tokens if they've expired

---

### OAuth Authorization Failed

**Error Message:**
```
OAuth authorization failed: Invalid client credentials

Recommended actions:
1. Verify your Client ID and Client Secret are correct
2. Ensure the OAuth callback URL is properly configured
3. Check that your OAuth application has the required permissions
4. Re-authorize the application through the Authentication tab
5. Contact your OAuth provider if the issue persists
```

**Solutions:**

1. **Verify OAuth Credentials**
   - Double-check Client ID and Client Secret
   - Ensure there are no extra spaces or characters
   - Regenerate credentials if necessary

2. **Configure Callback URL**
   - The callback URL must match exactly what's registered with the OAuth provider
   - Format: `https://your-krista-instance.com/rest/callback`
   - Update the callback URL in your OAuth application settings

3. **Check Permissions/Scopes**
   - Ensure your OAuth application has the required scopes
   - Request additional permissions if needed
   - Re-authorize after updating scopes

4. **Re-authorize**
   - Click the **Authorize** button in the Authentication tab
   - Complete the OAuth flow in the browser
   - Verify the refresh token is saved

---

## Request/Response Errors

### API Request Failed (4xx Errors)

**Error Message:**
```
API request failed with status 400: Bad Request

Client Error: There's an issue with the request.

Response details: {"error": "Missing required field: email"}
```

**Common Status Codes and Solutions:**

#### 400 Bad Request
- **Cause**: Invalid request format or missing required fields
- **Solution**:
  - Check the API documentation for required fields
  - Validate your JSON payload format
  - Ensure all required parameters are provided
  - Verify data types match API requirements

#### 401 Unauthorized
- **Cause**: Invalid or missing authentication credentials
- **Solution**:
  - Verify your authentication credentials are correct
  - Check if your access token has expired
  - Re-authenticate through the Authentication tab
  - Ensure the Authorization header is being sent

#### 403 Forbidden
- **Cause**: Insufficient permissions to access the resource
- **Solution**:
  - Verify you have permission to access this resource
  - Check if your API key has the required scopes
  - Contact your API administrator for access
  - Ensure your account is active and not suspended

#### 404 Not Found
- **Cause**: The requested resource doesn't exist
- **Solution**:
  - Verify the API endpoint URL is correct
  - Check if the resource ID is valid
  - Ensure the resource hasn't been deleted
  - Review the API documentation for the correct endpoint

#### 429 Too Many Requests
- **Cause**: Rate limit exceeded
- **Solution**:
  - Wait before retrying (check Retry-After header)
  - Reduce the frequency of API requests
  - Implement exponential backoff
  - Consider upgrading your API plan for higher limits

---

### API Request Failed (5xx Errors)

**Error Message:**
```
API request failed with status 500: Internal Server Error

Server Error: The API server encountered an error.
```

**Common Status Codes and Solutions:**

#### 500 Internal Server Error
- **Cause**: The API server encountered an unexpected error
- **Solution**:
  - Retry the request after a few moments
  - Check the API status page for known issues
  - Contact your API provider with request details
  - Review your request payload for any unusual data

#### 502 Bad Gateway / 503 Service Unavailable
- **Cause**: The API server is temporarily unavailable
- **Solution**:
  - Wait and retry after a few minutes
  - Check the API provider's status page
  - Contact support if the issue persists
  - Implement retry logic with exponential backoff

---

### Empty Response Body

**Error Message:**
```
The API returned an empty response (Status: 200 OK)

Possible causes:
• The API endpoint returned no data
• The requested resource may not exist
• The API may have encountered an internal error
```

**Solutions:**

1. **Verify the Endpoint**
   - Check if the API endpoint is correct
   - Test the endpoint in Postman or a browser
   - Review the API documentation

2. **Check Query Parameters**
   - Ensure filters aren't too restrictive
   - Verify the resource ID is correct
   - Remove filters to see if data is returned

3. **Review API Logs**
   - Check application logs for errors
   - Look for warnings about the request
   - Contact API provider with log details

---

## Data Size Errors

### Response Size Too Large

**Error Message:**
```
Response size (8.50 MB) exceeds the maximum allowed size (4.00 MB)

Recommended actions:
1. Use the 'Get with Pagination' request to retrieve data in smaller chunks
2. Add filters to reduce the amount of data returned
3. Request specific fields instead of all data
4. Contact your API provider about pagination support
```

**Solutions:**

1. **Use Pagination**
   - Switch to the **Get with Pagination** request type
   - Set an appropriate page size (recommended: 100-500 records)
   - Retrieve data across multiple pages
   - Process each page separately

2. **Add Filters**
   - Use query parameters to filter results
   - Request only the data you need
   - Add date ranges to limit results
   - Use field selection if supported by the API

3. **Request Specific Fields**
   - Many APIs support field selection (e.g., `?fields=id,name,email`)
   - Request only the fields you need
   - Reduce the amount of data transferred
   - Check API documentation for field selection syntax

4. **Server-Side Pagination**
   - Check if the API supports server-side pagination
   - Use pagination parameters (e.g., `?page=1&limit=100`)
   - This is more efficient than client-side pagination

---

### Paginated Results Too Large

**Error Message:**
```
The number of records (750) exceeds the limit of 500 per page.

Recommended actions:
1. Reduce the page size parameter
2. Retrieve data across multiple pages
3. Add filters to narrow down the results
4. Use a smaller page size (recommended: 100-500 records)
```

**Solutions:**

1. **Reduce Page Size**
   - Set `pageSize` to 500 or less
   - Recommended: 100-250 records per page
   - Balance between performance and number of requests

2. **Retrieve Multiple Pages**
   - Increment `pageIndex` to get subsequent pages
   - Process each page separately
   - Combine results if needed

3. **Add Filters**
   - Use filters to reduce the total number of results
   - Filter by date, status, or other criteria
   - Request only active or relevant records

---

## Validation Errors

### Invalid URL Format

**Error Message:**
```
Invalid URL format: htp://api.example.com/endpoint

Recommended actions:
1. Ensure the URL starts with http:// or https://
2. Check for typos in the URL
3. Verify the URL format matches: https://api.example.com/endpoint
4. Remove any invalid characters or spaces
```

**Solutions:**

1. **Check URL Format**
   - Must start with `http://` or `https://`
   - No spaces or special characters (except allowed in URLs)
   - Correct format: `https://api.example.com/v1/users`

2. **Common Mistakes**
   - Missing protocol: `api.example.com` → `https://api.example.com`
   - Typo in protocol: `htp://` → `http://`
   - Extra spaces: ` https://api.example.com ` → `https://api.example.com`
   - Wrong slashes: `https:\\api.example.com` → `https://api.example.com`

---

### Invalid JSON Payload

**Error Message:**
```
Invalid JSON format in request payload.

Recommended actions:
1. Validate your JSON using a JSON validator tool
2. Check for missing commas, brackets, or quotes
3. Ensure all strings are properly quoted
4. Verify the JSON structure matches the API requirements
```

**Solutions:**

1. **Validate JSON**
   - Use an online JSON validator (e.g., jsonlint.com)
   - Check for syntax errors
   - Ensure proper formatting

2. **Common JSON Errors**
   - Missing comma: `{"name": "John" "age": 30}` → `{"name": "John", "age": 30}`
   - Trailing comma: `{"name": "John",}` → `{"name": "John"}`
   - Unquoted strings: `{name: "John"}` → `{"name": "John"}`
   - Single quotes: `{'name': 'John'}` → `{"name": "John"}`

3. **Use JSON Tools**
   - Format JSON in a code editor
   - Use Krista's JSON builder if available
   - Copy from API documentation examples

---

## Performance Issues

### Slow API Responses

**Symptoms:**
- Requests take a long time to complete
- Timeouts occur frequently
- Application feels sluggish

**Solutions:**

1. **Optimize Requests**
   - Request only the data you need
   - Use field selection to reduce payload size
   - Add filters to limit results
   - Use pagination for large datasets

2. **Implement Caching**
   - Cache frequently accessed data
   - Set appropriate cache expiration times
   - Refresh cache periodically

3. **Use Async Requests**
   - Use **Async Get** or **Async Post** for long-running operations
   - Poll for results instead of waiting
   - Implement webhooks if supported

4. **Check API Performance**
   - Monitor API response times
   - Contact API provider about performance
   - Consider upgrading API plan
   - Use a CDN if available

---

## Getting Additional Help

If you're still experiencing issues after trying these solutions:

1. **Check Application Logs**
   - Review Krista application logs for detailed error messages
   - Look for stack traces or additional context
   - Note the timestamp of the error

2. **Gather Information**
   - API endpoint URL
   - Request method (GET, POST, etc.)
   - Request parameters and payload
   - Error message and status code
   - Steps to reproduce the issue

3. **Contact Support**
   - Krista Support: support@kristasoft.com
   - API Provider Support: Check their documentation
   - Include all gathered information
   - Provide screenshots if helpful

4. **Review Documentation**
   - [REST API Extension Overview](pages/overview.md)
   - [Authentication Guide](pages/authentication.md)
   - [Supported Requests](pages/supportedRequests.md)
   - API Provider's documentation

---

## Best Practices

To avoid common issues:

1. **Always Test Connections**
   - Use the **Test Connection** button after configuring authentication
   - Verify credentials before running workflows

2. **Start Small**
   - Test with small datasets first
   - Gradually increase data volume
   - Monitor performance and adjust

3. **Handle Errors Gracefully**
   - Implement error handling in your workflows
   - Log errors for troubleshooting
   - Provide user-friendly error messages

4. **Monitor Rate Limits**
   - Be aware of API rate limits
   - Implement throttling if needed
   - Use batch operations when available

5. **Keep Credentials Secure**
   - Never share API keys or tokens
   - Rotate credentials regularly
   - Use environment-specific credentials

6. **Stay Updated**
   - Keep the REST API Extension updated
   - Review release notes for bug fixes
   - Check for API changes from providers

