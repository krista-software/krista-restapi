# DELETE with Custom Headers

## Overview

DELETE with Custom Headers enables secure and controlled resource deletion by including HTTP headers for authentication, authorization, audit trails, and deletion policies. This operation is essential for protected resources and enterprise-grade deletion operations.

## Use Cases

- Authenticated resource deletion
- Audit trail and compliance logging
- Conditional deletion with ETags
- Administrative override operations
- Soft delete with recovery options
- Bulk deletion with authorization

## Request Configuration

### Input Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| URL | Text | Yes | Target resource endpoint | `https://api.example.com/users/123` |
| Headers | List of Multi-field | Yes | HTTP headers | `[{key: "Authorization", value: "Bearer token"}]` |

### Output Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| Response Info | FreeForm | HTTP response metadata |
| Response | List Of FreeForm | Deletion confirmation and audit information |

## HTTP Request Format

```http
DELETE {URL} HTTP/1.1
Host: api.example.com
{Header-Name}: {Header-Value}
{Header-Name-2}: {Header-Value-2}
User-Agent: Krista-REST-Extension/2.0
```

## Common Header Types

### Authentication Headers

#### Bearer Token Authentication
```
[{key: "Authorization", value: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}]
```

#### API Key Authentication
```
[{key: "X-API-Key", value: "your-api-key-here"},
 {key: "X-API-Secret", value: "your-api-secret"}]
```

#### Admin Authentication
```
[{key: "Authorization", value: "Bearer admin_token"},
 {key: "X-Admin-Override", value: "true"},
 {key: "X-Admin-Reason", value: "Policy violation cleanup"}]
```

### Conditional Deletion Headers

#### ETag-Based Deletion
```
[{key: "If-Match", value: "\"version-abc123\""},
 {key: "Authorization", value: "Bearer token"}]
```

#### Timestamp-Based Conditions
```
[{key: "If-Unmodified-Since", value: "Mon, 15 Jan 2024 10:00:00 GMT"},
 {key: "Authorization", value: "Bearer token"}]
```

### Audit and Compliance Headers

#### Audit Trail Headers
```
[{key: "X-Audit-User", value: "admin@company.com"},
 {key: "X-Audit-Reason", value: "GDPR data deletion request"},
 {key: "X-Audit-Ticket", value: "TICKET-12345"}]
```

#### Compliance Headers
```
[{key: "X-Compliance-Policy", value: "GDPR"},
 {key: "X-Legal-Basis", value: "user_request"},
 {key: "X-Retention-Override", value: "true"}]
```

### Deletion Policy Headers

#### Soft Delete Control
```
[{key: "X-Delete-Type", value: "soft"},
 {key: "X-Recovery-Period", value: "30"},
 {key: "X-Backup-Required", value: "true"}]
```

#### Force Deletion
```
[{key: "X-Force-Delete", value: "true"},
 {key: "X-Ignore-Dependencies", value: "true"},
 {key: "X-Admin-Override", value: "confirmed"}]
```

## Example Usage

### Authenticated User Deletion

**Configuration:**
- URL: `https://api.example.com/users/12345`
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "X-Audit-User", value: "admin@company.com"},
   {key: "X-Audit-Reason", value: "Account termination"},
   {key: "X-Delete-Type", value: "soft"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/users/12345
Authorization: Bearer token123
X-Audit-User: admin@company.com
X-Audit-Reason: Account termination
X-Delete-Type: soft
```

**Expected Response:**
```json
{
  "message": "User account soft deleted",
  "user_id": 12345,
  "deletion_type": "soft",
  "deleted_by": "admin@company.com",
  "deletion_reason": "Account termination",
  "deleted_at": "2024-01-15T10:30:00Z",
  "recovery_deadline": "2024-02-15T10:30:00Z",
  "audit_trail_id": "audit_abc123"
}
```

### Conditional Document Deletion

**Configuration:**
- URL: `https://api.example.com/documents/789`
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "If-Match", value: "\"version-def456\""},
   {key: "X-Backup-Required", value: "true"},
   {key: "X-Notify-Owner", value: "true"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/documents/789
Authorization: Bearer token123
If-Match: "version-def456"
X-Backup-Required: true
X-Notify-Owner: true
```

**Expected Response:**
```json
{
  "message": "Document deleted successfully",
  "document_id": 789,
  "title": "Confidential Report",
  "version_deleted": "def456",
  "backup_location": "https://backup.example.com/docs/789",
  "owner_notified": true,
  "deleted_at": "2024-01-15T10:30:00Z"
}
```

### Administrative Override Deletion

**Configuration:**
- URL: `https://api.example.com/products/456`
- Headers:
  ```
  [{key: "Authorization", value: "Bearer admin_token"},
   {key: "X-Admin-Override", value: "true"},
   {key: "X-Force-Delete", value: "true"},
   {key: "X-Ignore-Dependencies", value: "true"},
   {key: "X-Admin-Reason", value: "Emergency product recall"}]
  ```

**Generated Request:**
```http
DELETE https://api.example.com/products/456
Authorization: Bearer admin_token
X-Admin-Override: true
X-Force-Delete: true
X-Ignore-Dependencies: true
X-Admin-Reason: Emergency product recall
```

### GDPR Compliance Deletion

**Configuration:**
- URL: `https://api.example.com/users/personal-data/98765`
- Headers:
  ```
  [{key: "Authorization", value: "Bearer token123"},
   {key: "X-Compliance-Policy", value: "GDPR"},
   {key: "X-Legal-Basis", value: "user_request"},
   {key: "X-Request-ID", value: "gdpr_req_12345"},
   {key: "X-Permanent-Delete", value: "true"}]
  ```

## Advanced Header Patterns

### Workflow Integration

#### Approval Workflow Headers
```
[{key: "X-Approval-Required", value: "false"},
 {key: "X-Approved-By", value: "manager@company.com"},
 {key: "X-Approval-Ticket", value: "APPROVAL-789"}]
```

#### Notification Headers
```
[{key: "X-Notify-Stakeholders", value: "true"},
 {key: "X-Notification-Template", value: "deletion_notice"},
 {key: "X-Notification-Priority", value: "high"}]
```

### Data Protection Headers

#### Encryption and Security
```
[{key: "X-Secure-Delete", value: "true"},
 {key: "X-Overwrite-Passes", value: "3"},
 {key: "X-Encryption-Key-Delete", value: "true"}]
```

#### Privacy Headers
```
[{key: "X-Privacy-Level", value: "high"},
 {key: "X-PII-Handling", value: "secure_delete"},
 {key: "X-Data-Classification", value: "confidential"}]
```

### Integration Headers

#### External System Sync
```
[{key: "X-Sync-External", value: "true"},
 {key: "X-Sync-Systems", value: "crm,erp,warehouse"},
 {key: "X-Sync-Timeout", value: "30"}]
```

#### Webhook Notifications
```
[{key: "X-Webhook-Notify", value: "true"},
 {key: "X-Webhook-URL", value: "https://webhook.example.com/deleted"},
 {key: "X-Webhook-Secret", value: "webhook_secret_123"}]
```

## Response Header Analysis

### Important Response Headers

#### Deletion Confirmation
- `X-Deletion-ID`: Unique deletion operation identifier
- `X-Deletion-Type`: Type of deletion performed (soft/hard)
- `X-Recovery-Possible`: Whether recovery is available

#### Audit Information
- `X-Audit-Trail-ID`: Audit log entry identifier
- `X-Compliance-Status`: Compliance requirement status
- `X-Legal-Hold-Check`: Legal hold verification result

#### Processing Information
- `X-Processing-Time`: Deletion operation duration
- `X-Dependencies-Checked`: Number of dependencies verified
- `X-Backup-Status`: Backup creation status

## Error Handling

### Authentication Errors

#### Invalid Token
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired authentication token",
  "code": 401,
  "token_status": "expired",
  "required_permissions": ["delete:users"]
}
```

#### Insufficient Permissions
```json
{
  "error": "Forbidden",
  "message": "Insufficient permissions for deletion operation",
  "code": 403,
  "required_permissions": ["admin:delete", "force:override"],
  "user_permissions": ["user:delete"]
}
```

### Conditional Deletion Errors

#### Version Mismatch
```json
{
  "error": "Precondition Failed",
  "message": "Resource version mismatch",
  "code": 412,
  "expected_etag": "\"version-def456\"",
  "current_etag": "\"version-ghi789\"",
  "last_modified_by": "another_user"
}
```

#### Resource Modified
```json
{
  "error": "Precondition Failed",
  "message": "Resource has been modified since specified time",
  "code": 412,
  "if_unmodified_since": "2024-01-15T10:00:00Z",
  "last_modified": "2024-01-15T10:15:00Z"
}
```

### Policy Violation Errors

#### Compliance Restriction
```json
{
  "error": "Policy Violation",
  "message": "Deletion violates data retention policy",
  "code": 422,
  "policy": "GDPR_RETENTION",
  "retention_period": "7 years",
  "earliest_deletion_date": "2030-01-15T00:00:00Z"
}
```

#### Legal Hold
```json
{
  "error": "Legal Hold Active",
  "message": "Resource is under legal hold and cannot be deleted",
  "code": 423,
  "legal_hold_id": "HOLD-12345",
  "hold_reason": "Litigation pending",
  "contact": "legal@company.com"
}
```

### Header Validation Errors

#### Missing Required Header
```json
{
  "error": "Bad Request",
  "message": "Missing required audit header",
  "code": 400,
  "missing_header": "X-Audit-Reason",
  "description": "Audit reason required for user deletions"
}
```

#### Invalid Header Format
```json
{
  "error": "Bad Request",
  "message": "Invalid header format",
  "code": 400,
  "header": "X-Delete-Type",
  "expected_values": ["soft", "hard"],
  "received": "permanent"
}
```

## Best Practices

### Security Headers
- Always include authentication for deletion operations
- Use admin override headers only when necessary
- Implement proper audit trail headers
- Include compliance and legal headers when required

### Conditional Deletion
- Use ETags for optimistic concurrency control
- Implement proper version checking
- Handle conflicts gracefully
- Provide clear error messages

### Audit and Compliance
- Include comprehensive audit information
- Log all deletion operations with context
- Implement compliance policy headers
- Maintain detailed audit trails

### Error Handling
- Validate all headers before processing
- Provide specific error messages
- Handle authentication failures gracefully
- Implement proper retry mechanisms

## Troubleshooting

### Authentication Issues
1. Verify token format and validity
2. Check required permissions for deletion
3. Ensure proper admin privileges when needed
4. Validate token expiration and refresh

### Conditional Deletion Failures
1. Check ETag or version header accuracy
2. Verify resource hasn't been modified
3. Implement proper conflict resolution
4. Handle concurrent deletion attempts

### Policy Violations
1. Review applicable data retention policies
2. Check for legal holds or restrictions
3. Verify compliance requirements
4. Implement proper override procedures

### Header Validation Problems
1. Validate header names and formats
2. Check for required vs. optional headers
3. Ensure proper header value encoding
4. Review API-specific header requirements

## Related Operations

- [Basic DELETE Request](pages/basicDelete.md) - Simple resource deletion
- [DELETE with Query Parameters](pages/deleteWithFilters.md) - Conditional deletion
- [Authentication Setup](pages/authentication.md) - Configure API authentication
- [Basic GET Request](pages/basicGet.md) - Verify resource before deletion

## Next Steps

After implementing DELETE with headers:
1. Combine with query parameters for advanced operations
2. Implement comprehensive audit logging
3. Set up compliance and legal hold checking
4. Add automated backup and recovery mechanisms
