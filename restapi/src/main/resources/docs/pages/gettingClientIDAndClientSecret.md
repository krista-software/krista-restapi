# Google OAuth 2.0 Setup Guide

## Overview

This comprehensive guide walks you through creating a Google Cloud project and configuring OAuth 2.0 credentials to integrate Google services with the Krista REST API Extension.

## Prerequisites

- Google account with access to Google Cloud Console
- Basic understanding of OAuth 2.0 concepts
- Administrative permissions for your intended Google services

## Step-by-Step Setup Process

### Step 1: Access Google Cloud Console

1. Navigate to the [Google Cloud Console](https://console.cloud.google.com/getting-started)
2. Sign in with your Google account

### Step 2: Create or Select a Project

#### Select Existing Project
1. Click **Select a project** in the top navigation bar

![Select Project](../_media/gettingClientIDAndClientSecret_1.png)

2. Choose from your existing projects if available

![Project Selection](../_media/gettingClientIDAndClientSecret_2.png)

#### Create New Project
1. If you need a new project, click **NEW PROJECT**
2. Configure your project:
   - **Project name**: Enter a descriptive name (e.g., "Krista API Integration")
   - **Organization**: Select your organization (if applicable)
   - **Location**: Choose appropriate billing account or organization
3. Click **CREATE**

![Create New Project](../_media/gettingClientIDAndClientSecret_3.png)

### Step 3: Enable Required APIs

Before creating OAuth credentials, enable the Google APIs your integration will use.

1. In the left sidebar, navigate to **APIs & Services**
2. Click **Enabled APIs and services**

![APIs and Services Navigation](../_media/gettingClientIDAndClientSecret_4.png)
![APIs and Services Page](../_media/gettingClientIDAndClientSecret_5.png)

3. Click **+ ENABLE APIS AND SERVICES**

![Enable APIs Button](../_media/gettingClientIDAndClientSecret_6.png)

4. Search for and enable the APIs you need:

#### Common Google APIs for Integration
- **Google Drive API**: File storage and management
- **Gmail API**: Email access and management
- **Google Calendar API**: Calendar events and scheduling
- **Google Sheets API**: Spreadsheet data access
- **Google Cloud Storage API**: Cloud storage operations

![Enable Required APIs](../_media/gettingClientIDAndClientSecret_7.png)

### Step 4: Configure OAuth Consent Screen

The OAuth consent screen is what users see when granting permissions to your application.

1. Navigate to **APIs & Services** > **Credentials**
2. Click **CONFIGURE CONSENT SCREEN**

![Credentials Page](../_media/gettingClientIDAndClientSecret_8.png)
![Configure Consent Screen](../_media/gettingClientIDAndClientSecret_9.png)

#### Choose User Type
Select the appropriate user type for your application:

- **Internal**: For Google Workspace organizations only
- **External**: For any Google account (requires verification for production)

![User Type Selection](../_media/gettingClientIDAndClientSecret_10.png)

#### Configure OAuth Consent Screen Details

Complete the OAuth consent screen configuration:

**App Information**
- **App name**: Name shown to users during consent
- **User support email**: Contact email for user support
- **App logo**: Optional logo for your application

**App Domain Information**
- **Application home page**: Your application's main URL
- **Application privacy policy link**: Link to privacy policy
- **Application terms of service link**: Link to terms of service

**Developer Contact Information**
- **Developer contact emails**: Email addresses for Google to contact you

![OAuth Consent Screen Form 1](../_media/gettingClientIDAndClientSecret_11.png)
![OAuth Consent Screen Form 2](../_media/gettingClientIDAndClientSecret_12.png)

Click **SAVE AND CONTINUE** to proceed.

#### Configure Scopes

Add the OAuth scopes (permissions) your application needs:

1. On the Scopes page, click **ADD OR REMOVE SCOPES**
2. Select the appropriate scopes for your integration:

**Common Google API Scopes**
- `https://www.googleapis.com/auth/drive.readonly`: Read-only Drive access
- `https://www.googleapis.com/auth/gmail.readonly`: Read-only Gmail access
- `https://www.googleapis.com/auth/calendar.readonly`: Read-only Calendar access
- `https://www.googleapis.com/auth/userinfo.profile`: Basic profile information

![Scopes Configuration](../_media/gettingClientIDAndClientSecret_13.png)

3. Click **SAVE AND CONTINUE**

#### Configure Test Users (External Apps Only)

For external applications in testing mode, add test user email addresses:

![Test Users Configuration](../_media/gettingClientIDAndClientSecret_15.png)

#### Review Summary

Review your OAuth consent screen configuration and click **SAVE AND CONTINUE**

![Configuration Summary](../_media/gettingClientIDAndClientSecret_16.png)

### Step 5: Create OAuth 2.0 Credentials

Now create the OAuth 2.0 client credentials:

1. Navigate to **APIs & Services** > **Credentials**
2. Click **+ CREATE CREDENTIALS**
3. Select **OAuth client ID**

![Create Credentials](../_media/gettingClientIDAndClientSecret_17.png)
![OAuth Client ID Selection](../_media/gettingClientIDAndClientSecret_18.png)
  

#### Configure OAuth Client Details

Configure your OAuth 2.0 client with the following settings:

1. **Application Type**: Select **Web application**
2. **Name**: Enter a descriptive name for your OAuth client
3. **Authorized redirect URIs**: Add your Krista REST API Extension callback URL

**Redirect URI Format**: `https://your-krista-domain/rest/callback`

![Application Type Selection](../_media/gettingClientIDAndClientSecret_19.png)
![Authorized Redirect URIs](../_media/gettingClientIDAndClientSecret_20.png)

4. Click **CREATE** to generate your credentials

#### Retrieve Client Credentials

After creation, a popup will display your OAuth 2.0 credentials:

![Client ID and Client Secret](../_media/gettingClientIDAndClientSecret_21.png)

**Important**: Copy and securely store both values:
- **Client ID**: Used to identify your application
- **Client Secret**: Used to authenticate your application (keep confidential)

## Configuration Summary

After completing these steps, you should have:

| Credential | Description | Usage |
|------------|-------------|-------|
| **Client ID** | Public identifier for your application | Used in OAuth authorization requests |
| **Client Secret** | Private key for your application | Used in token exchange requests |
| **Project ID** | Google Cloud project identifier | Reference for API quotas and billing |

## Security Best Practices

### Credential Management
- **Secure Storage**: Store client secrets in secure, encrypted storage
- **Access Control**: Limit access to credentials to authorized personnel only
- **Regular Rotation**: Consider rotating client secrets periodically
- **Environment Separation**: Use different credentials for development and production

### OAuth Configuration
- **Minimal Scopes**: Request only the permissions your application actually needs
- **Redirect URI Validation**: Ensure redirect URIs are exact matches
- **HTTPS Only**: Always use HTTPS for redirect URIs in production
- **State Parameter**: Use state parameter for CSRF protection

## Common Google API Scopes

Choose appropriate scopes based on your integration requirements:

### Google Drive
- `https://www.googleapis.com/auth/drive.readonly`: Read-only access to Drive files
- `https://www.googleapis.com/auth/drive.file`: Access to files created by the app
- `https://www.googleapis.com/auth/drive`: Full access to Drive

### Gmail
- `https://www.googleapis.com/auth/gmail.readonly`: Read-only access to Gmail
- `https://www.googleapis.com/auth/gmail.send`: Send emails on behalf of user
- `https://www.googleapis.com/auth/gmail.modify`: Read, send, delete, and manage Gmail

### Google Calendar
- `https://www.googleapis.com/auth/calendar.readonly`: Read-only access to Calendar
- `https://www.googleapis.com/auth/calendar.events`: Manage calendar events
- `https://www.googleapis.com/auth/calendar`: Full access to Calendar

### User Information
- `https://www.googleapis.com/auth/userinfo.profile`: Basic profile information
- `https://www.googleapis.com/auth/userinfo.email`: User's email address
- `openid`: OpenID Connect authentication

## Next Steps

With your Google OAuth credentials ready, proceed to:
- [Connect Google Services to Krista REST API Extension](pages/connectingGoogleWithRestApiExtension.md)
- [Authentication Configuration Guide](pages/authentication.md)

## Troubleshooting

### Common Issues

#### API Not Enabled
- **Symptom**: OAuth flow works but API calls fail
- **Solution**: Ensure required Google APIs are enabled in your project

#### Invalid Redirect URI
- **Symptom**: OAuth flow fails with redirect URI error
- **Solution**: Verify redirect URI in Google Console matches Krista configuration exactly

#### Insufficient Permissions
- **Symptom**: API calls return permission denied errors
- **Solution**: Review and update OAuth scopes in your consent screen configuration

#### Quota Exceeded
- **Symptom**: API calls fail with quota exceeded errors
- **Solution**: Check API quotas in Google Cloud Console and request increases if needed

For additional support, refer to the [Google Cloud Documentation](https://cloud.google.com/docs) or contact your Google Cloud administrator.
  