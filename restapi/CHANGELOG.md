# Changelog

All notable changes to the REST API Extension for Krista will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Enhanced retry mechanism with configurable backoff strategies
- Support for GraphQL endpoints
- Webhook integration capabilities
- Advanced caching mechanisms for improved performance

## [2.0.16] - 2024-01-20

### Added
- GPLv3 license with comprehensive license headers across all source files
- Complete open source documentation (README.md, ARCHITECTURE.md, QUICKSTART.md)
- Professional documentation structure with detailed guides
- Comprehensive API reference documentation
- Platform-specific integration guides (Microsoft, Google)
- JaCoCo integration for code coverage reporting (95%+ coverage)
- SonarQube integration for code quality analysis
- Automated test coverage reports in XML and HTML formats

### Changed
- Enhanced error handling with user-friendly, actionable error messages
- Improved test coverage across all components
- Updated documentation with troubleshooting sections
- Refined authentication flow documentation
- Improved code quality and maintainability
- Enhanced JavaDoc comments following Java 21 best practices

### Security
- Implemented secure credential storage mechanisms
- Enhanced OAuth 2.0 token management
- Improved encrypted data transmission protocols

## [2.0.14] - 2024-01-10

### Added
- POST request file response support in Write area
- Automatic file extension detection based on Content-Type headers
- Support for multiple file formats (JSON, PDF, Excel, CSV, XML, images)
- Comprehensive error handling for file operations

### Changed
- Enhanced payload handling for POST requests
- Improved response parsing for file downloads

## [2.0.13] - 2024-01-05

### Fixed
- [KE-2793] Attributes value reset issue after clicking Test Connection
- Redundant credential handling in authentication flow
- Duplicate attribute store operations

### Changed
- Simplified authentication helper methods
- Improved code maintainability in AuthResource and AuthHelper classes
- Removed unnecessary attribute store updates during test connection

## [2.0.11] - 2023-12-15

### Added
- Centralized error management with ErrorMessages utility class
- Comprehensive troubleshooting guide (300+ lines)
- Enhanced JavaDoc documentation across all classes
- Error handling sections in overview and authentication guides
- Complete DOCX documentation optimized for LLM consumption

### Changed
- All error messages now provide clear explanations and actionable steps
- Improved error context with status codes and response details
- Enhanced documentation structure with consistent formatting
- Better technical accuracy in code comments

### Fixed
- Grammar and style issues in documentation
- Improved method descriptions with proper JavaDoc tags

## [2.0.6] - 2023-11-20

### Added
- Wait for Event Post Request support for long-running API calls
- Wait for Event Post Response handling in Write area
- Wait for Event Get Request support for asynchronous operations
- Wait for Event Get Response handling in Read area

### Changed
- Enhanced async operation support for long-duration API calls
- Improved event-driven processing capabilities

## [2.0.5] - 2023-11-10

### Added
- GET Response as File functionality in Read area
- Support for receiving GET API responses as downloadable files
- Automatic file format handling for various content types

### Changed
- Enhanced Read area with file download capabilities
- Improved response transformation for file operations

## [2.0.2] - 2023-10-25

### Added
- File inclusion support in Write area payload
- File inclusion support in Modify area payload
- Enhanced multipart/form-data handling

### Changed
- Revamped Write area with file upload capabilities
- Revamped Modify area with file attachment support
- Improved payload construction for file operations

## [2.0.0] - 2023-10-01

### Added
- Java 21 support and migration
- Enhanced OAuth 2.0 implementation with automatic token refresh
- Pagination support for large datasets
- Custom header and query parameter support
- Retry mechanism with exponential backoff
- Three-layer architecture (Catalog, Service, Connector)

### Changed
- Major version upgrade to Java 21
- Redesigned architecture for better separation of concerns
- Improved performance and scalability
- Enhanced error handling and logging

### Removed
- Java 8 support
- Legacy authentication mechanisms
- Deprecated API endpoints

## [1.5.0] - 2023-08-15

### Added
- OAuth 2.0 authentication support
- Microsoft Graph API integration templates
- Google Workspace integration templates
- Token refresh mechanism
- Secure credential storage

### Changed
- Enhanced authentication layer architecture
- Improved token lifecycle management

## [1.0.0] - 2023-06-01

### Added
- Initial release of REST API Extension
- Basic authentication support
- Token-based authentication
- HTTP methods: GET, POST, PUT, PATCH, DELETE
- Query parameter support
- Custom header support
- Basic error handling
- Response parsing and transformation

---

## Known Issues

- [KR-11948](https://antbrains.atlassian.net/browse/KR-11948) - Write/Update area shows payload as mandatory but executes with blank values
- [KR-12470](https://antbrains.atlassian.net/browse/KR-12470) - File selection issues in POST and Modify request payloads
- [KE-1224](https://antbrains.atlassian.net/browse/KE-1224) - Performance warnings in Extension logs

## Limitations

- API responses containing `type` as a key are automatically renamed to `_type` (reserved keyword)
- API responses containing `description` as a key are automatically renamed to `_description` (reserved keyword)
- Objects with `type={Any Object}` in JSON responses are not supported in Inform a Person steps

---

[unreleased]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.16...HEAD
[2.0.16]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.14...v2.0.16
[2.0.14]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.13...v2.0.14
[2.0.13]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.11...v2.0.13
[2.0.11]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.6...v2.0.11
[2.0.6]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.5...v2.0.6
[2.0.5]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.2...v2.0.5
[2.0.2]: https://github.com/krista-ai/rest-api-extension/compare/v2.0.0...v2.0.2
[2.0.0]: https://github.com/krista-ai/rest-api-extension/compare/v1.5.0...v2.0.0
[1.5.0]: https://github.com/krista-ai/rest-api-extension/compare/v1.0.0...v1.5.0
[1.0.0]: https://github.com/krista-ai/rest-api-extension/releases/tag/v1.0.0

