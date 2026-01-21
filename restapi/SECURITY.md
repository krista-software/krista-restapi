# Security Policy

## Supported Versions

We actively maintain and provide security updates for the following versions of the REST API Extension:

| Version | Supported          | Status |
| ------- | ------------------ | ------ |
| 2.0.16  | :white_check_mark: | Current Release |
| 2.0.14  | :white_check_mark: | Supported |
| 2.0.13  | :white_check_mark: | Supported |
| 2.0.11  | :white_check_mark: | Supported |
| < 2.0.11| :x:                | End of Life |

**Note:** We recommend always using the latest version to benefit from the most recent security enhancements and bug fixes.

## Reporting a Vulnerability

### How to Report

If you discover a security vulnerability in the REST API Extension, please report it responsibly:

**Email:** [security@kristasoft.com](mailto:security@kristasoft.com)

**Subject Line:** `[SECURITY] REST API Extension - [Brief Description]`

### What to Include

Please provide the following information in your report:

1. **Description:** A clear description of the vulnerability
2. **Impact:** Potential impact and severity assessment
3. **Steps to Reproduce:** Detailed steps to reproduce the issue
4. **Affected Versions:** Which versions are affected
5. **Proof of Concept:** Code snippets or screenshots (if applicable)
6. **Suggested Fix:** Any recommendations for remediation (optional)
7. **Contact Information:** Your name and preferred contact method

### Response Timeline

- **Initial Response:** Within 48 hours of receipt
- **Status Update:** Within 5 business days with preliminary assessment
- **Resolution Timeline:** Varies based on severity
  - **Critical:** 7-14 days
  - **High:** 14-30 days
  - **Medium:** 30-60 days
  - **Low:** 60-90 days

### What to Expect

1. We will acknowledge receipt of your vulnerability report
2. We will investigate and validate the reported issue
3. We will keep you informed of our progress
4. We will notify you when the issue is resolved
5. We will credit you in our security advisories (unless you prefer to remain anonymous)

### Responsible Disclosure

We kindly request that you:
- Do not publicly disclose the vulnerability until we have released a fix
- Do not exploit the vulnerability beyond what is necessary to demonstrate it
- Make a good faith effort to avoid privacy violations and data destruction

## Security Update Process

### Release Process

1. **Identification:** Security issues are identified through reports or internal audits
2. **Assessment:** Our security team evaluates severity and impact
3. **Development:** Patches are developed and tested
4. **Testing:** Comprehensive security testing is performed
5. **Release:** Security updates are released as patch versions
6. **Notification:** Users are notified through release notes and security advisories

### Update Notifications

Security updates are communicated through:
- Release notes in the extension documentation
- Email notifications to registered users
- Security advisories on our website
- Version update notifications in the Krista platform

## Best Practices for Users

### Credential Storage

✅ **DO:**
- Use the extension's built-in secure credential storage
- Store API keys and passwords using the `isSecured = true` field attribute
- Rotate credentials regularly (recommended: every 90 days)
- Use unique credentials for each environment (dev, staging, production)
- Implement least-privilege access principles

❌ **DON'T:**
- Hard-code credentials in conversation configurations
- Share credentials across multiple integrations
- Store credentials in plain text files or documentation
- Use production credentials in development environments

### HTTPS and Transport Security

✅ **DO:**
- Always use HTTPS endpoints for API communications
- Verify SSL/TLS certificates are valid
- Use TLS 1.2 or higher
- Ensure your API endpoints support modern cipher suites

❌ **DON'T:**
- Use HTTP for sensitive data transmission
- Disable SSL/TLS certificate validation
- Accept self-signed certificates in production

### Token Management

✅ **DO:**
- Use OAuth 2.0 for authentication when available
- Leverage the extension's automatic token refresh mechanism
- Monitor token expiration and renewal
- Revoke tokens when no longer needed
- Use conversation-scoped tokens appropriately

❌ **DON'T:**
- Share OAuth refresh tokens between conversations
- Store tokens outside the extension's secure storage
- Use expired or invalid tokens
- Bypass token validation mechanisms

### API Key Security

✅ **DO:**
- Generate strong, unique API keys
- Restrict API key permissions to minimum required scope
- Monitor API key usage for anomalies
- Revoke compromised keys immediately
- Use separate keys for different services

❌ **DON'T:**
- Expose API keys in logs or error messages
- Include API keys in URLs or query parameters
- Commit API keys to version control
- Share API keys via insecure channels (email, chat)

### Network Security

✅ **DO:**
- Use firewall rules to restrict outbound connections
- Implement IP whitelisting where possible
- Monitor network traffic for anomalies
- Use VPNs for sensitive integrations
- Configure appropriate timeout values

### Data Handling

✅ **DO:**
- Validate all input data before processing
- Sanitize data in error messages and logs
- Implement appropriate data retention policies
- Encrypt sensitive data at rest
- Follow GDPR and data privacy regulations

❌ **DON'T:**
- Log sensitive information (passwords, tokens, PII)
- Store unnecessary sensitive data
- Process data beyond stated purposes
- Share data with unauthorized third parties

## Best Practices for Developers

### Input Validation

✅ **DO:**
- Validate all user inputs before processing
- Use the extension's built-in validation mechanisms
- Implement fail-fast validation (validate early, fail early)
- Validate URL formats using `isUrlValid()` method
- Validate HTTP methods using `isMethodTypeValid()` method
- Check for null and empty values
- Validate JSON payload structure before sending requests

**Example from codebase:**
```java
if (!isUrlValid(urlWithQueryParams)) {
    throw new IllegalArgumentException(ErrorMessages.invalidUrl(urlWithQueryParams));
}
if (!isMethodTypeValid(methodType)) {
    throw new IllegalArgumentException(ErrorMessages.invalidHttpMethod(methodType));
}
```

❌ **DON'T:**
- Trust user input without validation
- Skip validation for "internal" requests
- Use generic error messages that expose system details
- Allow arbitrary URL access without validation

### Secure Coding Practices

✅ **DO:**
- Use parameterized queries and prepared statements
- Implement proper exception handling with the `ApiExceptionHandler`
- Use centralized error message management (`ErrorMessages` utility class)
- Follow the principle of least privilege
- Implement proper logging without exposing sensitive data
- Use secure random number generation for tokens and IDs
- Sanitize all outputs to prevent injection attacks

**Error Handling Example:**
```java
try {
    // API operation
} catch (Exception e) {
    Result result = ApiExceptionHandler.handle(e, url, operation);
    LOGGER.error(result.getLogMessage());
    throw new RuntimeException(result.getUserMessage());
}
```

❌ **DON'T:**
- Expose stack traces to end users
- Log sensitive information (passwords, tokens, API keys)
- Use deprecated or insecure libraries
- Implement custom cryptography
- Ignore compiler warnings (project uses `-Xlint:all,-serial`)

### Authentication and Authorization

✅ **DO:**
- Use the `@Field.Text(isSecured = true)` annotation for sensitive fields
- Implement proper OAuth 2.0 flows using `OAuthClient`
- Use the `RefreshTokenStore` for secure token storage
- Validate authentication state before processing requests
- Implement proper session management
- Use the `AttributeStore` for credential management

**Secure Field Example:**
```java
@Field.Text(value = Constants.PASSWORD, required = false, isSecured = true)
@Field.Text(value = Constants.API_KEY, required = false, isSecured = true)
```

❌ **DON'T:**
- Store credentials in plain text
- Implement custom authentication schemes
- Skip authentication checks
- Hard-code credentials or secrets
- Use weak or predictable tokens

### Dependency Management

✅ **DO:**
- Keep dependencies up to date
- Use specific version numbers (avoid wildcards)
- Regularly audit dependencies for vulnerabilities
- Use trusted repositories only
- Review dependency licenses for compliance

**Current Key Dependencies:**
```gradle
implementation 'app.krista:krista-apis:1.0.118'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.fasterxml.jackson.core:jackson-databind:2.18.2'
implementation 'org.glassfish.jersey.media:jersey-media-multipart:2.41'
```

❌ **DON'T:**
- Use outdated or unmaintained libraries
- Include unnecessary dependencies
- Use dependencies from untrusted sources
- Ignore security advisories for dependencies

### Code Review and Testing

✅ **DO:**
- Conduct security-focused code reviews
- Write comprehensive unit tests (use JUnit 5)
- Implement integration tests for authentication flows
- Use code coverage tools (JaCoCo is configured)
- Perform static code analysis (SonarQube is configured)
- Test error handling paths
- Validate input sanitization

**Testing Configuration:**
```gradle
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.0'
testImplementation 'org.mockito:mockito-core:5.5.0'
```

❌ **DON'T:**
- Skip security testing
- Test only happy paths
- Ignore code coverage metrics
- Deploy without proper testing

### Secure Communication

✅ **DO:**
- Use OkHttp client with proper SSL/TLS configuration
- Validate SSL certificates
- Implement proper timeout configurations
- Use secure headers (Authorization, Content-Type)
- Implement retry logic with exponential backoff

**HTTP Client Usage:**
```java
RequestBody body = getBody(payload, methodType);
Request.Builder requestBuilder = new Request.Builder()
    .url(urlWithQueryParams)
    .method(methodType, body);
createAuthHeader(requestBuilder);
```

❌ **DON'T:**
- Disable SSL verification
- Use insecure protocols (HTTP for sensitive data)
- Implement unlimited retries
- Expose sensitive data in headers or URLs



## Known Security Considerations

### OAuth 2.0 Flows

**Security Features:**
- Automatic token refresh using refresh tokens
- Secure callback URL handling
- State parameter validation for CSRF protection
- Token revocation support

**Considerations:**
- OAuth tokens are conversation-scoped
- Refresh tokens are stored securely in `RefreshTokenStore`
- Access tokens are automatically refreshed before expiration
- Google OAuth integration includes token revocation

**Implementation:**
```java
OAuthClient oAuthClient = new OAuthClient(clientId, clientSecret,
    authUrl, accessTokenUrl, scope, routingUrl);
AccessToken accessToken = oAuthClient.refreshAccessToken(refreshToken);
```

### Token Storage

**Security Features:**
- Tokens stored in encrypted `KeyValueStore`
- Conversation-scoped token isolation
- Automatic cleanup when conversations end
- Secure token retrieval and validation

**Storage Mechanism:**
```java
public void put(String key, String refToken) {
    keyValueStore.put(key, refToken);
}
```

**Important Notes:**
- Tokens from Conversation A cannot be used in Conversation B
- Tokens persist only for the conversation duration
- No cross-conversation token sharing

### API Key Management

**Security Features:**
- API keys marked with `isSecured = true` attribute
- Encrypted storage in attribute store
- No logging of API key values
- Secure transmission over HTTPS only

**Best Practices:**
- Rotate API keys regularly
- Use different keys for different environments
- Monitor API key usage
- Revoke compromised keys immediately

### Basic Authentication

**Security Features:**
- Credentials stored with `isSecured = true`
- Base64 encoding for HTTP Basic Auth
- Secure credential validation
- No credential logging

**Considerations:**
- Always use HTTPS with Basic Auth
- Implement credential rotation policies
- Monitor for unauthorized access attempts
- Use strong, unique passwords

## Security Features

### Encrypted Storage

The extension provides secure storage for sensitive data:

**Features:**
- All credentials encrypted at rest
- Secure `KeyValueStore` implementation
- Conversation-scoped data isolation
- Automatic data cleanup

**Protected Data:**
- API keys
- Passwords
- OAuth tokens (access and refresh)
- Client secrets
- Authentication credentials

### Token Refresh Mechanism

**Automatic Token Management:**
- Proactive token refresh before expiration
- Seamless re-authentication
- No user intervention required
- Fallback to re-authentication on refresh failure

**Implementation:**
```java
String refreshToken = refreshTokenStore.get(restApiAttributes.getClientId());
AccessToken accessToken = oAuthClient.refreshAccessToken(refreshToken);
requestBuilder.addHeader("Authorization", BEARER + accessToken.getAccessToken());
```

### Fail-Fast Validation

**Input Validation:**
- URL format validation
- HTTP method validation
- Payload structure validation
- Authentication credential validation

**Benefits:**
- Early error detection
- Prevents invalid requests
- Reduces attack surface
- Clear error messages for troubleshooting

**Example:**
```java
@InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
public void validateAttributes(Map<String, Object> attributes) {
    if (serverAddress == null || serverAddress.trim().isEmpty()) {
        throw new IllegalArgumentException("Server Address is required.");
    }
}
```

### Comprehensive Error Handling

**Error Categories:**
- Network errors (connection refused, host unreachable)
- Timeout errors (socket timeout, connection timeout)
- SSL/TLS errors
- Authentication and authorization errors
- Input validation errors
- Server-side errors (5xx)

**Error Handling Features:**
- Centralized exception handling via `ApiExceptionHandler`
- User-friendly error messages
- Detailed logging for debugging
- Retryable vs non-retryable error classification
- Sensitive data sanitization in error messages

### Secure HTTP Client

**OkHttp Security Features:**
- Modern TLS support (TLS 1.2+)
- Certificate validation
- Connection pooling with security
- Timeout configurations
- Secure header management

**Configuration:**
```java
Request.Builder requestBuilder = new Request.Builder()
    .url(urlWithQueryParams)
    .method(methodType, body);
createAuthHeader(requestBuilder);
addCustomHeaders(headers, requestBuilder);
```

## Compliance and Data Privacy

### GDPR Considerations

The REST API Extension is designed to support GDPR compliance:

**Data Minimization:**
- Only collect and process necessary data
- No unnecessary data retention
- Conversation-scoped data storage
- Automatic data cleanup when conversations end

**Data Subject Rights:**
- Right to access: Users can view stored credentials through the extension UI
- Right to erasure: Credentials can be deleted via the extension
- Right to rectification: Credentials can be updated at any time
- Data portability: API responses can be exported in standard formats

**Data Processing:**
- Data processed only for stated integration purposes
- No data sharing with unauthorized third parties
- Secure data transmission (HTTPS/TLS)
- Encrypted data storage

**Consent and Transparency:**
- Clear documentation of data handling practices
- User control over credential storage
- Transparent authentication flows
- Audit logging capabilities

### Data Handling Best Practices

**Personal Data:**
- Minimize collection of personally identifiable information (PII)
- Encrypt PII in transit and at rest
- Implement appropriate retention policies
- Provide mechanisms for data deletion

**API Response Data:**
- Process only necessary fields from API responses
- Sanitize sensitive data in logs and error messages
- Implement data masking where appropriate
- Follow data classification policies

**Audit and Logging:**
- Log authentication events (without credentials)
- Track API access patterns
- Monitor for anomalous behavior
- Retain logs according to compliance requirements

**Cross-Border Data Transfer:**
- Ensure API endpoints comply with data residency requirements
- Use appropriate data transfer mechanisms
- Document data flows for compliance audits
- Implement regional data storage where required

### Compliance Certifications

**Extension Compliance:**
- Java 21 compliance
- OpenAPI 3.0 specification support
- REST API best practices
- OAuth 2.0 standard compliance

**Security Standards:**
- Secure coding practices (OWASP guidelines)
- Input validation and sanitization
- Secure credential storage
- TLS/SSL encryption

## Security Audit and Monitoring

### Recommended Monitoring

**Authentication Monitoring:**
- Failed authentication attempts
- Token refresh failures
- Credential rotation events
- OAuth authorization flows

**API Usage Monitoring:**
- Request/response patterns
- Error rates and types
- Response time anomalies
- Rate limiting violations

**Security Events:**
- SSL/TLS handshake failures
- Invalid certificate warnings
- Unauthorized access attempts
- Suspicious request patterns

### Logging Best Practices

✅ **DO Log:**
- Authentication events (success/failure)
- API endpoint access
- Error conditions and exceptions
- Configuration changes
- Token refresh events

❌ **DON'T Log:**
- Passwords or API keys
- OAuth tokens (access or refresh)
- Full API responses containing PII
- Sensitive request payloads
- User credentials

### Security Auditing

**Regular Audits:**
- Review access logs quarterly
- Audit credential usage and rotation
- Check for outdated dependencies
- Review error patterns
- Validate SSL/TLS configurations

**Incident Response:**
- Document security incidents
- Analyze root causes
- Implement corrective actions
- Update security policies
- Communicate with affected users

## Additional Resources

### Documentation

- [REST API Extension Documentation](docs/)
- [Architecture Guide](ARCHITECTURE.md)
- [Quick Start Guide](QUICKSTART.md)
- [Release Notes](src/main/resources/docs/pages/releaseNotes.md)
- [Troubleshooting Guide](src/main/resources/docs/pages/troubleshooting.md)

### Security Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [OAuth 2.0 Security Best Practices](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics)
- [REST API Security Best Practices](https://restfulapi.net/security-essentials/)
- [Java Secure Coding Guidelines](https://www.oracle.com/java/technologies/javase/seccodeguide.html)

### Support and Contact

**Security Issues:**
- Email: security@kristasoft.com
- Subject: [SECURITY] REST API Extension

**General Support:**
- Documentation: Check the extension documentation
- Issue Tracker: Report bugs and feature requests
- Community: Join the Krista developer community

### Version History

**Current Version: 2.0.16**
- Developer: Simran Sethi & Vrushali Gaikwad
- Krista Service APIs Java: 1.0.118
- Global Catalog Version: GC-2026.01.3
- Java Version: 21

**Recent Security Enhancements:**
- Version 2.0.14: Enhanced error handling and file response security
- Version 2.0.13: Fixed attribute value reset security issue
- Version 2.0.11: Comprehensive error messages and validation improvements
- Version 2.0.6: Long-running API call support with timeout handling

## Security Checklist

### For Users

- [ ] All API endpoints use HTTPS
- [ ] Credentials stored using extension's secure storage
- [ ] API keys rotated regularly (every 90 days)
- [ ] Different credentials for dev/staging/production
- [ ] OAuth 2.0 used where available
- [ ] SSL/TLS certificates validated
- [ ] Network access restricted via firewall rules
- [ ] Monitoring enabled for API usage
- [ ] Incident response plan in place
- [ ] Regular security audits scheduled

### For Developers

- [ ] All inputs validated before processing
- [ ] Sensitive fields marked with `isSecured = true`
- [ ] Error handling uses `ApiExceptionHandler`
- [ ] No sensitive data in logs or error messages
- [ ] Dependencies up to date
- [ ] Unit tests cover security scenarios
- [ ] Code coverage meets minimum threshold
- [ ] Static analysis (SonarQube) passing
- [ ] Security-focused code review completed
- [ ] Documentation updated with security considerations

---

**Last Updated:** 2026-01-20
**Document Version:** 1.0
**Extension Version:** 2.0.16

For questions or concerns about this security policy, please contact security@kristasoft.com.
