# DELETE with Query Parameters

## Overview

DELETE with Query Parameters enables conditional and bulk deletion operations by including URL query parameters. This operation is essential for selective data cleanup, batch operations, and implementing complex deletion logic.

## Use Cases

- Conditional resource deletion based on criteria
- Bulk deletion of multiple resources
- Cleanup operations with filters
- Scheduled deletion with parameters
- Deletion with safety checks and confirmations
- Data retention policy implementation

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Query Parameters | List of Multi-field | Yes | URL query parameters | `[{key: "status", value: "inactive"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Deletion results and confirmation |

## HTTP Request Format

```http
DELETE {URL}?{key1}={value1}&{key2}={value2} HTTP/1.1
Host: api.example.com
User-Agent: Krista-REST-Extension/2.0
```

## Common Query Parameter Patterns

### Filtering Parameters

#### Status-Based Deletion
```
[{key: "status", value: "inactive"},
 {key: "last_login", value: "before:2023-01-01"}]
```

#### Date Range Deletion
```
[{key: "created_before", value: "2023-01-01"},
 {key: "created_after", value: "2022-01-01"}]
```

#### Category-Based Deletion
```
[{key: "category", value: "temporary"},
 {key: "type", value: "test_data"}]
```

### Safety Parameters

#### Confirmation and Limits
```
[{key: "confirm", value: "true"},
 {key: "limit", value: "100"}]
```

#### Dry Run Mode
```
[{key: "dry_run", value: "true"},
 {key: "preview", value: "true"}]
```

#### Force Deletion
```
[{key: "force", value: "true"},
 {key: "ignore_dependencies", value: "true"}]
```

### Processing Options

#### Batch Control
```
[{key: "batch_size", value: "50"},
 {key: "continue_on_error", value: "true"}]
```

#### Backup Options
```
[{key: "create_backup", value: "true"},
 {key: "backup_location", value: "archive"}]
```

## Example Usage

### Delete Inactive Users

**Configuration:**
- URL: `https://api.example.com/users`
- Query Parameters:
  ```
  [{key: "status", value: "inactive"},
   {key: "last_login_before", value: "2023-06-01"},
   {key: "confirm", value: "true"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/users?status=inactive&last_login_before=2023-06-01&confirm=true
```

**Expected Response:**
```json
{
  "message": "Bulk deletion completed",
  "deleted_count": 25,
  "criteria": {
    "status": "inactive",
    "last_login_before": "2023-06-01"
  },
  "deleted_users": [
    {"id": 101, "email": "user1@example.com"},
    {"id": 102, "email": "user2@example.com"}
  ],
  "backup_created": true,
  "backup_id": "backup_users_20240115"
}
```

### Clean Up Temporary Files

**Configuration:**
- URL: `https://api.example.com/files`
- Query Parameters:
  ```
  [{key: "type", value: "temporary"},
   {key: "created_before", value: "2024-01-01"},
   {key: "size_limit", value: "1000"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/files?type=temporary&created_before=2024-01-01&size_limit=1000
```

**Expected Response:**
```json
{
  "message": "Temporary files cleanup completed",
  "files_deleted": 150,
  "space_freed": "2.5 GB",
  "criteria": {
    "type": "temporary",
    "created_before": "2024-01-01",
    "max_files": 1000
  },
  "cleanup_summary": {
    "total_scanned": 200,
    "eligible_for_deletion": 150,
    "protected_files": 50
  }
}
```

### Conditional Product Removal

**Configuration:**
- URL: `https://api.example.com/products`
- Query Parameters:
  ```
  [{key: "status", value: "discontinued"},
   {key: "inventory", value: "0"},
   {key: "dry_run", value: "false"},
   {key: "notify_suppliers", value: "true"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/products?status=discontinued&inventory=0&dry_run=false&notify_suppliers=true
```

### Bulk Order Cleanup

**Configuration:**
- URL: `https://api.example.com/orders`
- Query Parameters:
  ```
  [{key: "status", value: "cancelled"},
   {key: "created_before", value: "2023-12-01"},
   {key: "batch_size", value: "100"},
   {key: "create_backup", value: "true"}]
  ```

## Advanced Parameter Patterns

### Complex Filtering

#### Multiple Conditions
```
[{key: "status", value: "inactive"},
 {key: "department", value: "temp"},
 {key: "role", value: "contractor"},
 {key: "contract_ended", value: "true"}]
```

#### Range-Based Deletion
```
[{key: "age_min", value: "365"},
 {key: "age_max", value: "1095"},
 {key: "size_min", value: "0"},
 {key: "size_max", value: "1024"}]
```

### Safety and Validation

#### Confirmation Requirements
```
[{key: "confirm_deletion", value: "DELETE_CONFIRMED"},
 {key: "admin_approval", value: "approved"},
 {key: "safety_check", value: "passed"}]
```

#### Limit and Throttling
```
[{key: "max_delete", value: "1000"},
 {key: "rate_limit", value: "10_per_second"},
 {key: "timeout", value: "300"}]
```

### Processing Control

#### Error Handling
```
[{key: "continue_on_error", value: "true"},
 {key: "max_errors", value: "10"},
 {key: "stop_on_critical", value: "true"}]
```

#### Progress Tracking
```
[{key: "progress_callback", value: "true"},
 {key: "status_updates", value: "every_100"},
 {key: "completion_webhook", value: "https://webhook.example.com"}]
```

## Response Handling

### Successful Bulk Deletion
```json
{
  "operation_id": "bulk_delete_abc123",
  "status": "completed",
  "summary": {
    "total_candidates": 500,
    "deleted": 450,
    "skipped": 30,
    "failed": 20
  },
  "criteria_applied": {
    "status": "inactive",
    "last_activity": "before:2023-01-01"
  },
  "processing_time": "45 seconds",
  "backup_location": "https://backup.example.com/bulk_delete_abc123"
}
```

### Dry Run Results
```json
{
  "dry_run": true,
  "message": "Preview of deletion operation",
  "would_delete": 75,
  "candidates": [
    {
      "id": 101,
      "name": "Test User 1",
      "reason": "Matches status=inactive"
    },
    {
      "id": 102,
      "name": "Test User 2", 
      "reason": "Matches last_login criteria"
    }
  ],
  "estimated_time": "30 seconds",
  "warnings": [
    "User 105 has active sessions",
    "User 110 has pending orders"
  ]
}
```

### Partial Success Response
```json
{
  "status": "partially_completed",
  "deleted": 80,
  "failed": 20,
  "total_attempted": 100,
  "successful_deletions": [
    {"id": 101, "status": "deleted"},
    {"id": 102, "status": "deleted"}
  ],
  "failed_deletions": [
    {
      "id": 201,
      "error": "Has active dependencies",
      "dependencies": ["orders", "subscriptions"]
    },
    {
      "id": 202,
      "error": "Permission denied",
      "reason": "Protected user account"
    }
  ]
}
```

## Error Handling

### Invalid Filter Criteria
```json
{
  "error": "Invalid filter criteria",
  "parameter": "last_login_before",
  "value": "invalid-date",
  "expected_format": "YYYY-MM-DD",
  "code": 400
}
```

### Safety Check Failed
```json
{
  "error": "Safety check failed",
  "message": "Deletion would affect too many resources",
  "affected_count": 5000,
  "safety_limit": 1000,
  "suggestion": "Use more specific criteria or increase limit",
  "code": 412
}
```

### Confirmation Required
```json
{
  "error": "Confirmation required",
  "message": "Bulk deletion requires explicit confirmation",
  "affected_resources": 250,
  "required_parameter": "confirm=true",
  "preview_url": "https://api.example.com/preview/delete?...",
  "code": 428
}
```

### Dependency Conflicts
```json
{
  "error": "Dependency conflicts detected",
  "message": "Some resources cannot be deleted due to dependencies",
  "conflicts": [
    {
      "resource_id": 123,
      "dependencies": ["active_orders", "subscriptions"],
      "dependency_count": 5
    }
  ],
  "options": [
    "Use force=true to ignore dependencies",
    "Delete dependencies first",
    "Use cascade=true for automatic cleanup"
  ],
  "code": 409
}
```

## Best Practices

### Safety First
- Always implement dry run functionality
- Require explicit confirmation for bulk operations
- Set reasonable limits on deletion counts
- Create backups before permanent deletion

### Parameter Validation
- Validate all filter parameters before processing
- Provide clear error messages for invalid criteria
- Implement parameter sanitization
- Support parameter combination validation

### Performance Optimization
- Use efficient filtering queries
- Implement batch processing for large operations
- Provide progress feedback for long operations
- Optimize database queries and indexes

### Error Recovery
- Implement proper transaction handling
- Provide detailed error reporting
- Support partial operation recovery
- Log all deletion operations for audit

## Troubleshooting

### No Resources Match Criteria
1. Verify filter parameters are correct
2. Check if resources exist with different criteria
3. Use dry run to preview matching resources
4. Review parameter syntax and values

### Deletion Limits Exceeded
1. Reduce the scope of filter criteria
2. Increase deletion limits if appropriate
3. Process deletions in smaller batches
4. Use pagination for large datasets

### Performance Issues
1. Optimize filter criteria for database efficiency
2. Reduce batch sizes for better performance
3. Check for database locks or conflicts
4. Monitor system resources during operation

### Permission Errors
1. Verify authentication and authorization
2. Check resource-level permissions
3. Confirm bulk operation permissions
4. Review role-based access controls

## Related Operations

- [Basic DELETE Request](pages/basicDelete.md) - Simple resource deletion
- [DELETE with Headers](pages/deleteWithHeaders.md) - Authenticated deletion
- [GET with Query Parameters](pages/getWithFilters.md) - Preview resources before deletion
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing DELETE with query parameters:
1. Add authentication headers for secure operations
2. Implement comprehensive safety checks and confirmations
3. Set up proper backup and recovery mechanisms
4. Consider asynchronous processing for large bulk operations
