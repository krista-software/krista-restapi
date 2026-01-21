# Asynchronous GET Operations

## Overview

Asynchronous GET operations handle long-running data retrieval tasks that cannot be completed within a standard HTTP request timeout. This pattern is essential for complex queries, large data processing, and operations that require significant server-side computation.

## Use Cases

- Complex data aggregation and reporting
- Large dataset exports that take time to generate
- Machine learning model predictions
- Data transformation and processing
- Multi-source data compilation
- Heavy computational queries

## Operation Flow

The asynchronous pattern consists of two operations:

1. **Wait for Event GET Request**: Initiates the async operation
2. **Wait for Event GET Response**: Retrieves the results

## Wait for Event GET Request

### Overview
Initiates an asynchronous GET operation and returns a task ID for tracking.

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | API endpoint for async operation | `https://api.example.com/async/reports` |
| Query Parameters | List of Multi-field | Yes | Request parameters | `[{key: "type", value: "annual_summary"}]` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Task Id | Text | Unique identifier for tracking the operation |

### HTTP Request Format

```http
POST {URL} HTTP/1.1
Host: api.example.com
Content-Type: application/json
{Custom Headers}

{
  "operation": "async_get",
  "parameters": {query_parameters}
}
```

## Wait for Event GET Response

### Overview
Retrieves the results of an asynchronous operation using the task ID.

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Task Id | Text | Yes | Task identifier from async request | `task_abc123def456` |

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

### Annual Report Generation

**Step 1: Initiate Async Operation**

**Configuration:**
- URL: `https://api.example.com/async/reports`
- Query Parameters:
  ```
  [{key: "report_type", value: "annual"},
   {key: "year", value: "2024"},
   {key: "format", value: "detailed"}]
  ```
- Headers: `[{key: "Authorization", value: "Bearer token123"}]`

**Generated Request:**
```http
POST https://api.example.com/async/reports
Authorization: Bearer token123
Content-Type: application/json

{
  "report_type": "annual",
  "year": "2024",
  "format": "detailed"
}
```

**Response:**
```json
{
  "task_id": "report_2024_abc123",
  "status": "initiated",
  "estimated_completion": "2024-01-15T10:45:00Z",
  "message": "Annual report generation started"
}
```

**Step 2: Check Status and Get Results**

**Configuration:**
- Task Id: `report_2024_abc123`

**Generated Request:**
```http
GET https://api.example.com/async/status/report_2024_abc123
Authorization: Bearer token123
```

**Response (In Progress):**
```json
{
  "task_id": "report_2024_abc123",
  "status": "processing",
  "progress": 65,
  "estimated_completion": "2024-01-15T10:45:00Z",
  "message": "Generating financial summaries..."
}
```

**Response (Completed):**
```json
{
  "task_id": "report_2024_abc123",
  "status": "completed",
  "progress": 100,
  "completed_at": "2024-01-15T10:42:30Z",
  "data": {
    "report": {
      "total_revenue": 5000000,
      "total_expenses": 3500000,
      "net_profit": 1500000,
      "growth_rate": 12.5
    },
    "download_url": "https://api.example.com/files/report_2024_abc123.pdf"
  }
}
```

### Large Dataset Export

**Step 1: Initiate Export**

**Configuration:**
- URL: `https://api.example.com/async/export`
- Query Parameters:
  ```
  [{key: "table", value: "transactions"},
   {key: "date_from", value: "2024-01-01"},
   {key: "date_to", value: "2024-12-31"},
   {key: "format", value: "csv"}]
  ```

**Response:**
```json
{
  "task_id": "export_trans_def789",
  "status": "queued",
  "estimated_records": 2500000,
  "estimated_completion": "2024-01-15T11:30:00Z"
}
```

**Step 2: Monitor Progress**

Multiple status checks showing progress:

```json
{
  "task_id": "export_trans_def789",
  "status": "processing",
  "progress": 25,
  "records_processed": 625000,
  "estimated_completion": "2024-01-15T11:25:00Z"
}
```

```json
{
  "task_id": "export_trans_def789",
  "status": "completed",
  "progress": 100,
  "records_processed": 2500000,
  "file_size": "125 MB",
  "download_url": "https://api.example.com/downloads/export_trans_def789.csv"
}
```

## Task Status States

### Standard Status Values

#### Queued
```json
{
  "status": "queued",
  "message": "Task is waiting to be processed",
  "queue_position": 3
}
```

#### Processing
```json
{
  "status": "processing",
  "progress": 45,
  "message": "Processing data...",
  "current_step": "aggregating_results"
}
```

#### Completed
```json
{
  "status": "completed",
  "progress": 100,
  "completed_at": "2024-01-15T10:30:00Z",
  "data": {...}
}
```

#### Failed
```json
{
  "status": "failed",
  "error": "Insufficient data",
  "message": "Unable to generate report due to missing data for Q3",
  "failed_at": "2024-01-15T10:25:00Z"
}
```

#### Cancelled
```json
{
  "status": "cancelled",
  "message": "Task was cancelled by user request",
  "cancelled_at": "2024-01-15T10:20:00Z"
}
```

## Polling Strategies

### Basic Polling
Check status at regular intervals:
```
Initial request → Wait 5s → Check status → Wait 10s → Check status → ...
```

### Exponential Backoff
Increase wait time between checks:
```
Check → Wait 2s → Check → Wait 4s → Check → Wait 8s → Check → Wait 16s → ...
```

### Adaptive Polling
Adjust based on estimated completion:
```
If estimated_time > 60s: Poll every 30s
If estimated_time > 300s: Poll every 60s
If estimated_time > 1800s: Poll every 300s
```

## Error Handling

### Task Not Found
```json
{
  "error": "Task not found",
  "task_id": "invalid_task_123",
  "message": "The specified task ID does not exist or has expired"
}
```

### Task Expired
```json
{
  "error": "Task expired",
  "task_id": "old_task_456",
  "message": "Task results are no longer available",
  "expired_at": "2024-01-10T10:30:00Z"
}
```

### Processing Failed
```json
{
  "status": "failed",
  "error": "Processing error",
  "message": "Data processing failed due to invalid parameters",
  "details": {
    "parameter": "date_range",
    "issue": "End date cannot be before start date"
  }
}
```

## Best Practices

### Task Management
- Store task IDs securely for later retrieval
- Implement proper timeout handling
- Set up monitoring for long-running tasks
- Clean up completed tasks when no longer needed

### Polling Optimization
- Use appropriate polling intervals
- Implement exponential backoff for efficiency
- Respect API rate limits during polling
- Stop polling when task completes or fails

### Error Recovery
- Implement retry logic for failed tasks
- Handle network interruptions gracefully
- Provide meaningful error messages to users
- Log task failures for debugging

### User Experience
- Show progress indicators for long operations
- Provide estimated completion times
- Allow task cancellation when possible
- Send notifications when tasks complete

## Performance Considerations

### Resource Management
- Monitor server resources during async operations
- Implement queue management for high loads
- Set appropriate timeouts for different operation types
- Clean up expired tasks regularly

### Scalability
- Use distributed task queues for high volume
- Implement load balancing for async workers
- Monitor queue depths and processing times
- Scale workers based on demand

## Troubleshooting

### Task Never Completes
1. Check server logs for processing errors
2. Verify task parameters are valid
3. Confirm sufficient server resources
4. Check for deadlocks or infinite loops

### Status Not Updating
1. Verify task ID is correct
2. Check authentication for status endpoint
3. Confirm task is actually processing
4. Review server-side status update logic

### Results Not Available
1. Check if task completed successfully
2. Verify result storage and retrieval
3. Confirm task hasn't expired
4. Check for result generation errors

## Related Operations

- [Basic GET Request](pages/basicGet.md) - Simple synchronous retrieval
- [GET with Pagination](pages/getWithPagination.md) - Handle large datasets synchronously
- [GET Response as File](pages/getAsFile.md) - Download generated files
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing async operations:
1. Set up proper task monitoring and alerting
2. Implement user notifications for task completion
3. Add task cancellation capabilities
4. Consider webhook notifications for task updates
