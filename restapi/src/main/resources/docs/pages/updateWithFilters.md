# Update with Query Parameters

## Overview

Update with Query Parameters combines resource modification with URL query parameters, enabling conditional updates, bulk operations, and configuration options. This pattern is useful for APIs that require both payload data and URL-based parameters for update operations.

## Use Cases

- Conditional resource updates
- Bulk update operations
- Update with validation options
- Partial updates with filters
- Update operations with processing directives
- Resource updates with workflow control

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target resource endpoint | `https://api.example.com/users/123` |
| Request Type | PickOne | Yes | HTTP method (PUT/PATCH) | `PATCH` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |
| Query Parameters | List of Multi-field | Yes | URL query parameters | `[{key: "validate", value: "strict"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Updated resource data |

## HTTP Request Format

```http
PATCH {URL}?{key1}={value1}&{key2}={value2} HTTP/1.1
Host: api.example.com
Content-Type: application/json

{Request Body}
```

## Common Query Parameter Patterns

### Update Control Options

#### Validation Settings
```
[{key: "validate", value: "strict"},
 {key: "skip_business_rules", value: "false"}]
```

#### Processing Directives
```
[{key: "async", value: "true"},
 {key: "priority", value: "high"}]
```

#### Notification Control
```
[{key: "notify", value: "true"},
 {key: "notification_type", value: "email"}]
```

### Conditional Update Parameters

#### Existence Checks
```
[{key: "if_exists", value: "update"},
 {key: "create_if_missing", value: "false"}]
```

#### Field-Based Conditions
```
[{key: "update_if", value: "status=active"},
 {key: "condition_field", value: "last_login"}]
```

#### Version Control
```
[{key: "expected_version", value: "2"},
 {key: "force_update", value: "false"}]
```

### Bulk Update Parameters

#### Batch Processing
```
[{key: "batch_update", value: "true"},
 {key: "batch_size", value: "100"}]
```

#### Error Handling
```
[{key: "continue_on_error", value: "true"},
 {key: "rollback_on_failure", value: "false"}]
```

## Example Usage

### User Profile Update with Validation

**Configuration:**
- URL: `https://api.example.com/users/12345`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "email": "newemail@example.com",
    "phone": "+1-555-0199",
    "department": "Marketing"
  }
  ```
- Query Parameters:
  ```
  [{key: "validate", value: "strict"},
   {key: "check_duplicates", value: "true"},
   {key: "notify_user", value: "true"}]
  ```

**Generated Request:**
```http
PATCH https://api.example.com/users/12345?validate=strict&check_duplicates=true&notify_user=true
Content-Type: application/json

{
  "email": "newemail@example.com",
  "phone": "+1-555-0199",
  "department": "Marketing"
}
```

**Expected Response:**
```json
{
  "id": 12345,
  "email": "newemail@example.com",
  "phone": "+1-555-0199",
  "department": "Marketing",
  "updated_at": "2024-01-15T10:30:00Z",
  "validation_status": "passed",
  "notification_sent": true
}
```

### Conditional Product Update

**Configuration:**
- URL: `https://api.example.com/products/67890`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "price": 199.99,
    "discount": 15,
    "promotion_end": "2024-02-15T23:59:59Z"
  }
  ```
- Query Parameters:
  ```
  [{key: "update_if", value: "status=active"},
   {key: "price_change_limit", value: "20"},
   {key: "require_approval", value: "false"}]
  ```

**Generated Request:**
```http
PATCH https://api.example.com/products/67890?update_if=status=active&price_change_limit=20&require_approval=false
Content-Type: application/json

{
  "price": 199.99,
  "discount": 15,
  "promotion_end": "2024-02-15T23:59:59Z"
}
```

### Bulk Status Update

**Configuration:**
- URL: `https://api.example.com/orders/bulk-update`
- Request Type: `PATCH`
- Payload:
  ```
  key: "json"
  value: {
    "status": "shipped",
    "tracking_number": "TRK123456789",
    "estimated_delivery": "2024-01-20"
  }
  ```
- Query Parameters:
  ```
  [{key: "order_ids", value: "1001,1002,1003,1004"},
   {key: "batch_size", value: "10"},
   {key: "notify_customers", value: "true"}]
  ```

### Document Update with Processing

**Configuration:**
- URL: `https://api.example.com/documents/456`
- Request Type: `PUT`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Updated Contract",
    "content": "Updated contract content...",
    "status": "final"
  }
  file: updated_contract.pdf
  ```
- Query Parameters:
  ```
  [{key: "extract_text", value: "true"},
   {key: "generate_preview", value: "true"},
   {key: "version_increment", value: "minor"}]
  ```

## Advanced Parameter Patterns

### Workflow Control

#### Approval Workflows
```
[{key: "require_approval", value: "true"},
 {key: "approver_role", value: "manager"},
 {key: "auto_approve_threshold", value: "100"}]
```

#### State Transitions
```
[{key: "transition_to", value: "published"},
 {key: "validate_transition", value: "true"},
 {key: "skip_workflow", value: "false"}]
```

### Data Processing Options

#### Validation Rules
```
[{key: "validation_level", value: "strict"},
 {key: "custom_validators", value: "email,phone"},
 {key: "skip_readonly_fields", value: "true"}]
```

#### Data Enrichment
```
[{key: "enrich_data", value: "true"},
 {key: "geocode_address", value: "true"},
 {key: "normalize_phone", value: "true"}]
```

### Integration Parameters

#### External System Sync
```
[{key: "sync_to_crm", value: "true"},
 {key: "sync_to_warehouse", value: "false"},
 {key: "webhook_notify", value: "true"}]
```

#### Cache Management
```
[{key: "invalidate_cache", value: "true"},
 {key: "cache_related", value: "false"},
 {key: "cache_ttl", value: "3600"}]
```

## Response Handling

### Successful Update with Parameters
```json
{
  "id": 12345,
  "name": "Updated Resource",
  "updated_at": "2024-01-15T10:30:00Z",
  "processing_results": {
    "validation_passed": true,
    "notifications_sent": 2,
    "cache_invalidated": true
  },
  "applied_parameters": {
    "validate": "strict",
    "notify": "true",
    "invalidate_cache": "true"
  }
}
```

### Conditional Update Result
```json
{
  "action": "updated",
  "id": 67890,
  "condition_met": true,
  "condition_checked": "status=active",
  "changes_applied": {
    "price": {
      "old": 179.99,
      "new": 199.99
    }
  },
  "approval_required": false
}
```

### Bulk Update Result
```json
{
  "bulk_operation_id": "bulk_update_abc123",
  "total_items": 50,
  "successful": 48,
  "failed": 2,
  "processing_time": "3.2 seconds",
  "parameters_applied": {
    "batch_size": "10",
    "notify_customers": "true"
  },
  "failed_items": [
    {
      "id": 1005,
      "error": "Invalid status transition"
    }
  ]
}
```

## Error Handling

### Parameter Validation Errors

#### Invalid Parameter Value
```json
{
  "error": "Invalid parameter value",
  "parameter": "validation_level",
  "value": "invalid_level",
  "allowed_values": ["basic", "strict", "custom"],
  "code": 400
}
```

#### Missing Required Parameter
```json
{
  "error": "Missing required parameter",
  "parameter": "expected_version",
  "message": "Version parameter required for this resource type",
  "code": 400
}
```

### Conditional Update Failures

#### Condition Not Met
```json
{
  "error": "Update condition not met",
  "condition": "status=active",
  "actual_status": "inactive",
  "message": "Resource does not meet update criteria",
  "code": 412
}
```

#### Version Mismatch
```json
{
  "error": "Version mismatch",
  "expected_version": "3",
  "current_version": "4",
  "message": "Resource has been modified by another process",
  "code": 409
}
```

### Processing Errors

#### Validation Failed
```json
{
  "error": "Validation failed",
  "validation_level": "strict",
  "details": [
    {
      "field": "email",
      "message": "Email already exists",
      "check": "duplicate_check"
    }
  ],
  "code": 400
}
```

#### Bulk Operation Partial Failure
```json
{
  "error": "Bulk operation partially failed",
  "total_items": 100,
  "successful": 85,
  "failed": 15,
  "continue_on_error": true,
  "error_details": "See failed_items array for specific errors"
}
```

## Best Practices

### Parameter Design
- Use clear, descriptive parameter names
- Provide sensible default values
- Implement parameter validation
- Document parameter interactions

### Conditional Updates
- Implement proper condition checking
- Provide clear error messages for unmet conditions
- Handle edge cases gracefully
- Support multiple condition types

### Bulk Operations
- Implement proper batch processing
- Provide progress feedback for large operations
- Handle partial failures appropriately
- Implement transaction control when needed

### Performance
- Optimize parameter processing
- Use efficient condition evaluation
- Implement proper caching strategies
- Monitor processing times

## Troubleshooting

### Parameters Not Working
1. Check parameter names and spelling
2. Verify parameter values are valid
3. Ensure proper URL encoding
4. Test parameters individually

### Conditional Updates Failing
1. Verify condition syntax and format
2. Check current resource state
3. Validate condition logic
4. Test with simpler conditions first

### Performance Issues
1. Reduce number of parameters
2. Optimize condition evaluation
3. Check for expensive parameter operations
4. Monitor bulk operation performance

## Related Operations

- [Basic Update Request](pages/basicUpdate.md) - Simple resource modification
- [Update with Headers](pages/updateWithHeaders.md) - Add authentication and custom headers
- [Basic POST Request](pages/basicPost.md) - Create new resources
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing updates with query parameters:
1. Combine with custom headers for full functionality
2. Implement comprehensive parameter validation
3. Add support for complex conditional logic
4. Consider asynchronous processing for bulk operations
