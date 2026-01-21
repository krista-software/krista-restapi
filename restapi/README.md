# REST API Extension for Krista

[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Version](https://img.shields.io/badge/Version-2.0.16-green.svg)](https://github.com/krista-ai/rest-api-extension)
[![Ecosystem](https://img.shields.io/badge/Ecosystem-Development-purple.svg)](https://krista.ai)

A comprehensive integration solution designed to bridge your Krista environment with external applications through RESTful APIs. This powerful extension eliminates the complexity of API integrations while maintaining enterprise-grade security and reliability.

---

## 🚀 Key Features

- **Universal API Connectivity** - Connect to virtually any REST API endpoint (GET, POST, PUT, PATCH, DELETE)
- **OAuth 2.0 Support** - Full OAuth 2.0 flow with automatic token refresh and pre-configured templates
- **Microsoft Integration** - Seamless integration with Microsoft Graph API, Azure, SharePoint, and Teams
- **Google Integration** - Native support for Google Workspace, Cloud Platform, and specialized Google services
- **Multiple Authentication Methods** - Basic Auth, Token-based Auth, and OAuth 2.0
- **File Operations** - Upload and download files with automatic format handling
- **Pagination Support** - Built-in pagination for handling large datasets efficiently
- **Asynchronous Operations** - Event-driven async processing for long-running tasks
- **Enterprise Security** - Encrypted data transmission, secure credential storage, and automated token management
- **Comprehensive Error Handling** - User-friendly, actionable error messages with troubleshooting guidance
- **Three-Layer Architecture** - Clean separation of concerns with Catalog, Service, and Connector layers

---

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage Examples](#-usage-examples)
- [Supported Operations](#-supported-operations)
- [Architecture Overview](#-architecture-overview)
- [API Reference](#-api-reference)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

---

## ⚡ Quick Start

Get started with the REST API Extension in under 5 minutes:

### Prerequisites

- Java 21 or higher
- Krista platform installed and running
- Access to the target REST API endpoint

### Basic GET Request Example

```java
// Simple data retrieval from an API endpoint
URL: https://api.example.com/users
Method: GET

// Response will include:
// - Response Info: HTTP status, headers, timing
// - Response: List of user data
```

### Authenticated POST Request Example

```java
// Create a new resource with authentication
URL: https://api.example.com/users
Method: POST
Headers:
  - Authorization: Bearer your_token_here
  - Content-Type: application/json
Payload:
  {
    "name": "John Doe",
    "email": "john@example.com"
  }
```

For detailed examples, see the [Usage Examples](#-usage-examples) section.

---

## 📦 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/krista-ai/rest-api-extension.git
cd rest-api-extension/restapi
```

### 2. Build the Extension

```bash
./gradlew clean build
```

### 3. Deploy to Krista

```bash
# Copy the built JAR to your Krista extensions directory
cp build/libs/RestAPI-2.0.16.jar $KRISTA_HOME/extensions/
```

### 4. Restart Krista

```bash
# Restart the Krista platform to load the extension
$KRISTA_HOME/bin/krista restart
```

---

## ⚙️ Configuration

### Authentication Setup

The REST API Extension supports three authentication methods:

#### 1. Basic Authentication

Simple username/password authentication for straightforward integrations.

**Configuration Parameters:**
- **Username**: Your account username or API key
- **Password**: Your account password or secret
- **Auth URL**: GET endpoint to validate credentials

**Example:**
```
Username: api_user
Password: secure_password123
Auth URL: https://api.example.com/auth/validate
```

#### 2. Token-Based Authentication

Pre-generated tokens for enhanced security.

**Configuration Parameters:**
- **Token**: Your API token or key
- **Token Type**: Token prefix (Bearer, Token, API-Key)
- **Auth URL**: GET endpoint to validate token

**Example:**
```
Token: sk-1234567890abcdef
Token Type: Bearer
Auth URL: https://api.example.com/user/profile
```

#### 3. OAuth 2.0

Full OAuth 2.0 authorization code flow with automatic token refresh.

**Configuration Parameters:**
- **Client ID**: OAuth application client ID
- **Client Secret**: OAuth application client secret
- **Authorization URL**: OAuth provider's authorization endpoint
- **Token URL**: OAuth provider's token endpoint
- **Scope**: Requested permissions (space-separated)

**Example (Microsoft):**
```
Client ID: your-client-id
Client Secret: your-client-secret
Authorization URL: https://login.microsoftonline.com/common/oauth2/v2.0/authorize
Token URL: https://login.microsoftonline.com/common/oauth2/v2.0/token
Scope: https://graph.microsoft.com/.default
```

For detailed setup guides:
- [Microsoft OAuth Setup](src/main/resources/docs/pages/obtainingClientIDClientSecret.md)
- [Google OAuth Setup](src/main/resources/docs/pages/gettingClientIDAndClientSecret.md)
- [General Authentication Guide](src/main/resources/docs/pages/authentication.md)

---

## 💡 Usage Examples

### Read Operations (GET)

#### Basic GET Request
Retrieve data from an API endpoint without additional parameters.

```java
// Fetch all users
URL: https://jsonplaceholder.typicode.com/users
Method: GET

// Response includes user list with id, name, email, etc.
```

#### GET with Query Parameters
Add URL query parameters for filtering and searching.

```java
// Fetch active users with pagination
URL: https://api.example.com/users
Method: GET
Query Parameters:
  - status: active
  - limit: 10
  - offset: 0
```

#### GET with Custom Headers
Include HTTP headers for authentication and content negotiation.

```java
// Authenticated request with custom headers
URL: https://api.example.com/users
Method: GET
Headers:
  - Authorization: Bearer your_token_here
  - Accept: application/json
  - X-API-Version: v2
```

#### GET with Pagination
Handle large datasets with built-in pagination support.

```java
// Page through 10,000 records with 50 records per page
URL: https://api.example.com/users
Method: GET
Pagination:
  - Page Size: 50
  - Total Records: 10000
```

### Create Operations (POST)

#### Basic POST Request
Create new resources by sending data to API endpoints.

```java
// Create a new user
URL: https://api.example.com/users
Method: POST
Headers:
  - Content-Type: application/json
Payload:
  {
    "name": "Jane Smith",
    "email": "jane@example.com",
    "role": "developer"
  }
```

#### File Upload
Upload files to external APIs.

```java
// Upload a document
URL: https://api.example.com/documents
Method: POST
Headers:
  - Content-Type: multipart/form-data
Payload:
  - file: document.pdf
  - metadata: {"category": "reports"}
```

### Update Operations (PUT/PATCH)

#### Basic Update Request
Modify existing resources using PUT (complete replacement) or PATCH (partial update).

```java
// Update user profile
URL: https://api.example.com/users/123
Method: PATCH
Headers:
  - Content-Type: application/json
Payload:
  {
    "email": "newemail@example.com",
    "role": "senior-developer"
  }
```

### Delete Operations (DELETE)

#### Basic DELETE Request
Remove resources from external systems.

```java
// Delete a user
URL: https://api.example.com/users/123
Method: DELETE
Headers:
  - Authorization: Bearer your_token_here
```

#### Conditional DELETE
Bulk deletion with query parameters.

```java
// Delete all inactive users from before 2023
URL: https://api.example.com/users
Method: DELETE
Query Parameters:
  - status: inactive
  - created_before: 2023-01-01
```

### File Operations

#### Download File
Direct file downloads from external URLs.

```java
// Download a generated report
URL: https://api.example.com/reports/annual-2024.pdf
Method: GET
Output: File (annual-2024.pdf)
```

For more examples, see the [Supported Requests Documentation](src/main/resources/docs/pages/supportedRequests.md).

---

## 🔧 Supported Operations

The REST API Extension provides comprehensive support for all standard HTTP operations:

### 📥 Read Operations (GET)
- **[Basic GET Request](src/main/resources/docs/pages/basicGet.md)** - Simple data retrieval
- **[GET with Query Parameters](src/main/resources/docs/pages/getWithFilters.md)** - Filtering and searching
- **[GET with Custom Headers](src/main/resources/docs/pages/getWithHeaders.md)** - Authentication and versioning
- **[GET with Pagination](src/main/resources/docs/pages/getWithPagination.md)** - Large dataset handling
- **[GET Response as File](src/main/resources/docs/pages/getAsFile.md)** - Download data as files
- **[Asynchronous GET](src/main/resources/docs/pages/asyncGet.md)** - Long-running queries

### 📤 Create Operations (POST)
- **[Basic POST Request](src/main/resources/docs/pages/basicPost.md)** - Simple resource creation
- **[POST with Custom Headers](src/main/resources/docs/pages/postWithHeaders.md)** - Authenticated creation
- **[POST with Query Parameters](src/main/resources/docs/pages/postWithFilters.md)** - Conditional operations
- **[Asynchronous POST](src/main/resources/docs/pages/asyncPost.md)** - Bulk imports and processing

### ✏️ Update Operations (PUT/PATCH)
- **[Basic Update Request](src/main/resources/docs/pages/basicUpdate.md)** - Resource modification
- **[Update with Custom Headers](src/main/resources/docs/pages/updateWithHeaders.md)** - Version control
- **[Update with Query Parameters](src/main/resources/docs/pages/updateWithFilters.md)** - Conditional updates

### 🗑️ Delete Operations (DELETE)
- **[Basic DELETE Request](src/main/resources/docs/pages/basicDelete.md)** - Simple deletion
- **[DELETE with Query Parameters](src/main/resources/docs/pages/deleteWithFilters.md)** - Bulk cleanup
- **[DELETE with Custom Headers](src/main/resources/docs/pages/deleteWithHeaders.md)** - Secure deletion

### 📁 File Operations
- **[Download File from URL](src/main/resources/docs/pages/downloadFile.md)** - Direct file downloads

---


## 🏗️ Architecture Overview

The REST API Extension follows a clean three-layer architecture pattern with clear separation of concerns:

### Layer Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Catalog Layer                           │
│  (ReadArea, WriteArea, UpdateArea, DeleteArea, DownloadArea)│
│  - Domain-specific request handling                         │
│  - Catalog request annotations                              │
│  - Event handling for async operations                      │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     Service Layer                           │
│  (ActionableImpl, HTTPRequest, ReadAction, WriteAction, etc)│
│  - Business logic execution                                 │
│  - HTTP request construction                                │
│  - Response parsing and error handling                      │
│  - Retry logic with exponential backoff                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Connector Layer                          │
│  (ActionableImplProvider, ActionableImplProviderFactory)    │
│  - Client instantiation                                     │
│  - Dependency injection                                     │
│  - Context management                                       │
└─────────────────────────────────────────────────────────────┘
```

### Key Components

#### 1. Catalog Layer
- **Location**: `app.krista.extensions.development.api.rest.catalog`
- **Responsibilities**: Domain operations, event handling, response transformation
- **Components**: ReadArea, WriteArea, UpdateArea, DeleteArea, DownloadArea

#### 2. Service Layer
- **Location**: `app.krista.extensions.development.api.rest.impl`
- **Responsibilities**: Business logic, HTTP client implementation, retry logic
- **Components**: ActionableImpl, HTTPRequest, ReadAction, WriteAction, ModifyAction, RemoveAction

#### 3. Connector Layer
- **Location**: `app.krista.extensions.development.api.rest.connectors`
- **Responsibilities**: Client provider, factory pattern, dependency injection
- **Components**: ActionableImplProvider, ActionableImplProviderFactory

### Authentication Layer
- **Location**: `app.krista.extensions.development.api.rest.auth`
- **Components**: OAuthClient, AccessToken, AuthPayload, AttributeStore
- **Features**: OAuth 2.0 flow, token refresh, credential validation

### Data Access Layer
- **Location**: `app.krista.extensions.development.api.rest.stores`
- **Components**: RefreshTokenStore, RestApiAttributeStore
- **Features**: Token persistence, credential lifecycle management

For detailed architecture documentation, see [Architecture.md](docs/Architecture.md).

---

## 📚 API Reference

### Complete Documentation

- **[Overview](src/main/resources/docs/pages/overview.md)** - Extension capabilities and use cases
- **[Supported Requests](src/main/resources/docs/pages/supportedRequests.md)** - Complete index of all operations
- **[Authentication Guide](src/main/resources/docs/pages/authentication.md)** - Setup and configuration
- **[Troubleshooting Guide](src/main/resources/docs/pages/troubleshooting.md)** - Common issues and solutions
- **[Release Notes](src/main/resources/docs/pages/releaseNotes.md)** - Latest updates and changes

### Platform-Specific Guides

#### Microsoft Integration
- **[Obtaining Client ID and Secret](src/main/resources/docs/pages/obtainingClientIDClientSecret.md)** - Azure AD setup
- **[Connecting Microsoft with REST API Extension](src/main/resources/docs/pages/connectingMicrosoftWithRestApiExtension_.md)** - Step-by-step guide

#### Google Integration
- **[Getting Client ID and Client Secret](src/main/resources/docs/pages/gettingClientIDAndClientSecret.md)** - Google Cloud setup
- **[Connecting Google with REST API Extension](src/main/resources/docs/pages/connectingGoogleWithRestApiExtension.md)** - Step-by-step guide

### Common Parameters

#### Input Parameters
| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| **URL** | Text | Target API endpoint | `https://api.example.com/users` |
| **Headers** | List of Multi-field | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |
| **Query Parameters** | List of Multi-field | URL query parameters | `[{key: "limit", value: "10"}]` |
| **Payload** | Multi-Field | Request body data | `{key: "json", value: "{...}", file: null}` |

#### Output Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| **Response Info** | FreeForm | HTTP response metadata (status, headers, timing) |
| **Response** | List Of FreeForm | Parsed response data |
| **File** | File | Downloaded file content (file operations) |

---

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

### Test Coverage

The REST API Extension maintains high test coverage (95%+) across all components:

- **Unit Tests**: Core business logic and utilities
- **Integration Tests**: End-to-end API operations
- **Authentication Tests**: OAuth flows and token management
- **Error Handling Tests**: Comprehensive error scenarios

### Testing Best Practices

- ✅ No mocks policy - Use real implementations for testing
- ✅ Test all authentication methods (Basic, Token, OAuth)
- ✅ Validate error handling and retry logic
- ✅ Test file upload/download operations
- ✅ Verify pagination and async operations

---

## 🤝 Contributing

We welcome contributions to the REST API Extension! Here's how you can help:

### Reporting Bugs

1. Check existing issues to avoid duplicates
2. Create a new issue with detailed information:
   - Extension version
   - Java version
   - Steps to reproduce
   - Expected vs actual behavior
   - Error messages and logs

### Suggesting Enhancements

1. Open an issue describing the enhancement
2. Explain the use case and benefits
3. Provide examples if applicable

### Development Setup

1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/rest-api-extension.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Make your changes and add tests
5. Run tests: `./gradlew test`
6. Commit your changes: `git commit -m "Add your feature"`
7. Push to your fork: `git push origin feature/your-feature-name`
8. Create a Pull Request

### Coding Standards

- Follow Java 21 best practices
- Maintain test coverage above 95%
- Add JavaDoc comments for public APIs
- Follow the existing code style
- Include unit tests for new features
- Update documentation as needed

### Pull Request Process

1. Ensure all tests pass
2. Update README.md with details of changes if applicable
3. Update documentation in `src/main/resources/docs/pages/`
4. Request review from maintainers
5. Address review feedback
6. Squash commits before merging

For detailed guidelines, see [CONTRIBUTING.md](CONTRIBUTING.md) (coming soon).

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0 (GPLv3)**.

### Key Points

- ✅ **Freedom to use**: Use the software for any purpose
- ✅ **Freedom to study**: Access and modify the source code
- ✅ **Freedom to share**: Distribute copies of the software
- ✅ **Freedom to improve**: Distribute modified versions

### License Requirements

- All source files include GPLv3 license headers
- Derivative works must also be licensed under GPLv3
- Source code must be made available when distributing

For the full license text, see [LICENSE](LICENSE) or visit [https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html).

---

## 🆘 Support

### Documentation Resources

- **[Overview](src/main/resources/docs/pages/overview.md)** - Extension capabilities and features
- **[Troubleshooting Guide](src/main/resources/docs/pages/troubleshooting.md)** - Common issues and solutions
- **[Authentication Guide](src/main/resources/docs/pages/authentication.md)** - Setup and configuration help

### Getting Help

1. **Check the Documentation**: Review the comprehensive guides in `src/main/resources/docs/pages/`
2. **Search Issues**: Look for similar issues in the GitHub issue tracker
3. **Create an Issue**: If you can't find a solution, create a new issue with:
   - Clear description of the problem
   - Steps to reproduce
   - Extension and Java versions
   - Error messages and logs
4. **Contact Support**: For enterprise support, contact your Krista representative

### Common Issues

- **Authentication Failures**: Check token validity and permissions
- **Rate Limiting**: Implement proper backoff strategies
- **Timeout Errors**: Adjust timeout settings for large operations
- **Data Validation**: Verify parameter formats and required fields

---

## 🌟 Acknowledgments

Built with ❤️ by the Krista development team.

Special thanks to all contributors who have helped improve this extension.

---

## 📊 Project Status

- **Version**: 2.0.16
- **Status**: Production Ready
- **Java Version**: 21
- **License**: GPLv3
- **Ecosystem**: Development

---

**Ready to get started?** Follow the [Quick Start](#-quick-start) guide or explore the [Usage Examples](#-usage-examples) to begin integrating with REST APIs today!
