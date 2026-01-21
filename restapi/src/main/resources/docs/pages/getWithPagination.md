# GET with Pagination

## Overview

GET with Pagination provides built-in support for handling large datasets by breaking them into manageable chunks. This operation automatically manages page navigation and provides comprehensive pagination metadata.

## Use Cases

- Process large datasets efficiently
- Implement user-friendly data browsing
- Reduce memory usage and response times
- Handle APIs with large result sets
- Implement infinite scroll or page-based navigation

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Page Size | Number | Yes | Records per page (max 500) | `50` |
| Page Index | Number | Yes | Page number (starts from 0) | `0` |
| Query Parameters | List of Multi-field | No | Additional filters | `[{key: "status", value: "active"}]` |
| Headers | List of Multi-field | No | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Paginated data |
| Total Records | Number | Total number of records available |
| Total Pages | Number | Total number of pages |
| Page Size | Number | Records per page |
| Page Index | Number | Current page number |

## Pagination Rules

### Page Index
- **Starting Value**: 0 (zero-based indexing)
- **Minimum Value**: 0 (cannot be negative)
- **Maximum Value**: Total Pages - 1

### Page Size
- **Minimum Value**: 1
- **Maximum Value**: 500 records
- **Recommended Values**: 10, 20, 50, 100

### Total Pages Calculation
```
Total Pages = ceil(Total Records / Page Size)
```

## HTTP Request Format

The extension automatically constructs pagination parameters:

```http
GET {URL}?page={Page Index}&limit={Page Size}&{additional_params} HTTP/1.1
Host: api.example.com
{Custom Headers}
```

## Example Usage

### Basic User Pagination

**Configuration:**
- URL: `https://api.example.com/users`
- Page Size: `20`
- Page Index: `0`

**Generated Request:**
```http
GET https://api.example.com/users?page=0&limit=20
```

**Expected Response:**
```json
{
  "users": [
    {"id": 1, "name": "John Doe"},
    {"id": 2, "name": "Jane Smith"},
    // ... 18 more users
  ],
  "pagination": {
    "current_page": 0,
    "page_size": 20,
    "total_records": 1250,
    "total_pages": 63
  }
}
```

**Krista Output:**
- Response: User data array
- Total Records: 1250
- Total Pages: 63
- Page Size: 20
- Page Index: 0

### Filtered Pagination

**Configuration:**
- URL: `https://api.example.com/products`
- Page Size: `10`
- Page Index: `2`
- Query Parameters: `[{key: "category", value: "electronics"}]`
- Headers: `[{key: "Authorization", value: "Bearer token123"}]`

**Generated Request:**
```http
GET https://api.example.com/products?page=2&limit=10&category=electronics
Authorization: Bearer token123
```

### Large Dataset Processing

**Configuration:**
- URL: `https://api.example.com/transactions`
- Page Size: `500` (maximum)
- Page Index: `0`
- Query Parameters: `[{key: "date_from", value: "2024-01-01"}]`

## Pagination Strategies

### Sequential Processing
Process all pages in order:
```
Page 0: Records 1-50
Page 1: Records 51-100
Page 2: Records 101-150
...
```

### Targeted Page Access
Jump to specific pages:
```
Page 10: Records 501-550
Page 25: Records 1251-1300
```

### Reverse Pagination
Start from the last page:
```
Last Page = Total Pages - 1
Page (Total Pages - 1): Last 50 records
```

## Response Handling

### Successful Pagination Response
```json
{
  "data": [...],
  "meta": {
    "current_page": 2,
    "per_page": 20,
    "total": 1250,
    "last_page": 62,
    "from": 41,
    "to": 60
  }
}
```

### Empty Page Response
```json
{
  "data": [],
  "meta": {
    "current_page": 100,
    "per_page": 20,
    "total": 1250,
    "last_page": 62,
    "message": "Page index exceeds available pages"
  }
}
```

### Single Page Result
```json
{
  "data": [
    {"id": 1, "name": "Only Item"}
  ],
  "meta": {
    "current_page": 0,
    "per_page": 20,
    "total": 1,
    "last_page": 0
  }
}
```

## Error Handling

### Invalid Page Index
```json
{
  "error": "Invalid page index",
  "message": "Page index cannot be negative",
  "provided_value": -1,
  "valid_range": "0 to 62"
}
```

### Invalid Page Size
```json
{
  "error": "Invalid page size",
  "message": "Page size must be between 1 and 500",
  "provided_value": 1000,
  "valid_range": "1 to 500"
}
```

### Page Out of Range
```json
{
  "error": "Page out of range",
  "message": "Requested page exceeds available pages",
  "requested_page": 100,
  "total_pages": 63
}
```

## Performance Optimization

### Optimal Page Sizes
- **Small datasets (< 1000 records)**: 50-100 per page
- **Medium datasets (1000-10000 records)**: 100-200 per page
- **Large datasets (> 10000 records)**: 200-500 per page

### Memory Management
- Process pages sequentially for large datasets
- Implement proper cleanup between pages
- Monitor memory usage during processing

### Network Efficiency
- Use appropriate page sizes to minimize requests
- Implement caching for frequently accessed pages
- Consider compression for large responses

## Best Practices

### Page Size Selection
- Start with smaller page sizes for testing
- Increase page size based on performance needs
- Consider API rate limits when choosing page size
- Balance between request count and response size

### Error Handling
- Always validate page parameters before requests
- Implement retry logic for failed pages
- Handle edge cases (empty results, single page)
- Provide meaningful error messages

### Progress Tracking
- Calculate and display progress percentage
- Implement cancellation mechanisms for long operations
- Log pagination progress for debugging
- Provide estimated completion times

## Troubleshooting

### No Results Returned
1. Check if page index is within valid range
2. Verify filters aren't too restrictive
3. Confirm total records count is accurate
4. Test with page index 0

### Inconsistent Page Counts
1. Verify data isn't changing during pagination
2. Check for concurrent modifications
3. Implement consistent read mechanisms
4. Use timestamps for data consistency

### Performance Issues
1. Reduce page size if responses are slow
2. Optimize query parameters and filters
3. Check for API rate limiting
4. Consider parallel processing for independent pages

## Related Operations

- [Basic GET Request](pages/basicGet.md) - Simple data retrieval
- [GET with Query Parameters](pages/getWithFilters.md) - Add filtering
- [GET with Headers](pages/getWithHeaders.md) - Authentication and custom headers
- [GET Response as File](pages/getAsFile.md) - Download large datasets

## Next Steps

After implementing pagination:
1. Combine with filtering for targeted data retrieval
2. Implement progress tracking for user feedback
3. Add error recovery and retry mechanisms
4. Consider parallel processing for performance optimization
