# Supported API Requests

## Overview

The Krista REST API Extension provides comprehensive support for HTTP operations, enabling seamless integration with external APIs. This page serves as a complete index of all available request types and operations.

## Request Categories

The extension organizes API operations into logical categories, each with detailed documentation and examples:

## 📥 Read Operations (GET)

Retrieve data from external APIs with various configuration options.

### [Basic GET Request](pages/basicGet.md)
Simple data retrieval from API endpoints without additional parameters.
- **Use Case**: Fetch all records, test connectivity, access public endpoints
- **Example**: `GET https://api.example.com/users`

### [GET with Query Parameters](pages/getWithFilters.md)
Add URL query parameters for filtering, searching, and pagination.
- **Use Case**: Filter records, search data, implement pagination
- **Example**: `GET https://api.example.com/users?status=active&limit=10`

### [GET with Custom Headers](pages/getWithHeaders.md)
Include HTTP headers for authentication, content negotiation, and API versioning.
- **Use Case**: Authenticated requests, content type negotiation, API versioning
- **Example**: `GET https://api.example.com/users` with `Authorization: Bearer token`

### [GET with Pagination](pages/getWithPagination.md)
Built-in pagination support for handling large datasets efficiently.
- **Use Case**: Process large datasets, implement user-friendly browsing
- **Example**: Page through 10,000 records with 50 records per page

### [GET Response as File](pages/getAsFile.md)
Download API response data directly as files for offline processing.
- **Use Case**: Export data, download reports, retrieve binary files
- **Example**: Download CSV export or PDF report

### [Asynchronous GET Operations](pages/asyncGet.md)
Handle long-running data retrieval tasks with task tracking.
- **Use Case**: Complex queries, large data processing, heavy computations
- **Example**: Generate annual report with progress tracking

## 📤 Create Operations (POST)

Create new resources in external systems with various configuration options.

### [Basic POST Request](pages/basicPost.md)
Simple resource creation by sending data to API endpoints.
- **Use Case**: Create users, submit forms, add records, upload files
- **Example**: `POST https://api.example.com/users` with user data

### [POST with Custom Headers](pages/postWithHeaders.md)
Include HTTP headers for authentication, tracking, and content negotiation.
- **Use Case**: Authenticated creation, audit trails, custom metadata
- **Example**: Create user with authentication and tracking headers

### [POST with Query Parameters](pages/postWithFilters.md)
Combine resource creation with URL parameters for conditional operations.
- **Use Case**: Conditional creation, bulk operations, processing directives
- **Example**: Create user with notification and validation options

### [Asynchronous POST Operations](pages/asyncPost.md)
Handle long-running resource creation tasks with progress tracking.
- **Use Case**: Bulk imports, file processing, complex validations
- **Example**: Import 10,000 users with progress monitoring

## ✏️ Update Operations (PUT/PATCH)

Modify existing resources with various update strategies and options.

### [Basic Update Request](pages/basicUpdate.md)
Simple resource modification using PUT (complete replacement) or PATCH (partial update).
- **Use Case**: Update profiles, modify settings, change status
- **Example**: `PATCH https://api.example.com/users/123` with updated fields

### [Update with Custom Headers](pages/updateWithHeaders.md)
Include headers for authentication, versioning, and concurrency control.
- **Use Case**: Authenticated updates, version control, audit trails
- **Example**: Update user with ETag-based optimistic locking

### [Update with Query Parameters](pages/updateWithFilters.md)
Combine updates with URL parameters for conditional and bulk operations.
- **Use Case**: Conditional updates, bulk modifications, validation options
- **Example**: Update user with strict validation and notifications

## 🗑️ Delete Operations (DELETE)

Remove resources from external systems with various deletion strategies.

### [Basic DELETE Request](pages/basicDelete.md)
Simple resource deletion from API endpoints.
- **Use Case**: Remove users, delete records, clean up resources
- **Example**: `DELETE https://api.example.com/users/123`

### [DELETE with Query Parameters](pages/deleteWithFilters.md)
Conditional and bulk deletion operations using URL parameters.
- **Use Case**: Bulk cleanup, conditional deletion, filtered removal
- **Example**: Delete all inactive users from before 2023

### [DELETE with Custom Headers](pages/deleteWithHeaders.md)
Secure deletion with authentication, audit trails, and compliance headers.
- **Use Case**: Authenticated deletion, compliance logging, admin overrides
- **Example**: GDPR-compliant user data deletion with audit trail

## 📁 File Operations

Specialized operations for handling file downloads and transfers.

### [Download File from URL](pages/downloadFile.md)
Direct file downloads from external URLs without complex authentication.
- **Use Case**: Download reports, retrieve shared files, access public resources
- **Example**: Download generated PDF report or CSV export

## 🔧 Common Parameters

All operations share these standard parameter types:

### Input Parameters
| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| **URL** | Text | Target API endpoint | `https://api.example.com/users` |
| **Headers** | List of Multi-field | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |
| **Query Parameters** | List of Multi-field | URL query parameters | `[{key: "limit", value: "10"}]` |
| **Payload** | Multi-Field | Request body data (POST/PUT/PATCH) | `{key: "json", value: "{...}", file: null}` |

### Output Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| **Response Info** | FreeForm | HTTP response metadata (status, headers, timing) |
| **Response** | List Of FreeForm | Parsed response data |
| **File** | File | Downloaded file content (file operations) |

## 🚀 Quick Start Guide

### 1. Choose Your Operation Type
- **GET**: Retrieve data from APIs
- **POST**: Create new resources
- **PUT/PATCH**: Update existing resources
- **DELETE**: Remove resources
- **File Download**: Get files directly

### 2. Select Specific Operation
Each category has multiple variants:
- **Basic**: Simple operations without extra parameters
- **With Headers**: Add authentication and custom headers
- **With Parameters**: Include URL query parameters
- **Advanced**: Pagination, async operations, file handling

### 3. Configure Parameters
- Set the target URL
- Add authentication headers if required
- Include query parameters for filtering
- Provide request body for POST/PUT/PATCH operations

### 4. Handle Responses
- Check Response Info for status and metadata
- Process Response data as needed
- Handle errors appropriately
- Save files for file operations

## 📋 Operation Comparison

| Operation Type | Authentication | Query Params | Request Body | File Support | Async Support |
|----------------|----------------|--------------|--------------|--------------|---------------|
| **Basic GET** | Optional | Optional | No | No | No |
| **GET with Headers** | Yes | Optional | No | No | No |
| **GET with Filters** | Optional | Yes | No | No | No |
| **GET Pagination** | Optional | Yes | No | No | No |
| **GET as File** | Optional | Optional | No | Yes | No |
| **Async GET** | Yes | Yes | No | No | Yes |
| **Basic POST** | Optional | No | Yes | Yes | No |
| **POST with Headers** | Yes | No | Yes | Yes | No |
| **POST with Filters** | Optional | Yes | Yes | Yes | No |
| **Async POST** | Yes | Yes | Yes | Yes | Yes |
| **Basic Update** | Optional | No | Yes | Yes | No |
| **Update with Headers** | Yes | No | Yes | Yes | No |
| **Update with Filters** | Optional | Yes | Yes | Yes | No |
| **Basic DELETE** | Optional | No | No | No | No |
| **DELETE with Headers** | Yes | No | No | No | No |
| **DELETE with Filters** | Optional | Yes | No | No | No |
| **File Download** | No | No | No | Yes | No |

## 🔍 Best Practices

### Authentication
- Use appropriate authentication method for your API
- Include authentication headers for protected endpoints
- Implement proper token refresh mechanisms
- Store credentials securely

### Error Handling
- Always check Response Info for status codes
- Implement retry logic for transient failures
- Handle rate limiting gracefully
- Provide meaningful error messages

### Performance
- Use pagination for large datasets
- Implement appropriate timeouts
- Consider async operations for long-running tasks
- Monitor API usage and response times

### Security
- Use HTTPS for all API communications
- Validate input parameters
- Implement proper access controls
- Log operations for audit trails

## 🛠️ Common Use Cases

### Data Integration
- **Sync user data** between systems using GET and POST operations
- **Update product catalogs** with PUT/PATCH operations
- **Clean up old records** with DELETE operations
- **Export reports** using GET as File operations

### Workflow Automation
- **Trigger actions** based on external events using POST
- **Monitor progress** with asynchronous operations
- **Validate data** before processing with conditional operations
- **Notify stakeholders** using webhook integrations

### Data Management
- **Bulk operations** for large datasets using pagination
- **Conditional processing** with query parameters
- **File handling** for documents and media
- **Backup and recovery** with file download operations

## 📚 Additional Resources

### Setup and Configuration
- [Authentication Setup](pages/authentication.md) - Configure API authentication methods
- [Microsoft OAuth Guide](pages/obtainingClientIDClientSecret.md) - Set up Microsoft integration
- [Google OAuth Guide](pages/gettingClientIDAndClientSecret.md) - Set up Google integration

### Getting Started
- [Overview](pages/overview.md) - Extension capabilities and use cases
- [Release Notes](pages/releaseNotes.md) - Latest updates and changes

## 🆘 Support and Troubleshooting

### Common Issues
1. **Authentication Failures**: Check token validity and permissions
2. **Rate Limiting**: Implement proper backoff strategies
3. **Timeout Errors**: Adjust timeout settings for large operations
4. **Data Validation**: Verify parameter formats and required fields

### Getting Help
- Review the specific operation documentation for detailed examples
- Check the authentication guides for setup issues
- Refer to the overview for general capabilities and limitations
- Contact your system administrator for environment-specific issues

---

**Ready to get started?** Choose an operation type above and follow the detailed documentation for step-by-step implementation guidance.




