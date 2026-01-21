# Release Notes - Krista Rest Api Extension

## Version 2.0.16

- **Developer Name** : Simran Sethi & Vrushali Gaikwad
- **Krista Service APIs Java** : 1.0.118
- **Global Catalog Version** :  GC-2026.01.3

## What's New in 2.0.14

This release introduces **new functionality** for POST request file responses and **code quality improvements**:

### ✨ New Features
- **[New] Get Post Request Response as File**: New catalog request in Write area enables receiving POST API responses as downloadable files
  - Supports sending data with payload, headers, and filters
  - Returns response as a file object along with response information
  - Handles various file formats (JSON, PDF, Excel, CSV, XML, images, etc.)
  - Automatic file extension detection based on Content-Type headers
  - Comprehensive error handling with user-friendly messages


###  Build & Analysis
- **JaCoCo Integration**: Added JaCoCo plugin for code coverage reporting
  - Automatic test coverage reports in XML and HTML formats
  - Integration with test execution workflow
- **SonarQube Integration**: Added SonarQube plugin for code quality analysis
  - Configured for Java source and test analysis
  - Coverage report integration with JaCoCo

---

## What's New in 2.0.13

This release focuses on **bug fixes** to improve stability and reliability:

### 🐛 Bug Fixes
- **[KE-2793] Fixed Attributes Value Reset Issue**: Resolved bug where attributes value was being set to null after clicking Test Connection
  - Removed unnecessary attribute store updates during test connection flow
  - Simplified authentication helper methods by removing redundant credential handling
  - Improved code maintainability by eliminating duplicate attribute store operations

### 🔧 Technical Changes
- **AuthResource.java**: Removed redundant credential handling in `testConnection()` method
- **AuthHelper.java**: Simplified `validateAndTestConnection()` and `testConnection()` methods
- **RestApiExtension.java**: Updated version to 2.0.13

---

## What's New in 2.0.11

This release focuses on **improving user experience** through enhanced error messages, comprehensive documentation, and better code quality:

### 🎯 User Experience Improvements
- **User-Friendly Error Messages**: All error messages now provide clear explanations, possible causes, and actionable steps to resolve issues
- **Comprehensive Troubleshooting Guide**: New documentation helps users quickly diagnose and fix common problems
- **Better Error Context**: Errors now include specific details like status codes, response bodies, and operation context

### 📚 Documentation Enhancements
- **Enhanced JavaDoc**: All classes have improved documentation following standard Java conventions
- **Troubleshooting Guide**: 300+ lines of detailed troubleshooting information
- **Error Handling Sections**: Added to overview and authentication guides
- **Complete DOCX Documentation**: Comprehensive documentation file optimized for LLM consumption

### 🔧 Technical Improvements
- **Centralized Error Management**: New `ErrorMessages` utility class for consistent error handling
- **Improved Code Quality**: Better JavaDoc comments, fixed grammar issues, enhanced technical accuracy
- **Updated All Areas**: Improvements across Read, Write, Update, Delete, and Download areas

### 📖 Documentation Links
- [Troubleshooting Guide](pages/troubleshooting.md) - Comprehensive error resolution guide
- [Overview](pages/overview.md) - Updated with error handling section
- [Authentication](pages/authentication.md) - Enhanced with troubleshooting tips

---

## Known Issue
* [KR-11948](https://antbrains.atlassian.net/browse/KR-11948) Write area(post)/Update area(modify) requests shows payload
  as mandatory but still getting executed in client with blank key/value
* [KR-12470](https://antbrains.atlassian.net/browse/KR-12470) Unable to select files 
  in the payload for POST and Modify requests
* [KE-1224](https://antbrains.atlassian.net/browse/KE-1224) Observing performance warning in Extension logs

## Enhancement Highlights

#### Key Highlights of Version 2.0.11

* **Enhanced Error Messages** - All error messages have been improved to be more user-friendly and actionable:
  - Clear descriptions of what went wrong
  - Possible causes listed for each error
  - Step-by-step recommended actions to resolve issues
  - Specific details for troubleshooting
  - Centralized error message management through ErrorMessages utility class

* **Comprehensive Troubleshooting Guide** - New troubleshooting documentation covering:
  - Connection errors (timeouts, unreachable endpoints)
  - Authentication errors (OAuth failures, invalid credentials)
  - Request/Response errors (4xx and 5xx status codes)
  - Data size errors (response limits, pagination)
  - Validation errors (invalid URL, JSON format)
  - Performance optimization tips
  - Best practices for avoiding common issues

* **Improved JavaDoc Documentation** - All Java classes now have enhanced JavaDoc comments:
  - Comprehensive class-level documentation
  - Detailed method-level documentation with proper @param, @return, and @throws tags
  - Fixed grammar and style issues
  - Improved technical accuracy and clarity

* **Enhanced Documentation Structure** - Documentation improvements include:
  - Added error handling sections to overview and authentication guides
  - Consistent link formatting across all documentation pages
  - Added troubleshooting guide to navigation sidebar
  - Comprehensive DOCX documentation file for LLM consumption

* **Better Error Context** - Error messages now include:
  - HTTP status codes and status messages
  - Response body details for debugging
  - Specific operation context (e.g., "reading data with filters")
  - File size information for size limit errors
  - URL validation with specific format requirements

#### Key Highlights of Version 2.0.6

* **Wait for Event Post Request and Wait for Event Post Response** - The requests in the Write area now support long-running API calls which take longer duration to fetch response.
* **Wait for Event Get Request and Wait for Event Get Response** - The requests in the Read area now support long-running API calls which take longer duration to fetch response.

#### Technical Improvements in Version 2.0.11

* **Centralized Error Management**
  - Created `ErrorMessages` utility class for consistent error messaging
  - All error messages follow a standard format with causes and solutions
  - Easy to maintain and update error messages in one location

* **Enhanced Error Handling**
  - Updated `ActionableImpl.java` with 9 improved error messages
  - Updated `HTTPRequest.java` with better validation and error context
  - Updated all catalog area classes (ReadArea, DeleteArea, DownloadAction)
  - Updated `OAuthClient.java` with clearer OAuth error messages

* **Documentation Improvements**
  - Created comprehensive troubleshooting guide (300+ lines)
  - Added error handling sections to existing documentation
  - Updated navigation sidebar with troubleshooting link
  - Ensured all internal links use proper `pages/` prefix

* **Code Quality**
  - Improved JavaDoc comments across all classes
  - Fixed grammar and style issues in documentation
  - Enhanced method descriptions with proper tags
  - Better technical accuracy in code comments

#### Key Highlights of Version 2.0.5

* **Get Response as a File** - Enhancements in the Read areas now enable you to receive responses from any GET API as a file. For more detailed information, please refer to the documentation on supported requests.

#### Key Highlights of Version 2.0.2

* **Revamped Write & Modify Areas:** The requests in the Write and Modify areas now support file inclusion in payload.
  If you're working with existing conversations, ensure to integrate these changes.

## Limitation

- While setting up a conversation and adding response into the Inform a Person step, an API response that contains "type={Any Object}" in the JSON, is not supported.
- For example,
In Jira while fetching get information of issue which has linked Issues we will see such type object

```json
{
  "type": {
    "id": 123,
    "name": "1_Relates",
    "inward": "relatesto"
  }
}
```

---

## ⚠️ Important Note: Reserved Keyword

The keyword **`type`** and **`description`**  is a **reserved keyword** in Krista and cannot be used as a key name in API response payloads. If an external API returns a response containing `type` or `description` as a key, it will be automatically renamed to `_type` and `_description` respectively to avoid conflicts with Krista's internal type system.

**Example:**
- Original API response: `{"type": "City", "name": "New Delhi", "description": "Its a beautiful city"}`
- Processed response: `{"_type": "City", "name": "New Delhi", "_description": "Its a beautiful city"}`

This applies to all nested levels of the response object.