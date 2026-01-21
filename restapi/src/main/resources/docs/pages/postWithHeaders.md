# POST with Custom Headers

## Overview

POST with Custom Headers enables secure resource creation by including HTTP headers for authentication, content negotiation, API versioning, and custom metadata. This operation is essential for authenticated endpoints and advanced API interactions.

## Use Cases

- Create resources with authentication
- Submit data with custom content types
- Include API versioning headers
- Add tracking and correlation IDs
- Implement custom security headers
- Content negotiation for responses

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Created resource data |

## HTTP Request Format

```http
POST {URL} HTTP/1.1
Host: api.example.com
Content-Type: application/json
{Header-Name}: {Header-Value}
{Header-Name-2}: {Header-Value-2}

{Request Body}
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

### Content Headers

#### Content Type Specification
```
[{key: "Content-Type", value: "application/json"},
 {key: "Accept", value: "application/json"}]
```

#### Content Encoding
```
[{key: "Content-Type", value: "application/json"},
 {key: "Content-Encoding", value: "gzip"}]
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

### Create Authenticated User

**Configuration:**
- URL: `https://api.example.com/users`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Sarah Wilson",
    "email": "sarah@example.com",
    "department": "Marketing"
  }
  ```
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "Content-Type", value: "application/json"},
   {key: "Accept", value: "application/json"}]
  ```

**Generated Request:**
```http
POST https://api.example.com/users
Authorization: Bearer token123
Content-Type: application/json
Accept: application/json

{
  "name": "Sarah Wilson",
  "email": "sarah@example.com",
  "department": "Marketing"
}
```

**Expected Response:**
```json
{
  "id": 54321,
  "name": "Sarah Wilson",
  "email": "sarah@example.com",
  "department": "Marketing",
  "created_at": "2024-01-15T10:30:00Z",
  "created_by": "admin_user"
}
```

### Submit with Tracking Headers

**Configuration:**
- URL: `https://api.example.com/orders`
- Payload:
  ```
  key: "json"
  value: {
    "product_id": "prod_123",
    "quantity": 2,
    "customer_id": "cust_456"
  }
  ```
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "X-Request-ID", value: "req_789012"},
   {key: "X-Correlation-ID", value: "corr_345678"},
   {key: "X-Source", value: "krista-integration"}]
  ```

**Generated Request:**
```http
POST https://api.example.com/orders
Authorization: Bearer token123
X-Request-ID: req_789012
X-Correlation-ID: corr_345678
X-Source: krista-integration
Content-Type: application/json

{
  "product_id": "prod_123",
  "quantity": 2,
  "customer_id": "cust_456"
}
```

### File Upload with Metadata

**Configuration:**
- URL: `https://api.example.com/documents`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Contract Document",
    "category": "legal"
  }
  file: contract.pdf
  ```
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "X-Upload-Source", value: "web-portal"},
   {key: "X-Document-Type", value: "contract"},
   {key: "Accept", value: "application/json"}]
  ```

## Advanced Header Patterns

### Conditional Creation

#### If-None-Match for Duplicate Prevention
```
[{key: "If-None-Match", value: "*"},
 {key: "Authorization", value: "Bearer token123"}]
```

#### Custom Duplicate Check
```
[{key: "X-Duplicate-Check", value: "email"},
 {key: "X-Conflict-Action", value: "return_existing"}]
```

### Content Negotiation

#### Multiple Accept Types
```
[{key: "Accept", value: "application/json, application/xml;q=0.8"},
 {key: "Accept-Language", value: "en-US,en;q=0.9"}]
```

#### Custom Response Format
```
[{key: "Accept", value: "application/vnd.api+json"},
 {key: "X-Response-Format", value: "detailed"}]
```

### Rate Limiting and Quotas

#### Priority Headers
```
[{key: "X-Priority", value: "high"},
 {key: "X-Queue", value: "express"}]
```

#### Quota Information
```
[{key: "X-Rate-Limit-Tier", value: "premium"},
 {key: "X-Quota-Group", value: "enterprise"}]
```

## Response Header Analysis

### Important Response Headers

The Response Info will include headers such as:

#### Resource Information
- `Location`: URL of created resource
- `ETag`: Entity tag for caching
- `Last-Modified`: Creation timestamp

#### Rate Limiting
- `X-RateLimit-Remaining`: Requests remaining
- `X-RateLimit-Reset`: When limit resets
- `Retry-After`: Delay before retry

#### Tracking
- `X-Request-ID`: Request correlation ID
- `X-Response-Time`: Processing time
- `X-Server-ID`: Processing server

## Error Handling

### Authentication Errors

#### Invalid Token
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired authentication token",
  "code": 401,
  "details": {
    "token_status": "expired",
    "expires_at": "2024-01-15T09:30:00Z"
  }
}
```

#### Insufficient Permissions
```json
{
  "error": "Forbidden",
  "message": "Insufficient permissions to create resource",
  "code": 403,
  "required_permissions": ["users:create", "users:write"]
}
```

### Header Validation Errors

#### Missing Required Header
```json
{
  "error": "Bad Request",
  "message": "Missing required header",
  "code": 400,
  "details": {
    "missing_header": "X-API-Key",
    "description": "API key is required for this endpoint"
  }
}
```

#### Invalid Header Format
```json
{
  "error": "Bad Request",
  "message": "Invalid header format",
  "code": 400,
  "details": {
    "header": "Authorization",
    "expected_format": "Bearer <token>",
    "received": "Token abc123"
  }
}
```

### Content Type Errors

#### Unsupported Content Type
```json
{
  "error": "Unsupported Media Type",
  "message": "Content type not supported",
  "code": 415,
  "supported_types": ["application/json", "application/xml"]
}
```

#### Content Type Mismatch
```json
{
  "error": "Bad Request",
  "message": "Content-Type header doesn't match request body",
  "code": 400,
  "details": {
    "declared_type": "application/json",
    "detected_type": "text/plain"
  }
}
```

## Best Practices

### Security Headers
- Always use HTTPS for sensitive data
- Include authentication headers for protected endpoints
- Use correlation IDs for request tracking
- Implement proper token refresh mechanisms

### Content Headers
- Specify correct Content-Type for payload
- Include Accept headers for response format
- Use appropriate encoding headers
- Validate content types match payload

### Performance Headers
- Include caching headers when appropriate
- Use compression headers for large payloads
- Implement conditional requests
- Monitor rate limit headers

### Debugging Headers
- Include request IDs for troubleshooting
- Add source identification headers
- Use correlation IDs for distributed tracing
- Log important headers for debugging

## Troubleshooting

### Authentication Issues
1. Verify token format and validity
2. Check header name spelling and case
3. Ensure proper token permissions
4. Validate token expiration

### Content Type Problems
1. Verify Content-Type matches payload
2. Check Accept header compatibility
3. Validate character encoding
4. Test with standard content types

### Header Rejection
1. Check for forbidden headers
2. Validate header value formats
3. Ensure required headers are present
4. Review API-specific header requirements

## Related Operations

- [Basic POST Request](pages/basicPost.md) - Simple resource creation
- [POST with Query Parameters](pages/postWithFilters.md) - Include URL parameters
- [Asynchronous POST](pages/asyncPost.md) - Handle long-running operations
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing POST with headers:
1. Combine with query parameters for advanced operations
2. Implement proper error handling for auth failures
3. Set up request tracking and monitoring
4. Add retry logic with exponential backoff
