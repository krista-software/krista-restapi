# Basic POST Request

## Overview

The Basic POST request creates new resources by sending data to an API endpoint. This operation is fundamental for adding new records, submitting forms, and creating resources in external systems.

## Use Cases

- Create new user accounts
- Submit form data
- Add new records to databases
- Upload content and files
- Trigger server-side actions
- Initialize new resources

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target API endpoint | `https://api.example.com/users` |
| Payload | Multi-Field | Yes | Request body data | `{key: "json", value: "{\"name\": \"John\"}", file: null}` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Created resource data |

## Payload Configuration

The payload field supports multiple content types and file uploads:

### Payload Structure
| Field | Type | Description | Example |
|-------|------|-------------|---------|
| **key** | Text | Content type (defaults to "json") | `json`, `xml`, `form`, `text` |
| **value** | Text | Request body content | `{"name": "John", "email": "john@example.com"}` |
| **file** | File | Single file upload (optional) | `profile_image.jpg` |

### Content Types

#### JSON Content
```
key: "json"
value: {"name": "John Doe", "email": "john@example.com", "role": "user"}
```

#### XML Content
```
key: "xml"
value: <user><name>John Doe</name><email>john@example.com</email></user>
```

#### Form Data
```
key: "form"
value: name=John+Doe&email=john%40example.com&role=user
```

#### Plain Text
```
key: "text"
value: Simple text content for the request body
```

## HTTP Request Format

### JSON Request
```http
POST {URL} HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 67

{
  "name": "John Doe",
  "email": "john@example.com",
  "role": "user"
}
```

### File Upload
```http
POST {URL} HTTP/1.1
Host: api.example.com
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="file"; filename="document.pdf"
Content-Type: application/pdf

[Binary file content]
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```

## Example Usage

### Create New User

**Configuration:**
- URL: `https://api.example.com/users`
- Payload:
  ```
  key: "json"
  value: {
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "department": "Engineering",
    "role": "developer"
  }
  ```

**Generated Request:**
```http
POST https://api.example.com/users
Content-Type: application/json

{
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "department": "Engineering",
  "role": "developer"
}
```

**Expected Response:**
```json
{
  "id": 12345,
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "department": "Engineering",
  "role": "developer",
  "created_at": "2024-01-15T10:30:00Z",
  "status": "active"
}
```

### Submit Contact Form

**Configuration:**
- URL: `https://api.example.com/contact`
- Payload:
  ```
  key: "json"
  value: {
    "name": "John Smith",
    "email": "john@company.com",
    "subject": "Product Inquiry",
    "message": "I'm interested in learning more about your services."
  }
  ```

**Expected Response:**
```json
{
  "id": "contact_67890",
  "status": "received",
  "message": "Thank you for your inquiry. We'll respond within 24 hours.",
  "reference_number": "REF-2024-0115-001"
}
```

### Upload Document

**Configuration:**
- URL: `https://api.example.com/documents`
- Payload:
  ```
  key: "json"
  value: {
    "title": "Project Proposal",
    "category": "business",
    "description": "Q1 2024 project proposal document"
  }
  file: project_proposal.pdf
  ```

**Expected Response:**
```json
{
  "id": "doc_98765",
  "title": "Project Proposal",
  "filename": "project_proposal.pdf",
  "size": 2048576,
  "upload_url": "https://storage.example.com/docs/doc_98765.pdf",
  "status": "uploaded"
}
```

## Response Handling

### Successful Creation (201 Created)
```json
{
  "id": 12345,
  "name": "New Resource",
  "created_at": "2024-01-15T10:30:00Z",
  "status": "active",
  "links": {
    "self": "https://api.example.com/resources/12345",
    "edit": "https://api.example.com/resources/12345/edit"
  }
}
```

### Accepted for Processing (202 Accepted)
```json
{
  "id": "task_abc123",
  "status": "processing",
  "message": "Resource creation initiated",
  "estimated_completion": "2024-01-15T10:35:00Z",
  "status_url": "https://api.example.com/tasks/task_abc123"
}
```

### Validation Success (200 OK)
```json
{
  "message": "Data submitted successfully",
  "reference_id": "ref_456789",
  "next_steps": "Your request will be reviewed within 2 business days"
}
```

## Error Handling

### Validation Errors (400 Bad Request)
```json
{
  "error": "Validation failed",
  "details": [
    {
      "field": "email",
      "message": "Invalid email format"
    },
    {
      "field": "name",
      "message": "Name is required"
    }
  ]
}
```

### Duplicate Resource (409 Conflict)
```json
{
  "error": "Resource already exists",
  "message": "A user with this email already exists",
  "existing_resource": {
    "id": 9876,
    "email": "alice@example.com"
  }
}
```

### Server Error (500 Internal Server Error)
```json
{
  "error": "Internal server error",
  "message": "Unable to process request at this time",
  "reference_id": "err_123456",
  "support_contact": "support@example.com"
}
```

### File Upload Errors
```json
{
  "error": "File upload failed",
  "details": {
    "file_size": "File exceeds maximum size limit of 10MB",
    "file_type": "Only PDF, DOC, and DOCX files are allowed"
  }
}
```

## Best Practices

### Data Validation
- Validate required fields before sending
- Use appropriate data types and formats
- Implement client-side validation for better UX
- Handle server-side validation errors gracefully

### Content Types
- Use JSON for structured data
- Use form data for simple key-value pairs
- Use multipart for file uploads
- Specify correct Content-Type headers

### Error Handling
- Check response status codes
- Parse and display validation errors
- Implement retry logic for transient failures
- Log errors for debugging and monitoring

### Security
- Validate and sanitize input data
- Use HTTPS for sensitive data transmission
- Implement proper authentication
- Avoid exposing sensitive data in logs

## File Upload Guidelines

### Supported File Types
- Documents: PDF, DOC, DOCX, TXT
- Images: JPG, PNG, GIF, SVG
- Archives: ZIP, RAR, TAR
- Data: CSV, JSON, XML

### File Size Limits
- Small files: < 1MB (images, documents)
- Medium files: 1-10MB (presentations, spreadsheets)
- Large files: 10-100MB (videos, large datasets)

### Upload Best Practices
- Validate file types and sizes client-side
- Implement progress indicators for large uploads
- Handle upload failures gracefully
- Provide clear error messages for rejected files

## Performance Optimization

### Request Size
- Keep payloads as small as possible
- Use compression for large text data
- Consider pagination for bulk operations
- Implement streaming for large file uploads

### Response Handling
- Parse only necessary response data
- Implement appropriate timeouts
- Handle large responses efficiently
- Cache frequently used data

## Troubleshooting

### Request Fails
1. Verify URL is correct and accessible
2. Check payload format and content type
3. Validate required fields are present
4. Confirm authentication if required

### Validation Errors
1. Review API documentation for field requirements
2. Check data types and formats
3. Validate required vs. optional fields
4. Test with minimal valid payload first

### File Upload Issues
1. Check file size and type restrictions
2. Verify multipart encoding is correct
3. Confirm file is not corrupted
4. Test with smaller files first

## Related Operations

- [POST with Headers](pages/postWithHeaders.md) - Add authentication and custom headers
- [POST with Query Parameters](pages/postWithFilters.md) - Include URL parameters
- [Asynchronous POST](pages/asyncPost.md) - Handle long-running operations
- [Authentication Setup](pages/authentication.md) - Configure API authentication

## Next Steps

After implementing basic POST requests:
1. Add authentication headers for secure endpoints
2. Implement proper error handling and validation
3. Set up file upload capabilities
4. Consider asynchronous operations for long-running tasks
