# GET with Query Parameters

## Overview

GET with Query Parameters allows you to filter, search, and customize data retrieval by adding URL query parameters. This operation is essential for implementing search functionality, filtering datasets, and passing configuration options to APIs.

## Use Cases

- Filter records by specific criteria
- Search for items matching certain conditions
- Paginate through large datasets
- Pass configuration parameters to APIs
- Sort results by specific fields

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Query Parameters | List of Multi-field | Yes | URL query parameters | `[{key: "status", value: "active"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Filtered data from the API |

## HTTP Request Format

```http
GET {URL}?{key1}={value1}&{key2}={value2} HTTP/1.1
Host: api.example.com
User-Agent: Krista-REST-Extension/2.0
```

## Query Parameter Configuration

### Parameter Structure
Each query parameter consists of:
- **Key**: Parameter name (e.g., "status", "limit", "sort")
- **Value**: Parameter value (e.g., "active", "10", "name")

### Common Parameter Types

#### Filtering Parameters
- `status=active` - Filter by status
- `category=electronics` - Filter by category
- `created_after=2024-01-01` - Date range filtering

#### Pagination Parameters
- `page=1` - Page number
- `limit=20` - Items per page
- `offset=0` - Starting position

#### Sorting Parameters
- `sort=name` - Sort by field
- `order=asc` - Sort direction
- `sort_by=created_date` - Alternative sort syntax

## Example Usage

### User Filtering

**Configuration:**
- URL: `https://api.example.com/users`
- Query Parameters:
  ```
  [{key: "status", value: "active"},
   {key: "department", value: "engineering"},
   {key: "limit", value: "10"}]
  ```

**Generated Request:**
```http
GET https://api.example.com/users?status=active&department=engineering&limit=10
```

**Expected Response:**
```json
{
  "users": [
    {
      "id": 1,
      "name": "John Doe",
      "status": "active",
      "department": "engineering"
    }
  ],
  "total": 25,
  "page": 1,
  "limit": 10
}
```

### Product Search

**Configuration:**
- URL: `https://api.example.com/products`
- Query Parameters:
  ```
  [{key: "search", value: "laptop"},
   {key: "min_price", value: "500"},
   {key: "max_price", value: "2000"},
   {key: "sort", value: "price"},
   {key: "order", value: "asc"}]
  ```

**Generated Request:**
```http
GET https://api.example.com/products?search=laptop&min_price=500&max_price=2000&sort=price&order=asc
```

### Date Range Filtering

**Configuration:**
- URL: `https://api.example.com/orders`
- Query Parameters:
  ```
  [{key: "start_date", value: "2024-01-01"},
   {key: "end_date", value: "2024-01-31"},
   {key: "status", value: "completed"}]
  ```

## Advanced Query Patterns

### Array Parameters
Some APIs support array-style parameters:
```
tags[]=javascript&tags[]=react&tags[]=nodejs
```

Configuration:
```
[{key: "tags[]", value: "javascript"},
 {key: "tags[]", value: "react"},
 {key: "tags[]", value: "nodejs"}]
```

### Nested Parameters
For complex filtering:
```
filter[user][status]=active&filter[user][role]=admin
```

Configuration:
```
[{key: "filter[user][status]", value: "active"},
 {key: "filter[user][role]", value: "admin"}]
```

### Special Characters
Handle special characters with proper encoding:
```
[{key: "search", value: "user@example.com"},
 {key: "description", value: "50% off sale"}]
```

## Response Handling

### Successful Response
```json
{
  "data": [...],
  "pagination": {
    "current_page": 1,
    "total_pages": 5,
    "total_items": 100,
    "items_per_page": 20
  },
  "filters_applied": {
    "status": "active",
    "department": "engineering"
  }
}
```

### No Results Found
```json
{
  "data": [],
  "message": "No results found matching the specified criteria",
  "filters_applied": {
    "status": "inactive"
  }
}
```

## Error Handling

### Invalid Parameter Values
```json
{
  "error": "Invalid parameter value",
  "details": {
    "parameter": "status",
    "value": "invalid_status",
    "allowed_values": ["active", "inactive", "pending"]
  }
}
```

### Missing Required Parameters
```json
{
  "error": "Missing required parameter",
  "details": {
    "parameter": "api_key",
    "message": "API key is required for this endpoint"
  }
}
```

## Best Practices

### Parameter Naming
- Use consistent naming conventions
- Follow the target API's parameter standards
- Use descriptive parameter names

### Value Formatting
- Encode special characters properly
- Use appropriate date formats (ISO 8601 recommended)
- Handle boolean values consistently (true/false, 1/0)

### Performance Optimization
- Limit the number of parameters when possible
- Use pagination for large result sets
- Cache frequently used parameter combinations

### Security
- Validate parameter values before sending
- Avoid exposing sensitive data in URLs
- Use POST requests for sensitive parameters when possible

## Troubleshooting

### Parameters Not Working
1. Check parameter names match API documentation
2. Verify parameter values are in correct format
3. Ensure proper URL encoding
4. Test parameters individually

### Unexpected Results
1. Review API documentation for parameter behavior
2. Check for case sensitivity in parameter names/values
3. Verify parameter combinations are supported
4. Test with minimal parameter set first

## Related Operations

- [Basic GET Request](pages/basicGet.md) - Simple data retrieval
- [GET with Headers](pages/getWithHeaders.md) - Add authentication and custom headers
- [GET with Pagination](pages/getWithPagination.md) - Built-in pagination support
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After mastering query parameters, consider:
1. Combining with custom headers for authentication
2. Implementing pagination for large datasets
3. Adding error handling for invalid parameters
4. Creating reusable parameter templates
