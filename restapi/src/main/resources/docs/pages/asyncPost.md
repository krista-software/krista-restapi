# Asynchronous POST Operations

## Overview

Asynchronous POST operations handle long-running resource creation tasks that cannot be completed within a standard HTTP request timeout. This pattern is essential for complex data processing, bulk operations, and resource creation that requires significant server-side computation.

## Use Cases

- Bulk data imports and processing
- Complex resource creation with validation
- File processing and transformation
- Data migration and synchronization
- Machine learning model training
- Large-scale batch operations

## Operation Flow

The asynchronous pattern consists of two operations:

1. **Wait for Event POST Request**: Initiates the async operation
2. **Wait for Event POST Response**: Retrieves the results

## Wait for Event POST Request

### Overview
Initiates an asynchronous POST operation and returns a task ID for tracking.

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | API endpoint for async operation | `https://api.example.com/async/import` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"data\": [...]}", file: null}` |
| Query Parameters | List of Multi-field | Yes | Request parameters | `[{key: "batch_size", value: "1000"}]` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Task Id | Text | Unique identifier for tracking the operation |

### HTTP Request Format

```http
POST {URL}?{query_params} HTTP/1.1
Host: api.example.com
Content-Type: application/json
{Custom Headers}

{Request Body}
```

## Wait for Event POST Response

### Overview
Retrieves the results of an asynchronous POST operation using the task ID.

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Task Id | Text | Yes | Task identifier from async request | `import_task_abc123` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Operation results |

### HTTP Request Format

```http
GET {base_url}/async/status/{task_id} HTTP/1.1
Host: api.example.com
{Authentication Headers}
```

## Example Usage

### Bulk User Import

**Step 1: Initiate Bulk Import**

**Configuration:**
- URL: `https://api.example.com/async/users/import`
- Payload:
  ```
  key: "json"
  value: {
    "users": [
      {"name": "John Doe", "email": "john@example.com", "department": "Engineering"},
      {"name": "Jane Smith", "email": "jane@example.com", "department": "Marketing"},
      // ... 1000 more users
    ],
    "options": {
      "send_welcome_emails": true,
      "create_default_permissions": true
    }
  }
  ```
- Query Parameters:
  ```
  [{key: "batch_size", value: "100"},
   {key: "continue_on_error", value: "true"}]
  ```
- Headers: `[{key: "Authorization", value: "Bearer token123"}]`

**Generated Request:**
```http
POST https://api.example.com/async/users/import?batch_size=100&continue_on_error=true
Authorization: Bearer token123
Content-Type: application/json

{
  "users": [...],
  "options": {
    "send_welcome_emails": true,
    "create_default_permissions": true
  }
}
```

**Response:**
```json
{
  "task_id": "import_users_abc123",
  "status": "queued",
  "total_items": 1000,
  "estimated_completion": "2024-01-15T10:45:00Z",
  "message": "Bulk user import initiated"
}
```

**Step 2: Monitor Progress and Get Results**

**Configuration:**
- Task Id: `import_users_abc123`

**Generated Request:**
```http
GET https://api.example.com/async/status/import_users_abc123
Authorization: Bearer token123
```

**Response (In Progress):**
```json
{
  "task_id": "import_users_abc123",
  "status": "processing",
  "progress": 45,
  "items_processed": 450,
  "items_successful": 440,
  "items_failed": 10,
  "estimated_completion": "2024-01-15T10:40:00Z",
  "current_batch": 5,
  "message": "Processing batch 5 of 10..."
}
```

**Response (Completed):**
```json
{
  "task_id": "import_users_abc123",
  "status": "completed",
  "progress": 100,
  "completed_at": "2024-01-15T10:38:45Z",
  "summary": {
    "total_items": 1000,
    "successful": 985,
    "failed": 15,
    "skipped": 0
  },
  "results": {
    "created_users": 985,
    "welcome_emails_sent": 985,
    "permissions_created": 985
  },
  "errors": [
    {
      "item": 23,
      "error": "Duplicate email address",
      "data": {"email": "duplicate@example.com"}
    }
  ]
}
```

### Large File Processing

**Step 1: Initiate File Processing**

**Configuration:**
- URL: `https://api.example.com/async/files/process`
- Payload:
  ```
  key: "json"
  value: {
    "file_url": "https://storage.example.com/large_dataset.csv",
    "processing_options": {
      "validate_data": true,
      "transform_format": "json",
      "extract_metadata": true
    }
  }
  ```
- Query Parameters:
  ```
  [{key: "priority", value: "high"},
   {key: "notify_completion", value: "true"}]
  ```

**Response:**
```json
{
  "task_id": "process_file_def456",
  "status": "initiated",
  "file_size": "250 MB",
  "estimated_processing_time": "15 minutes",
  "estimated_completion": "2024-01-15T11:00:00Z"
}
```

**Step 2: Check Processing Status**

**Response (Processing):**
```json
{
  "task_id": "process_file_def456",
  "status": "processing",
  "progress": 60,
  "current_step": "data_transformation",
  "steps_completed": ["validation", "parsing"],
  "steps_remaining": ["transformation", "metadata_extraction", "finalization"],
  "estimated_completion": "2024-01-15T10:55:00Z"
}
```

**Response (Completed):**
```json
{
  "task_id": "process_file_def456",
  "status": "completed",
  "progress": 100,
  "completed_at": "2024-01-15T10:52:30Z",
  "results": {
    "processed_records": 500000,
    "output_file": "https://storage.example.com/processed/dataset_def456.json",
    "metadata_file": "https://storage.example.com/metadata/dataset_def456_meta.json",
    "validation_report": "https://storage.example.com/reports/validation_def456.html"
  },
  "processing_stats": {
    "total_time": "12 minutes 30 seconds",
    "records_per_second": 666,
    "data_quality_score": 98.5
  }
}
```

## Task Status States

### Queued
```json
{
  "status": "queued",
  "message": "Task is waiting in the processing queue",
  "queue_position": 3,
  "estimated_start": "2024-01-15T10:35:00Z"
}
```

### Processing
```json
{
  "status": "processing",
  "progress": 35,
  "current_step": "data_validation",
  "items_processed": 350,
  "items_remaining": 650,
  "processing_rate": "50 items/second"
}
```

### Completed
```json
{
  "status": "completed",
  "progress": 100,
  "completed_at": "2024-01-15T10:45:00Z",
  "total_processing_time": "8 minutes 30 seconds",
  "results": {...}
}
```

### Failed
```json
{
  "status": "failed",
  "error": "Data validation failed",
  "message": "Input data contains invalid records that cannot be processed",
  "failed_at": "2024-01-15T10:25:00Z",
  "error_details": {
    "validation_errors": 25,
    "first_error": "Invalid email format in record 150"
  }
}
```

### Cancelled
```json
{
  "status": "cancelled",
  "message": "Task was cancelled by user request",
  "cancelled_at": "2024-01-15T10:30:00Z",
  "items_processed_before_cancellation": 200
}
```

## Advanced Features

### Batch Processing Control

#### Batch Size Optimization
```
[{key: "batch_size", value: "500"},
 {key: "max_concurrent_batches", value: "3"}]
```

#### Error Handling Strategies
```
[{key: "continue_on_error", value: "true"},
 {key: "max_error_rate", value: "5"},
 {key: "stop_on_critical_error", value: "true"}]
```

### Progress Tracking

#### Detailed Progress Information
```json
{
  "progress": {
    "percentage": 65,
    "current_item": 650,
    "total_items": 1000,
    "current_batch": 7,
    "total_batches": 10,
    "estimated_remaining_time": "3 minutes 20 seconds"
  }
}
```

#### Step-by-Step Progress
```json
{
  "steps": [
    {"name": "validation", "status": "completed", "duration": "2 minutes"},
    {"name": "transformation", "status": "processing", "progress": 40},
    {"name": "storage", "status": "pending"},
    {"name": "notification", "status": "pending"}
  ]
}
```

### Result Management

#### Large Result Handling
```json
{
  "results": {
    "summary": {
      "total_processed": 10000,
      "successful": 9850,
      "failed": 150
    },
    "detailed_results_url": "https://api.example.com/results/task_abc123",
    "download_links": {
      "success_report": "https://storage.example.com/reports/success_abc123.csv",
      "error_report": "https://storage.example.com/reports/errors_abc123.csv"
    }
  }
}
```

## Error Handling

### Task Initiation Errors

#### Invalid Payload
```json
{
  "error": "Invalid payload",
  "message": "Request body contains invalid data structure",
  "details": {
    "field": "users",
    "issue": "Expected array, received object"
  }
}
```

#### Resource Limits Exceeded
```json
{
  "error": "Resource limit exceeded",
  "message": "Payload size exceeds maximum limit for async operations",
  "limits": {
    "max_payload_size": "10 MB",
    "received_size": "15 MB"
  }
}
```

### Processing Errors

#### Partial Failure
```json
{
  "status": "completed_with_errors",
  "progress": 100,
  "summary": {
    "total": 1000,
    "successful": 850,
    "failed": 150
  },
  "error_summary": {
    "validation_errors": 100,
    "processing_errors": 50
  }
}
```

#### Critical Failure
```json
{
  "status": "failed",
  "error": "Critical system error",
  "message": "Database connection lost during processing",
  "recovery_options": [
    "Retry the operation",
    "Process in smaller batches",
    "Contact support"
  ]
}
```

## Best Practices

### Task Design
- Break large operations into manageable chunks
- Implement proper error handling and recovery
- Provide meaningful progress updates
- Set appropriate timeouts and limits

### Monitoring
- Implement comprehensive logging
- Track processing metrics and performance
- Monitor resource usage during operations
- Set up alerts for failed operations

### User Experience
- Provide clear progress indicators
- Estimate completion times accurately
- Allow task cancellation when possible
- Send notifications for completed operations

### Resource Management
- Implement queue management for high loads
- Monitor and limit concurrent operations
- Clean up completed tasks regularly
- Optimize batch sizes for performance

## Troubleshooting

### Task Never Starts
1. Check queue status and position
2. Verify resource availability
3. Confirm authentication and permissions
4. Review task priority and scheduling

### Processing Stalls
1. Monitor server resources and performance
2. Check for deadlocks or blocking operations
3. Verify data integrity and format
4. Review batch size and processing parameters

### High Error Rates
1. Validate input data quality
2. Check processing logic and validation rules
3. Review error patterns and common failures
4. Adjust batch sizes and error thresholds

## Related Operations

- [Basic POST Request](pages/basicPost.md) - Simple resource creation
- [POST with Headers](pages/postWithHeaders.md) - Authentication and custom headers
- [POST with Query Parameters](pages/postWithFilters.md) - Include URL parameters
- [Asynchronous GET](pages/asyncGet.md) - Long-running data retrieval

## Next Steps

After implementing async POST operations:
1. Set up comprehensive monitoring and alerting
2. Implement user notifications for task completion
3. Add task cancellation and retry capabilities
4. Consider webhook notifications for external systems
