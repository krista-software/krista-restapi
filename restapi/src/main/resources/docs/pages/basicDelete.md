# Basic DELETE Request

## Overview

The Basic DELETE request removes resources from external systems using HTTP DELETE methods. This operation is essential for data cleanup, resource management, and maintaining data integrity in external APIs.

## Use Cases

- Remove user accounts and profiles
- Delete obsolete records and data
- Clean up temporary resources
- Remove expired or invalid entries
- Implement data retention policies
- Manage resource lifecycle

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target resource endpoint | `https://api.example.com/users/123` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Deletion confirmation or error details |

## HTTP Request Format

```http
DELETE {URL} HTTP/1.1
Host: api.example.com
User-Agent: Krista-REST-Extension/2.0
```

## Example Usage

### Delete User Account

**Configuration:**
- URL: `https://api.example.com/users/12345`

**Generated Request:**
```http
DELETE https://api.example.com/users/12345
```

**Expected Response:**
```json
{
  "message": "User successfully deleted",
  "deleted_resource": {
    "id": 12345,
    "name": "John Doe",
    "email": "john@example.com"
  },
  "deleted_at": "2024-01-15T10:30:00Z"
}
```

### Remove Product from Catalog

**Configuration:**
- URL: `https://api.example.com/products/67890`

**Generated Request:**
```http
DELETE https://api.example.com/products/67890
```

**Expected Response:**
```json
{
  "message": "Product removed from catalog",
  "product_id": 67890,
  "sku": "PROD-67890",
  "removal_reason": "Discontinued",
  "deleted_at": "2024-01-15T10:30:00Z"
}
```

### Delete Document

**Configuration:**
- URL: `https://api.example.com/documents/456`

**Generated Request:**
```http
DELETE https://api.example.com/documents/456
```

**Expected Response:**
```json
{
  "message": "Document deleted successfully",
  "document": {
    "id": 456,
    "title": "Old Report",
    "filename": "report_2023.pdf"
  },
  "backup_location": "https://backup.example.com/docs/456",
  "deleted_at": "2024-01-15T10:30:00Z"
}
```

## Response Handling

### Successful Deletion (200 OK)
```json
{
  "message": "Resource deleted successfully",
  "deleted_resource": {
    "id": 123,
    "type": "user",
    "identifier": "john@example.com"
  },
  "deleted_at": "2024-01-15T10:30:00Z",
  "backup_available": true
}
```

### No Content Response (204 No Content)
```
HTTP/1.1 204 No Content
Date: Mon, 15 Jan 2024 10:30:00 GMT
Server: nginx/1.18.0
```

### Accepted for Processing (202 Accepted)
```json
{
  "message": "Deletion request accepted",
  "deletion_id": "del_abc123",
  "status": "processing",
  "estimated_completion": "2024-01-15T10:35:00Z",
  "status_url": "https://api.example.com/deletions/del_abc123"
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

### Forbidden Deletion (403 Forbidden)
```json
{
  "error": "Deletion forbidden",
  "message": "Cannot delete resource due to existing dependencies",
  "code": 403,
  "dependencies": [
    {
      "type": "orders",
      "count": 5,
      "message": "User has active orders"
    },
    {
      "type": "subscriptions",
      "count": 2,
      "message": "User has active subscriptions"
    }
  ]
}
```

### Method Not Allowed (405 Method Not Allowed)
```json
{
  "error": "Method not allowed",
  "message": "DELETE operation not supported for this resource",
  "code": 405,
  "allowed_methods": ["GET", "PUT", "PATCH"],
  "resource_type": "system_configuration"
}
```

### Conflict Error (409 Conflict)
```json
{
  "error": "Deletion conflict",
  "message": "Resource is currently being used and cannot be deleted",
  "code": 409,
  "details": {
    "resource_status": "in_use",
    "active_sessions": 3,
    "last_accessed": "2024-01-15T10:25:00Z"
  }
}
```

### Precondition Failed (412 Precondition Failed)
```json
{
  "error": "Precondition failed",
  "message": "Resource version mismatch",
  "code": 412,
  "expected_version": "3",
  "current_version": "4",
  "last_modified": "2024-01-15T10:20:00Z"
}
```

## Deletion Patterns

### Soft Delete
Some APIs implement soft deletion where resources are marked as deleted but not physically removed:

**Response:**
```json
{
  "message": "Resource marked for deletion",
  "resource_id": 123,
  "status": "deleted",
  "soft_delete": true,
  "recovery_period": "30 days",
  "permanent_deletion_date": "2024-02-15T10:30:00Z"
}
```

### Hard Delete
Physical removal of the resource from the system:

**Response:**
```json
{
  "message": "Resource permanently deleted",
  "resource_id": 123,
  "hard_delete": true,
  "backup_created": true,
  "recovery_possible": false
}
```

### Cascade Delete
Deletion that removes related resources:

**Response:**
```json
{
  "message": "Resource and dependencies deleted",
  "primary_resource": {
    "id": 123,
    "type": "user"
  },
  "cascade_deleted": [
    {"type": "profile", "id": 456},
    {"type": "preferences", "id": 789},
    {"type": "sessions", "count": 3}
  ],
  "total_deleted": 6
}
```

## Best Practices

### Safety Measures
- Implement confirmation mechanisms for critical deletions
- Use soft delete for recoverable operations
- Create backups before permanent deletion
- Validate dependencies before deletion

### Error Handling
- Check for resource existence before deletion
- Handle dependency conflicts gracefully
- Provide clear error messages
- Implement proper status codes

### Security
- Validate permissions before deletion
- Log all deletion operations for audit
- Implement rate limiting for bulk deletions
- Use authentication for protected resources

### Performance
- Optimize deletion queries for large datasets
- Implement asynchronous deletion for heavy operations
- Use batch deletion for multiple resources
- Monitor deletion performance and impact

## Deletion Strategies

### Immediate Deletion
Resource is removed immediately upon request:
```
DELETE /users/123 → Resource deleted instantly
```

### Scheduled Deletion
Resource is marked for deletion and removed later:
```
DELETE /users/123?schedule=true → Marked for deletion
Background process → Actual deletion
```

### Conditional Deletion
Deletion only occurs if certain conditions are met:
```
DELETE /users/123?force=false → Check dependencies first
If no dependencies → Delete
If dependencies exist → Return error
```

## Recovery and Backup

### Backup Before Deletion
```json
{
  "message": "Resource deleted with backup",
  "backup_id": "backup_abc123",
  "backup_location": "https://backup.example.com/users/123",
  "retention_period": "90 days",
  "recovery_instructions": "Contact support with backup ID"
}
```

### Recovery Options
```json
{
  "deletion_id": "del_123",
  "recovery_available": true,
  "recovery_deadline": "2024-01-22T10:30:00Z",
  "recovery_url": "https://api.example.com/recovery/del_123",
  "recovery_cost": "none"
}
```

## Troubleshooting

### Deletion Fails
1. Verify resource exists and is accessible
2. Check for dependencies that prevent deletion
3. Confirm proper authentication and permissions
4. Validate resource is not locked or protected

### Permission Denied
1. Verify user has delete permissions
2. Check resource ownership and access rights
3. Confirm authentication token is valid
4. Review role-based access controls

### Dependency Conflicts
1. Identify and resolve dependent resources
2. Consider cascade deletion options
3. Implement proper cleanup procedures
4. Use soft delete for complex dependencies

### Performance Issues
1. Optimize deletion queries and operations
2. Consider asynchronous deletion for large resources
3. Implement batch operations for multiple deletions
4. Monitor database and system performance

## Related Operations

- [DELETE with Query Parameters](pages/deleteWithFilters.md) - Conditional deletion
- [DELETE with Headers](pages/deleteWithHeaders.md) - Authenticated deletion
- [Basic GET Request](pages/basicGet.md) - Verify resource before deletion
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing basic DELETE operations:
1. Add authentication headers for secure endpoints
2. Implement proper error handling and validation
3. Set up dependency checking and cascade deletion
4. Consider soft delete options for data recovery
