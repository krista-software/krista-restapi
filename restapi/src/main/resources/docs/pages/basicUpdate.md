# Basic Update Request (PUT/PATCH)

## Overview

Basic Update requests modify existing resources using HTTP PUT or PATCH methods. PUT replaces the entire resource, while PATCH applies partial updates. This operation is essential for maintaining and updating data in external systems.

## Use Cases

- Update user profiles and settings
- Modify product information
- Change resource status and properties
- Apply configuration updates
- Correct data errors and inconsistencies
- Implement resource versioning

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target resource endpoint | `https://api.example.com/users/123` |
| Request Type | PickOne | Yes | HTTP method (PUT/PATCH) | `PATCH` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Updated resource data |

## HTTP Methods

### PUT vs PATCH

#### PUT (Complete Replacement)
- Replaces the entire resource
- Requires all fields to be specified
- Idempotent operation
- Use when updating complete records

#### PATCH (Partial Update)
- Updates only specified fields
- Preserves existing data for unspecified fields
- More efficient for small changes
- Use when modifying specific properties

## Payload Configuration

### Payload Structure
| Field | Type | Description | Example |
|-------|------|-------------|---------|
| **key** | Text | Content type (defaults to "json") | `json`, `xml`, `form`, `text` |
| **value** | Text | Request body content | `{"name": "Updated Name", "status": "active"}` |
| **file** | File | Single file upload (optional) | `updated_document.pdf` |

### Content Types

#### JSON Updates
```
key: "json"
value: {
  "name": "John Doe",
  "email": "john.doe@example.com",
  "status": "active"
}
```

#### XML Updates
```
key: "xml"
value: <user><name>John Doe</name><status>active</status></user>
```

#### Form Data Updates
```
key: "form"
value: name=John+Doe&status=active&email=john%40example.com
```

## HTTP Request Format

### PATCH Request
```http
PATCH {URL} HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 45

{
  "name": "Updated Name",
  "status": "active"
}
```

### PUT Request
```http
PUT {URL} HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 120

{
  "id": 123,
  "name": "Complete User Data",
  "email": "user@example.com",
  "status": "active",
  "created_at": "2024-01-15T10:30:00Z"
}
```

## Example Usage

### Update User Profile (PATCH)

**Configuration:**
- URL: `https://api.example.com/users/12345`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Alice Johnson",
    "department": "Engineering",
    "phone": "+1-555-0123"
  }
  ```

**Generated Request:**
```http
PATCH https://api.example.com/users/12345
Content-Type: application/json

{
  "name": "Alice Johnson",
  "department": "Engineering",
  "phone": "+1-555-0123"
}
```

**Expected Response:**
```json
{
  "id": 12345,
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "department": "Engineering",
  "phone": "+1-555-0123",
  "status": "active",
  "updated_at": "2024-01-15T10:30:00Z"
}
```

### Replace Product Data (PUT)

**Configuration:**
- URL: `https://api.example.com/products/67890`
- Request Type: `PUT`
- Payload:
  ```
  key: "json"
  value: {
    "id": 67890,
    "name": "Wireless Headphones Pro",
    "description": "Premium wireless headphones with noise cancellation",
    "price": 199.99,
    "category": "electronics",
    "in_stock": true,
    "sku": "WHP-PRO-001"
  }
  ```

**Generated Request:**
```http
PUT https://api.example.com/products/67890
Content-Type: application/json

{
  "id": 67890,
  "name": "Wireless Headphones Pro",
  "description": "Premium wireless headphones with noise cancellation",
  "price": 199.99,
  "category": "electronics",
  "in_stock": true,
  "sku": "WHP-PRO-001"
}
```

### Update Document with File

**Configuration:**
- URL: `https://api.example.com/documents/456`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Updated Project Proposal",
    "status": "final"
  }
  file: updated_proposal.pdf
  ```

**Expected Response:**
```json
{
  "id": 456,
  "title": "Updated Project Proposal",
  "filename": "updated_proposal.pdf",
  "status": "final",
  "version": 2,
  "updated_at": "2024-01-15T10:30:00Z",
  "file_url": "https://storage.example.com/docs/updated_proposal_v2.pdf"
}
```

## Response Handling

### Successful Update (200 OK)
```json
{
  "id": 12345,
  "name": "Updated Resource",
  "updated_at": "2024-01-15T10:30:00Z",
  "version": 2,
  "changes": {
    "name": {
      "old": "Original Name",
      "new": "Updated Resource"
    }
  }
}
```

### No Content Response (204 No Content)
```
HTTP/1.1 204 No Content
Location: https://api.example.com/users/12345
Last-Modified: Mon, 15 Jan 2024 10:30:00 GMT
```

### Created Resource (201 Created)
```json
{
  "id": 12345,
  "name": "New Resource",
  "created_at": "2024-01-15T10:30:00Z",
  "message": "Resource created because it didn't exist"
}
```

## Error Handling

### Resource Not Found (404 Not Found)
```json
{
  "error": "Resource not found",
  "message": "User with ID 12345 does not exist",
  "code": 404,
  "resource_type": "user",
  "resource_id": "12345"
}
```

### Validation Errors (400 Bad Request)
```json
{
  "error": "Validation failed",
  "details": [
    {
      "field": "email",
      "message": "Invalid email format",
      "value": "invalid-email"
    },
    {
      "field": "age",
      "message": "Must be a positive integer",
      "value": -5
    }
  ]
}
```

### Conflict Error (409 Conflict)
```json
{
  "error": "Update conflict",
  "message": "Resource has been modified by another user",
  "current_version": 3,
  "provided_version": 2,
  "last_modified_by": "another_user",
  "last_modified_at": "2024-01-15T10:25:00Z"
}
```

### Precondition Failed (412 Precondition Failed)
```json
{
  "error": "Precondition failed",
  "message": "Resource version mismatch",
  "expected_etag": "\"abc123\"",
  "current_etag": "\"def456\""
}
```

## Best Practices

### Method Selection
- Use PATCH for partial updates to minimize data transfer
- Use PUT when replacing entire resources
- Consider idempotency requirements
- Implement proper versioning for conflict resolution

### Data Validation
- Validate all input data before processing
- Implement field-level validation rules
- Handle data type conversions properly
- Provide clear validation error messages

### Concurrency Control
- Implement optimistic locking with ETags
- Use version numbers for conflict detection
- Handle concurrent update scenarios
- Provide merge conflict resolution

### Error Handling
- Return appropriate HTTP status codes
- Provide detailed error messages
- Implement retry logic for transient failures
- Log update operations for auditing

## Versioning and Concurrency

### Optimistic Locking
```http
PATCH https://api.example.com/users/123
If-Match: "version-abc123"
Content-Type: application/json

{"name": "Updated Name"}
```

### Version-Based Updates
```json
{
  "name": "Updated Name",
  "version": 2,
  "last_modified": "2024-01-15T10:25:00Z"
}
```

### Timestamp-Based Concurrency
```json
{
  "name": "Updated Name",
  "updated_at": "2024-01-15T10:30:00Z",
  "if_unmodified_since": "2024-01-15T10:25:00Z"
}
```

## Performance Optimization

### Efficient Updates
- Send only changed fields with PATCH
- Use appropriate payload sizes
- Implement delta updates for large resources
- Consider batch updates for multiple changes

### Response Optimization
- Return only necessary data in responses
- Use appropriate caching headers
- Implement conditional requests
- Consider async updates for heavy operations

## Troubleshooting

### Update Fails
1. Verify resource exists and is accessible
2. Check payload format and required fields
3. Validate authentication and permissions
4. Confirm resource is not locked or readonly

### Validation Errors
1. Review API documentation for field requirements
2. Check data types and format constraints
3. Validate required vs. optional fields
4. Test with minimal valid payload

### Concurrency Issues
1. Implement proper version control
2. Handle conflict resolution gracefully
3. Use appropriate locking mechanisms
4. Provide clear conflict error messages

## Related Operations

- [Update with Headers](pages/updateWithHeaders.md) - Add authentication and custom headers
- [Update with Query Parameters](pages/updateWithFilters.md) - Include URL parameters
- [Basic POST Request](pages/basicPost.md) - Create new resources
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing basic updates:
1. Add authentication headers for secure endpoints
2. Implement proper error handling and validation
3. Set up versioning and concurrency control
4. Consider batch update operations for efficiency
