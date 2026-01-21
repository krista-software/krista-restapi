# REST API Extension - Architecture Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Three-Layer Architecture](#three-layer-architecture)
3. [Design Patterns](#design-patterns)
4. [Component Interactions](#component-interactions)
5. [Technology Stack](#technology-stack)
6. [Dependencies](#dependencies)
7. [Extension Points](#extension-points)
8. [Package Structure](#package-structure)

---

## System Overview

The REST API Extension is a comprehensive Java-based integration framework that enables universal connectivity to REST APIs within the Krista platform. It provides a robust, production-ready solution for consuming external REST services with support for multiple authentication mechanisms, file handling, pagination, and asynchronous processing.

### Key Capabilities
- **Universal REST API Connectivity**: Support for all standard HTTP methods (GET, POST, PUT, PATCH, DELETE)
- **Multi-Authentication Support**: Basic Auth, Token-based Auth, and OAuth 2.0
- **File Operations**: Upload/download with automatic format conversion and compression
- **Pagination**: Client-side pagination with configurable page size and index
- **Event-Driven Processing**: Asynchronous request handling with event callbacks
- **Resilience**: Automatic retry mechanism with exponential backoff (max 2 retries)
- **Extensibility**: Custom headers, query parameters, and payload support

### Architecture Principles
- **Separation of Concerns**: Clear boundaries between catalog, service, and connector layers
- **Dependency Injection**: HK2-based DI for loose coupling and testability
- **Fail-Fast Validation**: Early input validation with user-friendly error messages
- **Resource Management**: Proper lifecycle management with PreDestroy hooks
- **Connection Pooling**: Reusable HTTP client instances for optimal performance

---

## Three-Layer Architecture

The extension follows a clean three-layer architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────┐
│                      CATALOG LAYER                              │
│  (User-Facing API - Krista Domain Requests)                     │
├─────────────────────────────────────────────────────────────────┤
│  DownloadArea  │  ReadArea  │  WriteArea  │  UpdateArea  │      │
│                │            │             │             │       │
│  DeleteArea    │            │             │             │       │
└────────┬────────────────────┬─────────────┬─────────────┬───────┘
         │                    │             │             │
         ▼                    ▼             ▼             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│  (Business Logic - HTTP Operations)                             │
├─────────────────────────────────────────────────────────────────┤
│  ActionableImpl  │  ReadAction  │  WriteAction  │  ModifyAction │
│                  │              │               │               │
│  RemoveAction    │  DownloadAction  │  AuthHelper              │
└────────┬─────────────────────┬──────────────────┬───────────────┘
         │                     │                  │
         ▼                     ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    CONNECTOR LAYER                              │
│  (HTTP Client & Authentication)                                 │
├─────────────────────────────────────────────────────────────────┤
│  HTTPRequest  │  OAuthClient  │  KristaMediaClient  │           │
│               │               │                     │           │
│  ActionableImplProvider  │  ActionableImplProviderFactory       │
└─────────────────────────────────────────────────────────────────┘
         │
         ▼
    [External REST APIs]
```

### 1. Catalog Layer
**Location**: `app.krista.extensions.development.api.rest.catalog.*`

**Purpose**: Provides user-facing domain requests that map to Krista's catalog system.

**Components**:
- **DownloadArea**: File download operations from REST endpoints
- **ReadArea**: HTTP GET operations with filtering, headers, and pagination
- **WriteArea**: HTTP POST operations for creating resources
- **UpdateArea**: HTTP PUT/PATCH operations for modifying resources
- **DeleteArea**: HTTP DELETE operations for removing resources

**Responsibilities**:
- Input validation and sanitization
- Request orchestration and delegation to service layer
- Response formatting for Krista platform
- Event-driven asynchronous processing (ExecutorService)
- Resource lifecycle management (PreDestroy)

**Key Features**:
- Annotated with `@Domain` for Krista catalog integration
- Supports both synchronous and asynchronous execution modes
- Provides user-friendly error messages via ErrorMessages utility
- Implements PreDestroy for proper cleanup of thread pools

### 2. Service Layer
**Location**: `app.krista.extensions.development.api.rest.impl.*`

**Purpose**: Implements core business logic for HTTP operations and request processing.

**Components**:

#### ActionableImpl (Core Service)
- Central service for executing HTTP requests
- Manages retry logic with exponential backoff
- Handles response parsing and conversion
- Supports pagination with client-side filtering
- File response handling with media client integration

**Key Methods**:
- `executeApiRequest()`: Main request execution with retry logic
- `getApiResponseFile()`: File download and conversion
- `executeCustomPaginatedApiRequest()`: Client-side pagination
- `testConnection()`: Connection validation

#### Action Classes (Specialized Services)
- **ReadAction**: GET request operations with pagination and file responses
- **WriteAction**: POST request operations with JSON and multipart support
- **ModifyAction**: PUT/PATCH operations for resource updates
- **RemoveAction**: DELETE operations for resource removal
- **DownloadAction**: Specialized file download with format detection

**Responsibilities**:
- HTTP request construction via HTTPRequest builder
- Response parsing and transformation
- Error handling with ApiExceptionHandler
- Retry mechanism (max 2 retries with 1s delay)
- File conversion via KristaMediaClient
- Authentication credential management

**Configuration**:
- HTTP timeout: 300 seconds
- Max retries: 2
- Retry delay: 1000ms
- Max response size: 4MB

### 3. Connector Layer
**Location**: `app.krista.extensions.development.api.rest.connectors.*`, `auth.*`, `util.*`

**Purpose**: Handles low-level HTTP communication and authentication.

**Components**:

#### HTTPRequest (Request Builder)
- Constructs OkHttp Request objects
- Applies authentication headers (Basic, Token, OAuth)
- Handles query parameters and custom headers
- Supports JSON and multipart/form-data payloads
- File upload with automatic Krista file conversion

**Authentication Support**:
```
┌─────────────────────────────────────────────────────────┐
│              Authentication Flow                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Basic Auth ──────► Base64 Encoding ──► Authorization  │
│                                          Header         │
│                                                         │
│  Token Auth ──────► Bearer Token ─────► Authorization  │
│                                          Header         │
│                                                         │
│  OAuth 2.0  ──────► Refresh Token ────► Access Token   │
│                     (from Store)        (via OAuthClient)
│                                          │              │
│                                          ▼              │
│                                     Authorization       │
│                                     Header              │
└─────────────────────────────────────────────────────────┘
```

#### OAuthClient
- OAuth 2.0 authorization flow management
- Access token generation and refresh
- Token revocation support
- Authorization URL generation with state parameter
- Supports offline access with refresh tokens

**OAuth Flow**:
1. Generate authorization URL with client credentials
2. User authorizes via callback URL
3. Exchange authorization code for access/refresh tokens
4. Store refresh token in RefreshTokenStore
5. Refresh access token automatically on expiration

#### KristaMediaClient
- Bidirectional file conversion (Java ↔ Krista)
- Automatic compression of unsupported formats to ZIP
- Temporary file management and cleanup
- Integration with Krista FileRepository

#### ActionableImplProvider & Factory
- Factory pattern for creating ActionableImpl instances
- User context management (admin vs. user execution)
- Refresh token retrieval and validation
- Authorization exception handling

**Storage Components**:
- **RefreshTokenStore**: Persistent storage for OAuth refresh tokens
- **RestApiAttributeStore**: Configuration and credential storage
- **AttributeStore**: Generic key-value store for authentication data

---

## Design Patterns

### 1. CQRS (Command Query Responsibility Segregation)

The architecture separates read and write operations into distinct areas:

**Query Operations** (ReadArea, DownloadArea):
- HTTP GET requests
- No side effects on server state
- Catalog request type: `QUERY_SYSTEM`
- Optimized for data retrieval and pagination

**Command Operations** (WriteArea, UpdateArea, DeleteArea):
- HTTP POST, PUT, PATCH, DELETE requests
- Modify server state
- Catalog request type: `CHANGE_SYSTEM`
- Support for complex payloads and file uploads

**Benefits**:
- Clear separation of concerns
- Independent scaling of read/write operations
- Simplified testing and maintenance
- Better alignment with REST semantics

### 2. Template Method Pattern

The Action classes follow a template method pattern:

```java
// Template in ActionableImpl
public Map<String, Object> executeApiRequest(url, method, params, headers, payload) {
    while (retryCount < MAX_RETRIES) {
        try {
            response = getResponse(url, method, params, headers, payload);
            if (response.isSuccessful()) {
                return createApiResponseDetails(response);
            }
        } catch (IOException e) {
            retryCount++;
            if (retryCount >= MAX_RETRIES) {
                return createApiErrorResponse(url, e);
            }
            Thread.sleep(RETRY_DELAY_MS);
        }
    }
}

// Concrete implementations in Action classes
public class ReadAction {
    public Map<String, Object> get(url, params, headers) {
        return actionable.executeApiRequest(url, HTTP_GET, params, headers, Map.of());
    }
}
```

**Benefits**:
- Consistent retry logic across all operations
- Centralized error handling
- Easy to extend with new HTTP methods
- Reduced code duplication

### 3. Fail-Fast Validation

Input validation occurs at the earliest possible point:

**Validation Layers**:
1. **Catalog Layer**: Field-level validation via `@Field` annotations
2. **Service Layer**: Business logic validation (URL format, method type)
3. **Connector Layer**: Authentication credential validation

**Example**:
```java
// HTTPRequest validation
private boolean isUrlValid(String url) {
    if (url == null || url.isEmpty() || url.isBlank())
        return false;
    try {
        java.net.URI.create(url).toURL();
        return true;
    } catch (Exception e) {
        LOGGER.error("Invalid URL format: {}", url);
        return false;
    }
}
```

**Error Message Strategy**:
- User-friendly messages via ErrorMessages utility
- Actionable recommendations for resolution
- Detailed logging for troubleshooting
- HTTP status code-specific guidance

### 4. Factory Pattern

ActionableImplProviderFactory creates provider instances:

```java
ActionableImplProvider provider = factory.create(restApiAttributes);
ActionableImpl client = provider.getRestClient();
```

**Benefits**:
- Encapsulates complex object creation
- Manages dependencies and configuration
- Supports different execution contexts (user vs. admin)

### 5. Dependency Injection (HK2)

All components use constructor injection:

```java
@Inject
public ReadAction(AttributeStore attributeStore,
                  RefreshTokenStore refreshTokenStore,
                  Invoker invoker,
                  KristaMediaClient kristaMediaClient) {
    // Dependencies injected by HK2
}
```

**Benefits**:
- Loose coupling between components
- Easy unit testing with mocks
- Clear dependency graph
- Lifecycle management by container

---

## Component Interactions

### Data Flow: GET Request with OAuth

```
User Request
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ ReadArea.get(url)                                           │
│   - Validates input                                         │
│   - Delegates to ReadAction                                 │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ ReadAction.get(url, params, headers)                        │
│   - Retrieves ActionableImpl instance                       │
│   - Calls executeApiRequest()                               │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ ActionableImpl.executeApiRequest()                          │
│   - Retry loop (max 2 retries)                             │
│   - Calls getResponse()                                     │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ HTTPRequest.create()                                        │
│   - Builds OkHttp Request                                   │
│   - Retrieves refresh token from RefreshTokenStore         │
│   - Calls OAuthClient.refreshAccessToken()                 │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ OAuthClient.refreshAccessToken(refreshToken)               │
│   - Exchanges refresh token for access token               │
│   - Returns AccessToken object                             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ HTTPRequest adds Authorization header                       │
│   - "Authorization: Bearer {accessToken}"                   │
│   - Adds custom headers and query parameters               │
│   - Returns complete Request object                         │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ OkHttpClient.newCall(request).execute()                    │
│   - Sends HTTP request to external API                     │
│   - Returns Response object                                 │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ ActionableImpl.convertResponseToMappedList()               │
│   - Parses JSON response                                    │
│   - Validates response size (max 4MB)                       │
│   - Returns Map<String, Object>                             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ ReadArea returns response to user                           │
│   - Response Info: FreeForm (status, message)              │
│   - Response: List<Composite> (data)                        │
└─────────────────────────────────────────────────────────────┘
```

### Data Flow: POST Request with File Upload

```
User Request (with file)
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ WriteArea.post(url, payload)                                │
│   - payload contains: { key, value, file }                  │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ WriteAction.post(url, params, headers, payload)             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ HTTPRequest.create()                                        │
│   - Detects file in payload                                 │
│   - Calls KristaMediaClient.toJavaFile()                    │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ KristaMediaClient.toJavaFile(kristaFile)                    │
│   - Retrieves file from FileRepository                      │
│   - Converts to java.io.File                                │
│   - Returns File object                                     │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ HTTPRequest builds multipart request                        │
│   - Creates MultipartBody.Builder                           │
│   - Adds file part with proper content type                 │
│   - Adds other form fields                                  │
│   - Returns Request with multipart/form-data                │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
                  [HTTP POST to API]
                         │
                         ▼
                  [Response Processing]
```

### Asynchronous Event Processing

```
User Request (async)
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ ReadArea.getAsync(url, params, headers)                     │
│   - Generates unique taskId (UUID)                          │
│   - Submits task to ExecutorService                         │
│   - Returns taskId immediately                              │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│ ExecutorService.submit(() -> {                              │
│     Map<String, Object> result = readable.get(url, ...);    │
│     FreeForm freeForm = new FreeForm();                     │
│     freeForm.put("Data", ..., result);                      │
│     eventHandler.handleEvent(taskId, freeForm);             │
│ })                                                          │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
                  [Event Callback]
                         │
                         ▼
              [User receives notification]
```

---

## Technology Stack

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Primary programming language |
| **Gradle** | 8.x | Build automation and dependency management |
| **HK2** | 3.0.6 | Dependency injection framework |
| **OkHttp** | 4.12.0 | HTTP client for REST communication |
| **Jackson** | 2.18.2 | JSON serialization/deserialization |
| **Jersey** | 2.41 | JAX-RS implementation for REST endpoints |
| **SLF4J** | - | Logging facade |

### Krista Platform APIs

| Component | Version | Purpose |
|-----------|---------|---------|
| **krista-apis** | 1.0.118 | Core Krista platform APIs |
| **extension-impl-anno-processors** | 1.0.118 | Annotation processors for extensions |

### Testing & Quality

| Tool | Version | Purpose |
|------|---------|---------|
| **JUnit Jupiter** | 5.10.0 | Unit testing framework |
| **Mockito** | 5.5.0 | Mocking framework for tests |
| **JaCoCo** | 0.8.12 | Code coverage analysis |
| **SonarQube** | 6.3.1.5724 | Code quality and security analysis |

### Build & UI

| Tool | Version | Purpose |
|------|---------|---------|
| **Node.js** | 24.2.0 | JavaScript runtime for UI build |
| **npm** | 10.7.0 | Package manager for UI dependencies |
| **Yarn** | 1.22.17 | Alternative package manager |

---

## Dependencies

### Runtime Dependencies

```gradle
dependencies {
    // Krista Platform
    implementation 'app.krista:krista-apis:1.0.118'

    // Dependency Injection
    implementation 'org.glassfish.hk2:hk2-api:3.0.6'

    // HTTP Client
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'

    // JSON Processing
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.18.2'

    // JAX-RS & Multipart
    implementation 'org.glassfish.jersey.media:jersey-media-multipart:2.41'
}
```

### Annotation Processing

```gradle
annotationProcessor 'app.krista:extension-impl-anno-processors:1.0.118'
```

This processor generates metadata for:
- `@Extension` - Extension configuration
- `@Domain` - Catalog domain definitions
- `@CatalogRequest` - Request method metadata
- `@Field` - Input/output field definitions

### Dependency Graph

```
RestApiExtension
    │
    ├─► krista-apis (Platform Integration)
    │   ├─► Extension Framework
    │   ├─► Catalog System
    │   ├─► File Repository
    │   └─► Event Handling
    │
    ├─► HK2 (Dependency Injection)
    │   ├─► Service Locator
    │   ├─► Lifecycle Management
    │   └─► @Inject Support
    │
    ├─► OkHttp (HTTP Client)
    │   ├─► Connection Pooling
    │   ├─► Request/Response Handling
    │   └─► Interceptor Support
    │
    ├─► Jackson (JSON Processing)
    │   ├─► ObjectMapper
    │   ├─► Type Conversion
    │   └─► Streaming API
    │
    └─► Jersey (JAX-RS)
        ├─► REST Endpoints
        ├─► Multipart Support
        └─► Client API
```

---

## Extension Points

The architecture provides several extension points for customization:

### 1. Custom Authentication Types

Add new authentication mechanisms by extending the authentication layer:

```java
// Add new AuthType to AuthPayload
public enum AuthType {
    Basic,
    OAuth,
    Token,
    CustomAuth  // New type
}

// Implement authentication logic in HTTPRequest
private void applyAuthentication(Request.Builder builder) {
    switch (authType) {
        case CustomAuth:
            // Custom authentication logic
            break;
    }
}
```

### 2. Custom Response Transformers

Extend ActionableImpl to add custom response processing:

```java
public class CustomActionableImpl extends ActionableImpl {
    @Override
    protected Map<String, Object> createApiResponseDetails(
            ArrayList<LinkedHashMap<Object, Object>> response,
            FreeForm freeForm) {
        // Custom transformation logic
        return super.createApiResponseDetails(response, freeForm);
    }
}
```

### 3. Custom Catalog Areas

Create new catalog areas for specialized operations:

```java
@Domain(id = "custom-domain", name = "Custom Operations")
public class CustomArea {

    @Inject
    private CustomAction customAction;

    @CatalogRequest(
        id = "custom-request",
        name = "Custom Operation",
        area = "Custom",
        type = CatalogRequest.Type.QUERY_SYSTEM
    )
    public Map<String, Object> customOperation(
        @Field(name = "Input") String input) {
        return customAction.execute(input);
    }
}
```

### 4. Custom Error Handlers

Extend ApiExceptionHandler for custom error handling:

```java
public class CustomExceptionHandler extends ApiExceptionHandler {
    public static Result handleCustomException(Exception e) {
        // Custom error handling logic
        return new Result(userMessage, logMessage);
    }
}
```

### 5. Custom File Processors

Extend KristaMediaClient for custom file handling:

```java
public class CustomMediaClient extends KristaMediaClient {
    @Override
    public File toKristaFile(File file) throws IOException {
        // Custom file processing (e.g., encryption, validation)
        return super.toKristaFile(file);
    }
}
```

---

## Package Structure

```
app.krista.extensions.development.api.rest/
│
├── RestApiExtension.java          # Main extension entry point
├── RestApiAttributes.java         # Configuration model
│
├── api/                            # JAX-RS REST endpoints
│   ├── RestApiApplication.java    # JAX-RS application config
│   ├── AuthResource.java          # Authentication endpoint
│   └── AuthCallBackResource.java  # OAuth callback handler
│
├── auth/                           # Authentication components
│   ├── OAuthClient.java           # OAuth 2.0 client
│   ├── AccessToken.java           # Access token model
│   ├── AuthPayload.java           # Auth configuration model
│   ├── AttributeStore.java        # Credential storage interface
│   ├── SaveCredentialsResponse.java
│   └── TestConnectionResponse.java
│
├── catalog/                        # Catalog layer (user-facing)
│   ├── DownloadArea.java          # File download operations
│   ├── ReadArea.java              # GET operations
│   ├── WriteArea.java             # POST operations
│   ├── UpdateArea.java            # PUT/PATCH operations
│   ├── DeleteArea.java            # DELETE operations
│   └── Response.java              # Response wrapper
│
├── connectors/                     # Connector factories
│   ├── ActionableImplProvider.java
│   └── ActionableImplProviderFactory.java
│
├── impl/                           # Service layer (business logic)
│   ├── ActionableImpl.java        # Core HTTP service
│   ├── ReadAction.java            # GET operations service
│   ├── WriteAction.java           # POST operations service
│   ├── ModifyAction.java          # PUT/PATCH operations service
│   ├── RemoveAction.java          # DELETE operations service
│   ├── DownloadAction.java        # File download service
│   ├── HTTPRequest.java           # HTTP request builder
│   ├── AuthHelper.java            # Authentication helper
│   └── Constants.java             # Shared constants
│
├── stores/                         # Persistent storage
│   ├── RefreshTokenStore.java     # OAuth token storage
│   └── RestApiAttributeStore.java # Configuration storage
│
└── util/                           # Utilities
    ├── KristaMediaClient.java     # File conversion utility
    ├── ErrorMessages.java         # Error message templates
    ├── ApiExceptionHandler.java   # Exception handling
    ├── HttpResponseException.java # Custom exception
    ├── ResponseUtil.java          # Response utilities
    ├── AuthUtils.java             # Auth utilities
    └── RestApiConstants.java      # Constants
```

### Package Responsibilities

| Package | Responsibility | Key Classes |
|---------|---------------|-------------|
| **api** | JAX-RS endpoints for UI integration | RestApiApplication, AuthResource |
| **auth** | Authentication and authorization | OAuthClient, AccessToken |
| **catalog** | User-facing catalog requests | ReadArea, WriteArea, UpdateArea |
| **connectors** | Client factories and providers | ActionableImplProvider |
| **impl** | Core business logic and HTTP operations | ActionableImpl, Action classes |
| **stores** | Persistent data storage | RefreshTokenStore, AttributeStore |
| **util** | Shared utilities and helpers | KristaMediaClient, ErrorMessages |

---

## Architecture Diagrams

### Component Dependency Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                    Krista Platform                           │
│  ┌────────────────────────────────────────────────────────┐  │
│  │         Extension Framework (HK2)                      │  │
│  └────────────────────────────────────────────────────────┘  │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│              RestApiExtension                                │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  JAX-RS Layer (api.*)                                  │  │
│  │    - RestApiApplication                                │  │
│  │    - AuthResource, AuthCallBackResource                │  │
│  └────────────────────────────────────────────────────────┘  │
│                         │                                    │
│                         ▼                                    │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Catalog Layer (catalog.*)                             │  │
│  │    - DownloadArea, ReadArea, WriteArea                 │  │
│  │    - UpdateArea, DeleteArea                            │  │
│  └────────────────────────────────────────────────────────┘  │
│                         │                                    │
│                         ▼                                    │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Service Layer (impl.*)                                │  │
│  │    - ActionableImpl (Core)                             │  │
│  │    - ReadAction, WriteAction, ModifyAction             │  │
│  │    - RemoveAction, DownloadAction                      │  │
│  └────────────────────────────────────────────────────────┘  │
│                         │                                    │
│                         ▼                                    │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Connector Layer (connectors.*, auth.*, util.*)        │  │
│  │    - HTTPRequest (OkHttp)                              │  │
│  │    - OAuthClient (Jersey Client)                       │  │
│  │    - KristaMediaClient                                 │  │
│  └────────────────────────────────────────────────────────┘  │
│                         │                                    │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Storage Layer (stores.*)                              │  │
│  │    - RefreshTokenStore                                 │  │
│  │    - RestApiAttributeStore                             │  │
│  └────────────────────────────────────────────────────────┘  │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │  External REST APIs  │
              └──────────────────────┘
```

### Request Processing Flow

```
┌─────────┐
│  User   │
└────┬────┘
     │
     │ 1. Catalog Request
     ▼
┌─────────────────┐
│  Catalog Area   │ ◄─── @CatalogRequest annotation
│  (ReadArea)     │      @Field validation
└────┬────────────┘
     │
     │ 2. Delegate to Action
     ▼
┌─────────────────┐
│  Action Class   │ ◄─── Business logic
│  (ReadAction)   │      Error handling
└────┬────────────┘
     │
     │ 3. Execute request
     ▼
┌─────────────────┐
│ ActionableImpl  │ ◄─── Retry logic
│                 │      Response parsing
└────┬────────────┘
     │
     │ 4. Build HTTP request
     ▼
┌─────────────────┐
│  HTTPRequest    │ ◄─── Authentication
│                 │      Headers, params
└────┬────────────┘
     │
     │ 5. Get access token (if OAuth)
     ▼
┌─────────────────┐
│  OAuthClient    │ ◄─── Token refresh
│                 │      Authorization
└────┬────────────┘
     │
     │ 6. HTTP call
     ▼
┌─────────────────┐
│  OkHttpClient   │ ◄─── Connection pool
│                 │      Request execution
└────┬────────────┘
     │
     │ 7. HTTP Response
     ▼
┌─────────────────┐
│ External API    │
└─────────────────┘
```

---

## Best Practices

### 1. Error Handling
- Always use ErrorMessages utility for user-facing errors
- Log detailed error information for troubleshooting
- Provide actionable recommendations in error messages
- Use appropriate HTTP status codes

### 2. Resource Management
- Implement PreDestroy for cleanup (ExecutorService, connections)
- Close response bodies in finally blocks
- Use try-with-resources for file operations
- Clean up temporary files after processing

### 3. Security
- Never log sensitive credentials or tokens
- Use secure token storage (RefreshTokenStore)
- Validate all user inputs
- Compress unsupported file formats before upload

### 4. Performance
- Reuse HTTP client instances (connection pooling)
- Implement pagination for large datasets
- Use asynchronous processing for long-running operations
- Set appropriate timeouts (300s default)

### 5. Testing
- Mock external dependencies (OAuthClient, OkHttpClient)
- Test retry logic with simulated failures
- Validate error handling paths
- Test file upload/download scenarios

---

## Conclusion

The REST API Extension provides a robust, production-ready solution for integrating external REST APIs into the Krista platform. Its three-layer architecture ensures clear separation of concerns, while design patterns like CQRS, Template Method, and Fail-Fast validation promote maintainability and reliability.

Key architectural strengths:
- **Modularity**: Clear layer boundaries enable independent evolution
- **Extensibility**: Multiple extension points for customization
- **Resilience**: Retry logic and comprehensive error handling
- **Security**: Multi-authentication support with secure token management
- **Performance**: Connection pooling and asynchronous processing

For implementation details, refer to the source code in `restapi/src/main/java/app/krista/extensions/development/api/rest/`.

