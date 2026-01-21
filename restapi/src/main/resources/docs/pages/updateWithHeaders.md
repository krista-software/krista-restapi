# Update with Custom Headers

## Overview

Update with Custom Headers enables secure resource modification by including HTTP headers for authentication, versioning, concurrency control, and custom metadata. This operation is essential for authenticated endpoints and advanced update scenarios.

## Use Cases

- Authenticated resource updates
- Version-controlled modifications
- Conditional updates with ETags
- Custom tracking and audit trails
- Content negotiation for responses
- Concurrency control and conflict resolution

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target resource endpoint | `https://api.example.com/users/123` |
| Request Type | PickOne | Yes | HTTP method (PUT/PATCH) | `PATCH` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Updated resource data |

## HTTP Request Format

```http
PATCH {URL} HTTP/1.1
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

### Concurrency Control Headers

#### ETag-Based Optimistic Locking
```
[{key: "If-Match", value: "\"version-abc123\""},
 {key: "Authorization", value: "Bearer token"}]
```

#### Timestamp-Based Concurrency
```
[{key: "If-Unmodified-Since", value: "Mon, 15 Jan 2024 10:25:00 GMT"},
 {key: "Authorization", value: "Bearer token"}]
```

### Content Negotiation Headers

#### Response Format Control
```
[{key: "Accept", value: "application/json"},
 {key: "Content-Type", value: "application/json"}]
```

#### Compression and Encoding
```
[{key: "Accept-Encoding", value: "gzip, deflate"},
 {key: "Content-Encoding", value: "gzip"}]
```

### Tracking and Audit Headers

#### Request Tracking
```
[{key: "X-Request-ID", value: "req-12345"},
 {key: "X-Correlation-ID", value: "corr-67890"},
 {key: "X-User-ID", value: "user-456"}]
```

#### Source Identification
```
[{key: "X-Source", value: "krista-integration"},
 {key: "X-Client-Version", value: "2.0.8"},
 {key: "User-Agent", value: "Krista-REST-Extension/2.0"}]
```

## Example Usage

### Authenticated User Update

**Configuration:**
- URL: `https://api.example.com/users/12345`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Robert Johnson",
    "department": "Engineering",
    "title": "Senior Developer"
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
PATCH https://api.example.com/users/12345
Authorization: Bearer token123
Content-Type: application/json
Accept: application/json

{
  "name": "Robert Johnson",
  "department": "Engineering",
  "title": "Senior Developer"
}
```

**Expected Response:**
```json
{
  "id": 12345,
  "name": "Robert Johnson",
  "department": "Engineering",
  "title": "Senior Developer",
  "updated_at": "2024-01-15T10:30:00Z",
  "updated_by": "authenticated_user"
}
```

### Version-Controlled Update

**Configuration:**
- URL: `https://api.example.com/documents/789`
- Request Type: `PUT`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Updated Document Title",
    "content": "Updated document content...",
    "status": "published"
  }
  ```
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "If-Match", value: "\"version-abc123\""},
   {key: "X-Update-Reason", value: "Content revision"}]
  ```

**Generated Request:**
```http
PUT https://api.example.com/documents/789
Authorization: Bearer token123
If-Match: "version-abc123"
X-Update-Reason: Content revision
Content-Type: application/json

{
  "title": "Updated Document Title",
  "content": "Updated document content...",
  "status": "published"
}
```

### Conditional Update with Tracking

**Configuration:**
- URL: `https://api.example.com/products/456`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "price": 299.99,
    "discount": 10
  }
  ```
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "If-Unmodified-Since", value: "Mon, 15 Jan 2024 09:00:00 GMT"},
   {key: "X-Request-ID", value: "price-update-001"},
   {key: "X-Reason", value: "Promotional pricing"}]
  ```

## Advanced Header Patterns

### Batch Update Control

#### Transaction Headers
```
[{key: "X-Transaction-ID", value: "txn-12345"},
 {key: "X-Atomic-Update", value: "true"},
 {key: "X-Rollback-On-Error", value: "true"}]
```

#### Batch Processing
```
[{key: "X-Batch-ID", value: "batch-67890"},
 {key: "X-Batch-Size", value: "100"},
 {key: "X-Continue-On-Error", value: "false"}]
```

### Custom Validation Headers

#### Validation Control
```
[{key: "X-Validation-Level", value: "strict"},
 {key: "X-Skip-Business-Rules", value: "false"},
 {key: "X-Validate-References", value: "true"}]
```

#### Data Quality Headers
```
[{key: "X-Data-Quality-Check", value: "enabled"},
 {key: "X-Duplicate-Check", value: "email,phone"},
 {key: "X-Format-Validation", value: "strict"}]
```

### Workflow Control Headers

#### Approval Workflow
```
[{key: "X-Require-Approval", value: "true"},
 {key: "X-Approver-Group", value: "managers"},
 {key: "X-Auto-Approve", value: "false"}]
```

#### State Management
```
[{key: "X-State-Transition", value: "draft_to_published"},
 {key: "X-Validate-Transition", value: "true"},
 {key: "X-Notify-Stakeholders", value: "true"}]
```

## Response Header Analysis

### Important Response Headers

#### Update Confirmation
- `ETag`: New version identifier
- `Last-Modified`: Update timestamp
- `Location`: Resource location (for PUT)

#### Concurrency Information
- `X-Version`: Resource version number
- `X-Lock-Status`: Resource lock information
- `X-Conflict-Resolution`: How conflicts were handled

#### Processing Information
- `X-Processing-Time`: Update duration
- `X-Validation-Status`: Validation results
- `X-Changes-Applied`: Number of changes made

## Error Handling

### Authentication Errors

#### Invalid Token
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired authentication token",
  "code": 401,
  "token_status": "expired",
  "expires_at": "2024-01-15T09:30:00Z"
}
```

#### Insufficient Permissions
```json
{
  "error": "Forbidden",
  "message": "Insufficient permissions to update resource",
  "code": 403,
  "required_permissions": ["users:update", "users:write"],
  "user_permissions": ["users:read"]
}
```

### Concurrency Control Errors

#### Version Conflict
```json
{
  "error": "Precondition Failed",
  "message": "Resource version mismatch",
  "code": 412,
  "expected_etag": "\"version-abc123\"",
  "current_etag": "\"version-def456\"",
  "last_modified_by": "another_user"
}
```

#### Resource Modified
```json
{
  "error": "Precondition Failed",
  "message": "Resource has been modified since specified time",
  "code": 412,
  "if_unmodified_since": "2024-01-15T09:00:00Z",
  "last_modified": "2024-01-15T09:30:00Z"
}
```

### Header Validation Errors

#### Missing Required Header
```json
{
  "error": "Bad Request",
  "message": "Missing required header",
  "code": 400,
  "missing_header": "If-Match",
  "description": "Version control header required for this resource"
}
```

#### Invalid Header Format
```json
{
  "error": "Bad Request",
  "message": "Invalid header format",
  "code": 400,
  "header": "If-Match",
  "expected_format": "\"version-string\"",
  "received": "version-string"
}
```

## Best Practices

### Security Headers
- Always include authentication for protected resources
- Use HTTPS for all authenticated requests
- Implement proper token validation and refresh
- Include audit trail headers for compliance

### Concurrency Control
- Use ETags for optimistic locking
- Implement proper conflict resolution
- Handle version mismatches gracefully
- Provide clear error messages for conflicts

### Performance Headers
- Include appropriate caching headers
- Use compression for large payloads
- Implement conditional updates
- Monitor processing time headers

### Debugging Headers
- Include request IDs for troubleshooting
- Add correlation IDs for distributed tracing
- Use source identification headers
- Log important headers for debugging

## Troubleshooting

### Authentication Issues
1. Verify token format and validity
2. Check header name spelling and case
3. Ensure proper token permissions
4. Validate token expiration and refresh

### Concurrency Problems
1. Check ETag or version header format
2. Verify resource hasn't been modified
3. Implement proper conflict resolution
4. Handle concurrent update scenarios

### Header Rejection
1. Validate header names and formats
2. Check for forbidden or restricted headers
3. Ensure required headers are present
4. Review API-specific header requirements

## Related Operations

- [Basic Update Request](pages/basicUpdate.md) - Simple resource modification
- [Update with Query Parameters](pages/updateWithFilters.md) - Include URL parameters
- [Basic POST Request](pages/basicPost.md) - Create new resources
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing updates with headers:
1. Combine with query parameters for advanced operations
2. Implement proper concurrency control mechanisms
3. Set up comprehensive audit logging
4. Add automated conflict resolution strategies
