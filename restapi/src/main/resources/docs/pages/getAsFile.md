# GET Response as File

## Overview

GET Response as File downloads API response data directly as a file, enabling offline processing, data archival, and handling of large datasets. This operation is ideal for exporting data, downloading reports, and retrieving binary content.

## Use Cases

- Export large datasets to files
- Download generated reports (PDF, CSV, Excel)
- Retrieve binary files (images, documents, archives)
- Backup data in various formats
- Offline data processing and analysis
- Batch data downloads

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/export/users` |
| Query Parameters | List of Multi-field | No | URL query parameters | `[{key: "format", value: "csv"}]` |
| Headers | List of Multi-field | No | HTTP headers | `[{key: "Accept", value: "text/csv"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response File | File | Downloaded file content |

## HTTP Request Format

```http
GET {URL}?{query_params} HTTP/1.1
Host: api.example.com
Accept: {content_type}
{Custom Headers}
```

## Supported File Formats

### Text Formats
- **CSV**: Comma-separated values
- **TSV**: Tab-separated values
- **JSON**: JavaScript Object Notation
- **XML**: Extensible Markup Language
- **TXT**: Plain text

### Document Formats
- **PDF**: Portable Document Format
- **XLSX**: Excel spreadsheet
- **DOCX**: Word document
- **RTF**: Rich Text Format

### Binary Formats
- **ZIP**: Compressed archives
- **PNG/JPG**: Images
- **MP4/AVI**: Videos
- **MP3/WAV**: Audio files

## Example Usage

### CSV Data Export

**Configuration:**
- URL: `https://api.example.com/export/users`
- Query Parameters: `[{key: "format", value: "csv"}]`
- Headers: `[{key: "Accept", value: "text/csv"}]`

**Generated Request:**
```http
GET https://api.example.com/export/users?format=csv
Accept: text/csv
Authorization: Bearer token123
```

**Response File Content:**
```csv
id,name,email,department,created_date
1,John Doe,john@example.com,Engineering,2024-01-15
2,Jane Smith,jane@example.com,Marketing,2024-01-16
3,Bob Wilson,bob@example.com,Sales,2024-01-17
```

### PDF Report Download

**Configuration:**
- URL: `https://api.example.com/reports/monthly`
- Query Parameters: `[{key: "month", value: "2024-01"}, {key: "format", value: "pdf"}]`
- Headers: `[{key: "Accept", value: "application/pdf"}]`

**Generated Request:**
```http
GET https://api.example.com/reports/monthly?month=2024-01&format=pdf
Accept: application/pdf
Authorization: Bearer token123
```

**Response:** Binary PDF file content

### Excel Spreadsheet Export

**Configuration:**
- URL: `https://api.example.com/data/financial`
- Query Parameters: `[{key: "year", value: "2024"}, {key: "format", value: "xlsx"}]`
- Headers: `[{key: "Accept", value: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}]`

### Image Download

**Configuration:**
- URL: `https://api.example.com/files/image/12345`
- Headers: `[{key: "Accept", value: "image/png"}]`

## Content Type Negotiation

### Accept Headers for Different Formats

#### CSV Files
```
[{key: "Accept", value: "text/csv"}]
```

#### JSON Files
```
[{key: "Accept", value: "application/json"}]
```

#### PDF Files
```
[{key: "Accept", value: "application/pdf"}]
```

#### Excel Files
```
[{key: "Accept", value: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}]
```

#### ZIP Archives
```
[{key: "Accept", value: "application/zip"}]
```

## Response Handling

### Successful File Download

**Response Info:**
```json
{
  "status_code": 200,
  "headers": {
    "Content-Type": "text/csv",
    "Content-Length": "2048",
    "Content-Disposition": "attachment; filename=\"users_export.csv\"",
    "Cache-Control": "no-cache"
  },
  "download_time": "2.3 seconds",
  "file_size": "2048 bytes"
}
```

**Response File:** Binary file content ready for use

### Large File Download

**Response Info:**
```json
{
  "status_code": 200,
  "headers": {
    "Content-Type": "application/zip",
    "Content-Length": "52428800",
    "Content-Disposition": "attachment; filename=\"data_backup.zip\"",
    "Transfer-Encoding": "chunked"
  },
  "download_time": "45.2 seconds",
  "file_size": "50 MB"
}
```

## Error Handling

### Unsupported Format

**Response Info:**
```json
{
  "status_code": 406,
  "error": "Not Acceptable",
  "message": "Requested format not supported",
  "supported_formats": ["csv", "json", "pdf", "xlsx"]
}
```

### File Not Found

**Response Info:**
```json
{
  "status_code": 404,
  "error": "File Not Found",
  "message": "The requested file does not exist or has been removed"
}
```

### File Too Large

**Response Info:**
```json
{
  "status_code": 413,
  "error": "Payload Too Large",
  "message": "File size exceeds maximum download limit",
  "max_size": "100 MB",
  "actual_size": "150 MB"
}
```

### Generation Failed

**Response Info:**
```json
{
  "status_code": 500,
  "error": "File Generation Failed",
  "message": "Unable to generate requested file format",
  "details": "Insufficient data for report generation"
}
```

## Advanced Features

### Streaming Downloads
For large files, the extension supports streaming downloads:
- Reduces memory usage
- Provides progress feedback
- Enables cancellation of long downloads

### Compression Support
Automatic handling of compressed responses:
- Gzip compression
- Deflate compression
- Brotli compression (where supported)

### Resume Support
For interrupted downloads:
- Range request headers
- Partial content handling
- Download resumption

## Best Practices

### File Format Selection
- Choose appropriate formats for your data type
- Consider file size vs. compatibility trade-offs
- Use compression for large text files
- Specify exact MIME types in Accept headers

### Performance Optimization
- Use streaming for large files
- Implement timeout handling for slow downloads
- Consider parallel downloads for multiple files
- Monitor download progress for user feedback

### Error Handling
- Validate file availability before download
- Implement retry logic for network failures
- Handle partial downloads gracefully
- Provide meaningful error messages

### Security
- Validate file types and sizes
- Scan downloaded files for security threats
- Use HTTPS for sensitive file downloads
- Implement proper access controls

## File Processing

### Post-Download Operations
After successful download, you can:
- Parse CSV/JSON files for data processing
- Extract ZIP archives
- Convert between file formats
- Validate file integrity

### Integration with Other Operations
- Combine with pagination for large dataset exports
- Use with authentication for secure file access
- Chain with other API operations for workflow automation

## Troubleshooting

### Download Failures
1. Check network connectivity and stability
2. Verify API endpoint supports file downloads
3. Confirm Accept headers match available formats
4. Test with smaller files first

### Corrupted Files
1. Verify Content-Length matches actual file size
2. Check for network interruptions during download
3. Validate file format and structure
4. Retry download with different parameters

### Performance Issues
1. Use appropriate timeout values
2. Consider file size limitations
3. Implement progress monitoring
4. Use streaming for large files

## Related Operations

- [Basic GET Request](pages/basicGet.md) - Simple data retrieval
- [GET with Query Parameters](pages/getWithFilters.md) - Filter data before download
- [GET with Headers](pages/getWithHeaders.md) - Authentication and content negotiation
- [GET with Pagination](pages/getWithPagination.md) - Handle large datasets

## Next Steps

After implementing file downloads:
1. Add file validation and processing logic
2. Implement progress tracking for large downloads
3. Set up automated file processing workflows
4. Consider file storage and archival strategies
