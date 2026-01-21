# POST with Query Parameters

## Overview

POST with Query Parameters combines resource creation with URL query parameters, enabling conditional creation, configuration options, and additional context for the operation. This pattern is useful for APIs that require both payload data and URL-based parameters.

## Use Cases

- Conditional resource creation
- Creation with configuration options
- Bulk operations with parameters
- Resource creation with validation options
- API operations requiring both body and URL parameters
- Creation with processing directives

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |
| Query Parameters | List of Multi-field | Yes | URL query parameters | `[{key: "notify", value: "true"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Created resource data |

## HTTP Request Format

```http
POST {URL}?{key1}={value1}&{key2}={value2} HTTP/1.1
Host: api.example.com
Content-Type: application/json

{Request Body}
```

## Common Query Parameter Patterns

### Creation Options

#### Notification Settings
```
[{key: "notify", value: "true"},
 {key: "notification_type", value: "email"}]
```

#### Validation Options
```
[{key: "validate", value: "strict"},
 {key: "skip_duplicates", value: "true"}]
```

#### Processing Directives
```
[{key: "async", value: "true"},
 {key: "priority", value: "high"}]
```

### Conditional Creation

#### Duplicate Handling
```
[{key: "if_exists", value: "update"},
 {key: "merge_strategy", value: "overwrite"}]
```

#### Conditional Logic
```
[{key: "create_if", value: "not_exists"},
 {key: "check_field", value: "email"}]
```

### Bulk Operations

#### Batch Processing
```
[{key: "batch_size", value: "100"},
 {key: "continue_on_error", value: "true"}]
```

#### Transaction Control
```
[{key: "transaction", value: "atomic"},
 {key: "rollback_on_failure", value: "true"}]
```

## Example Usage

### Create User with Notifications

**Configuration:**
- URL: `https://api.example.com/users`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Michael Brown",
    "email": "michael@example.com",
    "department": "Sales"
  }
  ```
- Query Parameters:
  ```
  [{key: "notify", value: "true"},
   {key: "send_welcome_email", value: "true"},
   {key: "create_calendar", value: "true"}]
  ```

**Generated Request:**
```http
POST https://api.example.com/users?notify=true&send_welcome_email=true&create_calendar=true
Content-Type: application/json

{
  "name": "Michael Brown",
  "email": "michael@example.com",
  "department": "Sales"
}
```

**Expected Response:**
```json
{
  "id": 67890,
  "name": "Michael Brown",
  "email": "michael@example.com",
  "department": "Sales",
  "created_at": "2024-01-15T10:30:00Z",
  "notifications_sent": {
    "welcome_email": "sent",
    "calendar_invite": "sent"
  }
}
```

### Conditional Product Creation

**Configuration:**
- URL: `https://api.example.com/products`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Wireless Headphones",
    "sku": "WH-001",
    "price": 99.99,
    "category": "electronics"
  }
  ```
- Query Parameters:
  ```
  [{key: "if_exists", value: "update"},
   {key: "check_field", value: "sku"},
   {key: "validate_price", value: "true"}]
  ```

**Generated Request:**
```http
POST https://api.example.com/products?if_exists=update&check_field=sku&validate_price=true
Content-Type: application/json

{
  "name": "Wireless Headphones",
  "sku": "WH-001",
  "price": 99.99,
  "category": "electronics"
}
```

### Bulk Data Import

**Configuration:**
- URL: `https://api.example.com/import/contacts`
- Payload:
  ```
  key: "json"
  value: {
    "contacts": [
      {"name": "John Doe", "email": "john@example.com"},
      {"name": "Jane Smith", "email": "jane@example.com"}
    ]
  }
  ```
- Query Parameters:
  ```
  [{key: "batch_size", value: "50"},
   {key: "continue_on_error", value: "true"},
   {key: "duplicate_action", value: "skip"}]
  ```

### File Upload with Processing Options

**Configuration:**
- URL: `https://api.example.com/documents`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Annual Report",
    "category": "financial"
  }
  file: annual_report.pdf
  ```
- Query Parameters:
  ```
  [{key: "extract_text", value: "true"},
   {key: "generate_thumbnail", value: "true"},
   {key: "ocr_enabled", value: "true"}]
  ```

## Advanced Parameter Patterns

### Workflow Control

#### Approval Workflows
```
[{key: "require_approval", value: "true"},
 {key: "approver_group", value: "managers"},
 {key: "auto_approve_threshold", value: "1000"}]
```

#### State Management
```
[{key: "initial_state", value: "draft"},
 {key: "auto_publish", value: "false"},
 {key: "schedule_publish", value: "2024-01-20T09:00:00Z"}]
```

### Integration Options

#### External System Sync
```
[{key: "sync_to_crm", value: "true"},
 {key: "sync_to_erp", value: "false"},
 {key: "webhook_notify", value: "true"}]
```

#### Data Enrichment
```
[{key: "enrich_data", value: "true"},
 {key: "geocode_address", value: "true"},
 {key: "validate_email", value: "true"}]
```

### Performance Options

#### Caching Control
```
[{key: "cache_result", value: "true"},
 {key: "cache_ttl", value: "3600"},
 {key: "invalidate_related", value: "true"}]
```

#### Processing Priority
```
[{key: "priority", value: "high"},
 {key: "queue", value: "express"},
 {key: "max_wait_time", value: "30"}]
```

## Response Handling

### Successful Creation with Parameters
```json
{
  "id": 12345,
  "name": "New Resource",
  "created_at": "2024-01-15T10:30:00Z",
  "processing_options": {
    "notifications_sent": true,
    "validation_passed": true,
    "async_processing": false
  },
  "applied_parameters": {
    "notify": "true",
    "validate": "strict"
  }
}
```

### Conditional Creation Result
```json
{
  "action": "updated",
  "id": 67890,
  "name": "Updated Resource",
  "updated_at": "2024-01-15T10:30:00Z",
  "changes": {
    "price": {
      "old": 89.99,
      "new": 99.99
    }
  },
  "reason": "Resource existed, performed update as requested"
}
```

### Batch Processing Result
```json
{
  "batch_id": "batch_abc123",
  "total_items": 100,
  "processed": 95,
  "successful": 90,
  "failed": 5,
  "skipped": 5,
  "processing_time": "2.5 seconds",
  "errors": [
    {
      "item": 23,
      "error": "Invalid email format",
      "data": {"email": "invalid-email"}
    }
  ]
}
```

## Error Handling

### Invalid Parameter Values
```json
{
  "error": "Invalid parameter value",
  "parameter": "priority",
  "value": "invalid_priority",
  "allowed_values": ["low", "normal", "high", "urgent"],
  "code": 400
}
```

### Conflicting Parameters
```json
{
  "error": "Conflicting parameters",
  "message": "Cannot use 'async=true' with 'immediate_response=true'",
  "conflicting_params": ["async", "immediate_response"],
  "code": 400
}
```

### Parameter Validation Failed
```json
{
  "error": "Parameter validation failed",
  "details": [
    {
      "parameter": "batch_size",
      "message": "Must be between 1 and 1000",
      "value": 2000
    },
    {
      "parameter": "priority",
      "message": "High priority requires premium account",
      "value": "high"
    }
  ],
  "code": 400
}
```

### Conditional Creation Failed
```json
{
  "error": "Conditional creation failed",
  "message": "Resource already exists and if_exists=fail",
  "existing_resource": {
    "id": 12345,
    "sku": "WH-001"
  },
  "code": 409
}
```

## Best Practices

### Parameter Design
- Use descriptive parameter names
- Provide clear allowed values
- Implement parameter validation
- Document parameter interactions

### Error Handling
- Validate parameters before processing
- Provide clear error messages
- Handle parameter conflicts gracefully
- Return meaningful error codes

### Performance
- Limit the number of parameters
- Use efficient parameter parsing
- Cache parameter validation results
- Optimize for common parameter combinations

### Security
- Validate parameter values
- Sanitize parameter inputs
- Implement parameter-based access control
- Log parameter usage for auditing

## Troubleshooting

### Parameters Not Working
1. Check parameter names and spelling
2. Verify parameter values are valid
3. Ensure proper URL encoding
4. Test parameters individually

### Unexpected Behavior
1. Review parameter interactions
2. Check for conflicting parameters
3. Verify parameter precedence rules
4. Test with minimal parameter set

### Performance Issues
1. Reduce number of parameters
2. Optimize parameter validation
3. Check for expensive parameter operations
4. Monitor parameter processing time

## Related Operations

- [Basic POST Request](pages/basicPost.md) - Simple resource creation
- [POST with Headers](pages/postWithHeaders.md) - Add authentication and custom headers
- [Asynchronous POST](pages/asyncPost.md) - Handle long-running operations
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing POST with query parameters:
1. Combine with custom headers for full functionality
2. Implement parameter validation and error handling
3. Add support for complex parameter interactions
4. Consider asynchronous processing for complex operations
