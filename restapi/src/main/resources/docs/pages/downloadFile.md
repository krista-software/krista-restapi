# Download File from URL

## Overview

Download File from URL enables direct file retrieval from external URLs without requiring complex API authentication or headers. This operation is ideal for downloading publicly accessible files, generated reports, and shared resources.

## Use Cases

- Download generated reports and exports
- Retrieve shared documents and files
- Access public file repositories
- Download backup files and archives
- Fetch media files and assets
- Retrieve temporary file links

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Download URL | Text | Yes | Direct file download URL | `https://api.example.com/files/download/report123.pdf` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| File | File | Downloaded file content ready for use |

## HTTP Request Format

```http
GET {Download URL} HTTP/1.1
Host: api.example.com
User-Agent: Krista-REST-Extension/2.0
Accept: */*
```

## Example Usage

### Download PDF Report

**Configuration:**
- Download URL: `https://api.example.com/reports/download/monthly_report_2024_01.pdf`

**Generated Request:**
```http
GET https://api.example.com/reports/download/monthly_report_2024_01.pdf
```

**Result:**
- File: `monthly_report_2024_01.pdf` (Binary PDF content)
- File Size: 2.5 MB
- Content Type: application/pdf

### Download CSV Export

**Configuration:**
- Download URL: `https://storage.example.com/exports/users_export_20240115.csv`

**Generated Request:**
```http
GET https://storage.example.com/exports/users_export_20240115.csv
```

**Result:**
- File: `users_export_20240115.csv` (Text CSV content)
- File Size: 1.2 MB
- Content Type: text/csv

### Download Image File

**Configuration:**
- Download URL: `https://cdn.example.com/images/product_photo_12345.jpg`

**Generated Request:**
```http
GET https://cdn.example.com/images/product_photo_12345.jpg
```

**Result:**
- File: `product_photo_12345.jpg` (Binary image content)
- File Size: 850 KB
- Content Type: image/jpeg

### Download Archive

**Configuration:**
- Download URL: `https://backup.example.com/archives/data_backup_20240115.zip`

**Generated Request:**
```http
GET https://backup.example.com/archives/data_backup_20240115.zip
```

**Result:**
- File: `data_backup_20240115.zip` (Binary archive content)
- File Size: 125 MB
- Content Type: application/zip

## Supported File Types

### Document Formats
- **PDF**: Portable Document Format files
- **DOC/DOCX**: Microsoft Word documents
- **XLS/XLSX**: Microsoft Excel spreadsheets
- **PPT/PPTX**: Microsoft PowerPoint presentations
- **TXT**: Plain text files

### Data Formats
- **CSV**: Comma-separated values
- **JSON**: JavaScript Object Notation
- **XML**: Extensible Markup Language
- **TSV**: Tab-separated values
- **SQL**: Database dump files

### Media Formats
- **Images**: JPG, PNG, GIF, BMP, SVG
- **Audio**: MP3, WAV, AAC, FLAC
- **Video**: MP4, AVI, MOV, WMV
- **Graphics**: PSD, AI, EPS

### Archive Formats
- **ZIP**: Compressed archives
- **RAR**: WinRAR archives
- **TAR**: Tape archive files
- **GZ**: Gzip compressed files
- **7Z**: 7-Zip archives

## URL Patterns

### Direct File URLs
```
https://api.example.com/files/download/12345
https://storage.example.com/documents/report.pdf
https://cdn.example.com/assets/image.jpg
```

### Temporary Download Links
```
https://api.example.com/temp/abc123def456/file.zip
https://secure.example.com/download?token=xyz789&file=document.pdf
https://storage.example.com/signed/url/with/expiration
```

### CDN and Storage URLs
```
https://cdn.amazonaws.com/bucket/file.pdf
https://storage.googleapis.com/bucket/document.docx
https://files.dropbox.com/shared/link/file.zip
```

### API-Generated URLs
```
https://api.example.com/export/users?format=csv&token=abc123
https://reports.example.com/generate/monthly?id=456&download=true
https://backup.example.com/restore/database?backup_id=789
```

## Response Handling

### Successful Download
The file is downloaded and made available as a File object with the following characteristics:

#### File Properties
- **Content**: Binary or text file content
- **Size**: File size in bytes
- **Type**: MIME content type
- **Name**: Original filename (if available)

#### Download Metadata
- **Download Time**: Time taken to download
- **Source URL**: Original download URL
- **Content Length**: File size from server
- **Last Modified**: File modification date (if available)

### Large File Handling
For large files, the extension provides:
- **Streaming Download**: Efficient memory usage
- **Progress Tracking**: Download progress feedback
- **Resume Support**: Ability to resume interrupted downloads
- **Timeout Management**: Appropriate timeouts for large files

## Error Handling

### File Not Found (404)
```json
{
  "error": "File not found",
  "url": "https://api.example.com/files/missing.pdf",
  "status_code": 404,
  "message": "The requested file does not exist or has been removed"
}
```

### Access Denied (403)
```json
{
  "error": "Access denied",
  "url": "https://secure.example.com/private/file.pdf",
  "status_code": 403,
  "message": "You do not have permission to access this file"
}
```

### File Too Large (413)
```json
{
  "error": "File too large",
  "url": "https://storage.example.com/huge_file.zip",
  "status_code": 413,
  "file_size": "500 MB",
  "max_allowed_size": "100 MB"
}
```

### Network Timeout
```json
{
  "error": "Download timeout",
  "url": "https://slow.example.com/large_file.zip",
  "timeout_duration": "300 seconds",
  "bytes_downloaded": "50 MB",
  "total_size": "200 MB"
}
```

### Invalid URL Format
```json
{
  "error": "Invalid URL",
  "url": "not-a-valid-url",
  "message": "The provided URL is not in a valid format"
}
```

### Server Error (500)
```json
{
  "error": "Server error",
  "url": "https://api.example.com/files/report.pdf",
  "status_code": 500,
  "message": "The server encountered an error while processing the request"
}
```

## Advanced Features

### Automatic File Type Detection
The extension automatically detects file types based on:
- **Content-Type Header**: MIME type from server
- **File Extension**: Extension in the URL
- **Content Analysis**: Binary content inspection
- **Magic Numbers**: File signature detection

### Content Validation
- **File Integrity**: Checksum validation when available
- **Size Verification**: Content-Length header validation
- **Format Validation**: File format structure checking
- **Virus Scanning**: Optional security scanning

### Download Optimization
- **Compression Support**: Automatic decompression of gzipped content
- **Range Requests**: Partial content downloads
- **Connection Reuse**: Efficient HTTP connection management
- **Retry Logic**: Automatic retry for failed downloads

## Best Practices

### URL Validation
- Verify URLs are properly formatted and accessible
- Check for HTTPS when downloading sensitive files
- Validate domain and path components
- Test URLs before production use

### File Handling
- Implement appropriate timeout values for different file sizes
- Monitor download progress for large files
- Validate file types and sizes after download
- Implement proper error handling for failed downloads

### Security Considerations
- Only download from trusted sources
- Validate file content after download
- Implement virus scanning for executable files
- Use HTTPS for sensitive file downloads

### Performance Optimization
- Use appropriate timeout settings
- Implement progress monitoring for user feedback
- Consider parallel downloads for multiple files
- Cache frequently accessed files when appropriate

## File Processing

### Post-Download Operations
After successful download, you can:
- **Parse Content**: Extract data from CSV, JSON, XML files
- **Extract Archives**: Unzip downloaded archive files
- **Convert Formats**: Transform files between different formats
- **Validate Content**: Check file integrity and format

### Integration Workflows
- **Chain Operations**: Use downloaded files in subsequent API calls
- **Data Processing**: Process downloaded data files
- **File Storage**: Save files to local or cloud storage
- **Notification**: Send alerts when downloads complete

## Troubleshooting

### Download Failures
1. **Check URL Accessibility**: Verify the URL is reachable
2. **Network Connectivity**: Ensure stable internet connection
3. **Server Status**: Confirm the source server is operational
4. **Authentication**: Check if the URL requires authentication

### Slow Downloads
1. **Network Speed**: Check internet connection speed
2. **Server Performance**: Verify source server response time
3. **File Size**: Consider file size vs. timeout settings
4. **Concurrent Downloads**: Limit simultaneous downloads

### Corrupted Files
1. **Network Issues**: Check for connection interruptions
2. **Server Problems**: Verify source file integrity
3. **Size Mismatch**: Compare downloaded size with expected size
4. **Retry Download**: Attempt download again

### Timeout Issues
1. **Increase Timeout**: Adjust timeout for large files
2. **Check File Size**: Verify file size is reasonable
3. **Network Stability**: Ensure stable connection
4. **Server Response**: Check server response time

## Related Operations

- [GET Response as File](pages/getAsFile.md) - Download API responses as files
- [Basic GET Request](pages/basicGet.md) - Simple data retrieval
- [GET with Headers](pages/getWithHeaders.md) - Authenticated file access
- [Asynchronous GET](pages/asyncGet.md) - Long-running file generation

## Next Steps

After implementing file downloads:
1. Add file validation and integrity checking
2. Implement progress tracking for large downloads
3. Set up automated file processing workflows
4. Consider file caching and storage strategies
