# Authentication

## Overview

The Krista REST API Extension provides robust authentication capabilities to securely connect with external APIs. Choose from three authentication methods based on your target API's requirements and security standards.

## Authentication Methods

### Basic Authentication

Basic Authentication uses a username and password combination transmitted via HTTP headers. This method is simple to implement and widely supported across various APIs.

#### When to Use Basic Auth
- Internal APIs with simple security requirements
- Legacy systems that don't support modern authentication
- Development and testing environments
- APIs that require straightforward username/password authentication

#### Configuration Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Username | Text | Yes | Your account username or API key | `api_user` |
| Password | Text | Yes | Your account password or secret | `secure_password123` |
| Auth URL | URL | Yes | GET endpoint to validate credentials | `https://api.example.com/auth/validate` |

#### Security Considerations
- Credentials are base64 encoded (not encrypted)
- Use HTTPS endpoints only to protect credentials in transit
- Consider token-based auth for production environments
- Regularly rotate passwords for enhanced security

### Token-Based Authentication

Token-based authentication uses pre-generated tokens instead of username/password combinations. This method provides better security and is commonly used in modern APIs.

#### When to Use Token Auth
- APIs that provide API keys or access tokens
- Services requiring Bearer token authentication
- Systems with token-based security models
- Production environments requiring enhanced security

#### Configuration Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Token | Text | Yes | Your API token or key | `sk-1234567890abcdef` |
| Token Type | Text | Yes | Token prefix for Authorization header | `Bearer`, `Token`, `API-Key` |
| Auth URL | URL | Yes | GET endpoint to validate token | `https://api.example.com/user/profile` |

#### Token Types
- **Bearer**: Most common format (`Authorization: Bearer <token>`)
- **API-Key**: Custom header format (`Authorization: API-Key <token>`)
- **Token**: Simple token format (`Authorization: Token <token>`)
- **Custom**: Use custom token type as needed

#### Security Best Practices
- Store tokens securely and never expose in logs
- Use tokens with appropriate scope limitations
- Implement token rotation when supported
- Monitor token usage for suspicious activity

### OAuth 2.0 Authentication

OAuth 2.0 provides secure, delegated access to user accounts on third-party services. This method is ideal for integrating with major platforms like Microsoft and Google.

#### When to Use OAuth 2.0
- Integration with Microsoft Graph, Google APIs, or other OAuth providers
- Applications requiring user consent for data access
- Multi-tenant applications with user-specific data
- Services requiring fine-grained permission control

#### Configuration Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| Auth URL | URL | Yes | OAuth authorization endpoint |
| Access Token URL | URL | Yes | Token exchange endpoint |
| Client ID | Text | Yes | Application identifier from OAuth provider |
| Client Secret | Text | Yes | Application secret from OAuth provider |
| Scope | Text | Optional | Requested permissions (e.g., `User.Read Mail.Read`) |
| State | Text | Optional | Security parameter for CSRF protection |
| Auth Verification URL | URL | Yes | Endpoint to test authenticated access |

**Example Values:**
- **Auth URL**: `https://login.microsoftonline.com/{tenant}/oauth2/v2.0/authorize`
- **Access Token URL**: `https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token`
- **Client ID**: `12345678-1234-1234-1234-123456789012`
- **Auth Verification URL**: `https://graph.microsoft.com/v1.0/me`

#### OAuth Flow Process
1. **Authorization Request**: User is redirected to OAuth provider
2. **User Consent**: User grants permissions to your application
3. **Authorization Code**: Provider returns authorization code
4. **Token Exchange**: Code is exchanged for access token
5. **API Access**: Access token is used for authenticated requests
6. **Token Refresh**: Automatic token renewal when supported

## Platform-Specific OAuth Examples

### Microsoft OAuth 2.0 Configuration

Microsoft Graph API integration requires specific OAuth 2.0 endpoints and scopes. Here's a complete configuration example:

| Parameter | Value | Description |
|-----------|-------|-------------|
| **Auth URL** | `https://login.microsoftonline.com/{tenant}/oauth2/v2.0/authorize` | Replace `{tenant}` with your Azure AD tenant ID |
| **Access Token URL** | `https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token` | Token exchange endpoint for your tenant |
| **Client ID** | `12345678-1234-1234-1234-123456789012` | Application ID from Azure App Registration |
| **Client Secret** | `your_client_secret_here` | Secret generated in Azure App Registration |
| **Scope** | `User.Read Mail.Read Files.Read` | Space-separated list of Microsoft Graph permissions |
| **State** | `random_security_string` | Optional CSRF protection parameter |
| **Auth Verification URL** | `https://graph.microsoft.com/v1.0/me` | Endpoint to verify successful authentication |

#### Common Microsoft Graph Scopes
- `User.Read`: Read user profile information
- `Mail.Read`: Read user's email messages
- `Files.Read`: Read user's files in OneDrive
- `Calendars.Read`: Read user's calendar events
- `Directory.Read.All`: Read directory data (admin consent required)

### Google OAuth 2.0 Configuration

Google APIs use standardized OAuth 2.0 endpoints with service-specific scopes. Here's a complete configuration example:

| Parameter | Value | Description |
|-----------|-------|-------------|
| **Auth URL** | `https://accounts.google.com/o/oauth2/v2/auth` | Google's OAuth 2.0 authorization endpoint |
| **Access Token URL** | `https://oauth2.googleapis.com/token` | Google's token exchange endpoint |
| **Client ID** | `123456789012-abcdefghijklmnop.apps.googleusercontent.com` | Client ID from Google Cloud Console |
| **Client Secret** | `your_google_client_secret` | Client secret from Google Cloud Console |
| **Scope** | `https://www.googleapis.com/auth/drive.readonly` | URL-formatted Google API scope |
| **State** | `random_security_string` | Optional CSRF protection parameter |
| **Auth Verification URL** | `https://www.googleapis.com/drive/v3/about?fields=user` | Endpoint to verify successful authentication |

#### Common Google API Scopes
- `https://www.googleapis.com/auth/drive.readonly`: Read-only access to Google Drive
- `https://www.googleapis.com/auth/gmail.readonly`: Read-only access to Gmail
- `https://www.googleapis.com/auth/calendar.readonly`: Read-only access to Google Calendar
- `https://www.googleapis.com/auth/spreadsheets`: Read/write access to Google Sheets
- `https://www.googleapis.com/auth/userinfo.profile`: Access to user profile information

## Advanced Configuration

### Custom Headers
For APIs requiring additional authentication headers beyond standard methods:

```http
Authorization: Bearer your_token_here
X-API-Key: your_api_key
X-Custom-Auth: custom_value
```

> **Note**: Custom headers can be configured in individual API requests when standard authentication methods are insufficient.

### Authentication Troubleshooting

#### Common Issues
1. **Invalid Credentials**: Verify username/password or token accuracy
2. **Expired Tokens**: Check token expiration and refresh if needed
3. **Insufficient Permissions**: Ensure proper scopes are configured
4. **Network Connectivity**: Verify HTTPS endpoints are accessible
5. **OAuth Redirect Mismatch**: Confirm redirect URIs match exactly

#### Testing Authentication
Always use the **Test Connection** feature to validate your authentication setup before deploying to production.

## Security Best Practices

- **Use HTTPS Only**: Never send credentials over unencrypted connections
- **Rotate Credentials**: Regularly update passwords and regenerate tokens
- **Limit Scope**: Request only the minimum permissions required
- **Monitor Access**: Track API usage and watch for suspicious activity
- **Secure Storage**: Store credentials securely and never expose in logs or code

## Troubleshooting Authentication Issues

### Common Authentication Errors

#### No Authentication Configured
If you see an error about missing authentication, follow these steps:
1. Open the **Authentication** tab in the extension configuration
2. Select an authentication type (Basic, OAuth 2.0, or Token)
3. Provide the required credentials
4. Click **Save** and use **Test Connection** to verify

#### OAuth Authorization Failed
If OAuth authorization fails:
- Verify your Client ID and Client Secret are correct
- Ensure the OAuth callback URL matches your provider's configuration
- Check that your OAuth application has the required permissions
- Re-authorize through the Authentication tab

#### Invalid Credentials
If you receive authentication errors:
- Verify credentials are correct and haven't expired
- Use the **Test Connection** button to validate
- Regenerate API keys or tokens if necessary
- Check the API provider's documentation for requirements

For detailed troubleshooting guidance, see our [Troubleshooting Guide](pages/troubleshooting.md).

## Next Steps

- [Microsoft OAuth Setup Guide](pages/obtainingClientIDClientSecret.md)
- [Google OAuth Setup Guide](pages/gettingClientIDAndClientSecret.md)
- [API Request Documentation](pages/supportedRequests.md)
- [Troubleshooting Guide](pages/troubleshooting.md)
