# GET with Custom Headers

## Overview

GET with Custom Headers allows you to include HTTP headers in your requests for authentication, content negotiation, API versioning, and other custom requirements. Headers provide metadata about the request and enable advanced API interactions.

## Use Cases

- API authentication (Bearer tokens, API keys)
- Content type negotiation (JSON, XML, CSV)
- API versioning
- Custom tracking and correlation IDs
- Rate limiting and quota management
- Caching control

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token123"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Retrieved data from the API |

## HTTP Request Format

```http
GET {URL} HTTP/1.1
Host: api.example.com
{Header-Name}: {Header-Value}
{Header-Name-2}: {Header-Value-2}
User-Agent: Krista-REST-Extension/2.0
```

## Common Header Types

### Authentication Headers

#### Bearer Token Authentication
```
[{key: "Authorization", value: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}]
```

#### API Key Authentication
```
[{key: "X-API-Key", value: "your-api-key-here"},
 {key: "X-API-Secret", value: "your-api-secret"}]
```

#### Basic Authentication
```
[{key: "Authorization", value: "Basic dXNlcm5hbWU6cGFzc3dvcmQ="}]
```

### Content Negotiation Headers

#### Accept Headers
```
[{key: "Accept", value: "application/json"},
 {key: "Accept-Language", value: "en-US,en;q=0.9"},
 {key: "Accept-Encoding", value: "gzip, deflate"}]
```

#### Content Type Specification
```
[{key: "Content-Type", value: "application/json"},
 {key: "Accept", value: "application/xml"}]
```

### API Versioning Headers

#### Version in Header
```
[{key: "API-Version", value: "v2"},
 {key: "Accept", value: "application/vnd.api+json;version=2"}]
```

#### Custom Version Headers
```
[{key: "X-Version", value: "2024-01-15"},
 {key: "Accept", value: "application/vnd.company.api.v2+json"}]
```

## Example Usage

### Authenticated User Retrieval

**Configuration:**
- URL: `https://api.github.com/user`
- Headers:
  ```
  [{key: "Authorization", value: "token ghp_xxxxxxxxxxxxxxxxxxxx"},
   {key: "Accept", value: "application/vnd.github.v3+json"},
   {key: "User-Agent", value: "MyApp/1.0"}]
  ```

**Generated Request:**
```http
GET https://api.github.com/user
Authorization: token ghp_xxxxxxxxxxxxxxxxxxxx
Accept: application/vnd.github.v3+json
User-Agent: MyApp/1.0
```

**Expected Response:**
```json
{
  "login": "username",
  "id": 12345,
  "name": "John Doe",
  "email": "john@example.com",
  "public_repos": 25
}
```

### API with Custom Headers

**Configuration:**
- URL: `https://api.example.com/data`
- Headers:
  ```
  [{key: "X-API-Key", value: "abc123def456"},
   {key: "X-Request-ID", value: "req-789"},
   {key: "X-Client-Version", value: "1.2.3"},
   {key: "Accept", value: "application/json"}]
  ```

### Content Type Negotiation

**Configuration:**
- URL: `https://api.example.com/export/users`
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "Accept", value: "text/csv"},
   {key: "X-Export-Format", value: "csv"}]
  ```

**Expected Response:** CSV data instead of JSON

## Advanced Header Patterns

### Conditional Requests

#### If-Modified-Since
```
[{key: "If-Modified-Since", value: "Wed, 21 Oct 2024 07:28:00 GMT"}]
```

#### ETag Validation
```
[{key: "If-None-Match", value: "\"686897696a7c876b7e\""}]
```

### Custom Tracking Headers

#### Request Correlation
```
[{key: "X-Correlation-ID", value: "550e8400-e29b-41d4-a716-446655440000"},
 {key: "X-Request-Source", value: "krista-integration"},
 {key: "X-User-ID", value: "user123"}]
```

### Rate Limiting Headers

#### Rate Limit Information
```
[{key: "X-RateLimit-Limit", value: "1000"},
 {key: "X-RateLimit-Remaining", value: "999"}]
```

## Response Header Analysis

### Important Response Headers

The Response Info will include headers such as:

#### Rate Limiting
- `X-RateLimit-Limit`: Maximum requests allowed
- `X-RateLimit-Remaining`: Requests remaining
- `X-RateLimit-Reset`: When limit resets

#### Caching
- `Cache-Control`: Caching directives
- `ETag`: Entity tag for caching
- `Last-Modified`: Last modification date

#### Content Information
- `Content-Type`: Response content type
- `Content-Length`: Response size
- `Content-Encoding`: Compression used

## Error Handling

### Authentication Errors

#### Invalid Token
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "code": 401
}
```

#### Missing API Key
```json
{
  "error": "Forbidden",
  "message": "API key required",
  "code": 403
}
```

### Header Validation Errors

#### Invalid Header Format
```json
{
  "error": "Bad Request",
  "message": "Invalid Authorization header format",
  "code": 400
}
```

#### Unsupported Content Type
```json
{
  "error": "Not Acceptable",
  "message": "Requested content type not supported",
  "code": 406
}
```

## Best Practices

### Security
- Never log sensitive headers (Authorization, API keys)
- Use HTTPS for all authenticated requests
- Implement proper token refresh mechanisms
- Validate header values before sending

### Performance
- Include only necessary headers
- Use appropriate caching headers
- Implement conditional requests when possible
- Monitor rate limit headers

### Compatibility
- Follow standard HTTP header conventions
- Use vendor-specific prefixes for custom headers (X-)
- Include User-Agent for API tracking
- Respect API versioning requirements

## Troubleshooting

### Authentication Issues
1. Verify token/key format and validity
2. Check header name spelling and case
3. Ensure proper encoding of header values
4. Validate token permissions and scopes

### Content Negotiation Problems
1. Check Accept header format
2. Verify API supports requested content type
3. Review API documentation for supported formats
4. Test with default content types first

### Header Rejection
1. Validate header names follow HTTP standards
2. Check for forbidden or restricted headers
3. Verify header value encoding
4. Review API-specific header requirements

## Related Operations

- [Basic GET Request](pages/basicGet.md) - Simple data retrieval
- [GET with Query Parameters](pages/getWithFilters.md) - Add filtering and search
- [Authentication Setup](pages/authentication.md) - Configure API authentication
- [GET with Pagination](pages/getWithPagination.md) - Handle large datasets

## Next Steps

After implementing custom headers:
1. Combine with query parameters for advanced filtering
2. Implement proper error handling for auth failures
3. Set up token refresh mechanisms
4. Monitor rate limits and implement backoff strategies
