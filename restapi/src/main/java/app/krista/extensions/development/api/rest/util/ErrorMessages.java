/*
 * RestApi Extension for Krista
 * Copyright (C) 2024 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.development.api.rest.util;

/**
 * Centralized repository for user-friendly error messages used throughout the REST API Extension.
 * <p>
 * This class provides consistent, actionable error messages that help users understand what went wrong
 * and how to fix the issue. All error messages follow these principles:
 * <ul>
 *   <li>Clear description of what went wrong</li>
 *   <li>Actionable steps to resolve the issue</li>
 *   <li>User-friendly language without technical jargon where possible</li>
 *   <li>Specific details to help with troubleshooting</li>
 * </ul>
 * </p>
 *
 * @see RestApiConstants
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Utility class - prevent instantiation
    }

    // ========== Connection Errors ==========

    /**
     * Formats a connection error message with actionable steps.
     *
     * @param url the URL that failed to connect
     * @return formatted error message
     */
    public static String connectionError(String url) {
        return String.format(
                "Unable to connect to the API endpoint: %s\n\n" +
                        "Possible causes:\n" +
                        "• The API server may be down or unreachable\n" +
                        "• The URL might be incorrect\n" +
                        "• Network connectivity issues\n" +
                        "• Firewall or proxy blocking the connection\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify the URL is correct and the server is running\n" +
                        "2. Check your network connection\n" +
                        "3. Ensure firewall/proxy settings allow access to this endpoint\n" +
                        "4. Contact your API provider if the issue persists",
                url
        );
    }

    /**
     * Formats a timeout error message with actionable steps.
     *
     * @param url the URL that timed out
     * @return formatted error message
     */
    public static String timeoutError(String url) {
        return String.format(
                "Request timed out while connecting to: %s\n\n" +
                        "Possible causes:\n" +
                        "• The API server is taking too long to respond\n" +
                        "• Network latency or slow connection\n" +
                        "• The request payload is too large\n" +
                        "• Server is overloaded or experiencing issues\n\n" +
                        "Recommended actions:\n" +
                        "1. Retry the request after a few moments\n" +
                        "2. Check if the API endpoint is experiencing high load\n" +
                        "3. Reduce the request payload size if applicable\n" +
                        "4. Contact your API provider if timeouts persist",
                url
        );
    }

    // ========== Authentication Errors ==========

    /**
     * Returns an error message for missing authentication configuration.
     *
     * @return formatted error message
     */
    public static String noAuthenticationConfigured() {
        return "No authentication type selected.\n\n" +
                "Recommended actions:\n" +
                "1. Open the Authentication tab in the extension configuration\n" +
                "2. Select an authentication type (Basic, OAuth 2.0, or Token)\n" +
                "3. Provide the required credentials\n" +
                "4. Save the configuration and try again";
    }

    /**
     * Returns an error message for failed OAuth authorization.
     *
     * @param reason the reason for authorization failure
     * @return formatted error message
     */
    public static String oauthAuthorizationFailed(String reason) {
        return String.format(
                "OAuth authorization failed: %s\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify your Client ID and Client Secret are correct\n" +
                        "2. Ensure the OAuth callback URL is properly configured\n" +
                        "3. Check that your OAuth application has the required permissions\n" +
                        "4. Re-authorize the application through the Authentication tab\n" +
                        "5. Contact your OAuth provider if the issue persists",
                reason
        );
    }

    // ========== Request/Response Errors ==========

    /**
     * Formats an API request failure message with status code details.
     *
     * @param statusCode the HTTP status code
     * @param statusMessage the HTTP status message
     * @param responseBody the response body (may contain error details)
     * @return formatted error message
     */
    public static String apiRequestFailed(int statusCode, String statusMessage, String responseBody) {
        String userFriendlyMessage = getUserFriendlyStatusMessage(statusCode);
        return String.format(
                "API request failed with status %d: %s\n\n" +
                        "%s\n\n" +
                        "Response details: %s\n\n" +
                        "Recommended actions:\n" +
                        "%s",
                statusCode,
                statusMessage,
                userFriendlyMessage,
                responseBody,
                getRecommendedActionsForStatusCode(statusCode)
        );
    }

    /**
     * Returns an error message for empty response body.
     *
     * @param statusCode the HTTP status code
     * @param statusMessage the HTTP status message
     * @return formatted error message
     */
    public static String emptyResponseBody(int statusCode, String statusMessage) {
        return String.format(
                "The API returned an empty response (Status: %d %s)\n\n" +
                        "Possible causes:\n" +
                        "• The API endpoint returned no data\n" +
                        "• The requested resource may not exist\n" +
                        "• The API may have encountered an internal error\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify the API endpoint URL is correct\n" +
                        "2. Check if the requested resource exists\n" +
                        "3. Review the API documentation for expected response format\n" +
                        "4. Contact your API provider if the issue persists",
                statusCode,
                statusMessage
        );
    }

    /**
     * Returns an error message for response body being null during file conversion.
     *
     * @return formatted error message
     */
    public static String responseBodyNull() {
        return "Unable to download file: The API response is empty.\n\n" +
                "Possible causes:\n" +
                "• The file does not exist at the specified endpoint\n" +
                "• The API returned an error without a response body\n" +
                "• Network issues interrupted the download\n\n" +
                "Recommended actions:\n" +
                "1. Verify the file URL is correct\n" +
                "2. Check if the file exists on the server\n" +
                "3. Review API logs for any error messages\n" +
                "4. Try the request again";
    }

    /**
     * Returns an error message for failed file write operations.
     *
     * @return formatted error message
     */
    public static String failedToWriteFile() {
        return "Failed to save the downloaded file to disk.\n\n" +
                "Possible causes:\n" +
                "• Insufficient disk space\n" +
                "• Permission issues with the temporary directory\n" +
                "• File system errors\n\n" +
                "Recommended actions:\n" +
                "1. Check available disk space\n" +
                "2. Verify write permissions for the application\n" +
                "3. Contact your system administrator if the issue persists";
    }

    // ========== Data Size Errors ==========

    /**
     * Formats an error message for response size exceeding limits.
     *
     * @param actualSize the actual response size
     * @param maxSize the maximum allowed size
     * @return formatted error message
     */
    public static String responseSizeTooLarge(long actualSize, long maxSize) {
        double actualSizeMB = actualSize / (1024.0 * 1024.0);
        double maxSizeMB = maxSize / (1024.0 * 1024.0);
        return String.format(
                "Response size (%.2f MB) exceeds the maximum allowed size (%.2f MB)\n\n" +
                        "Recommended actions:\n" +
                        "1. Use the 'Get with Pagination' request to retrieve data in smaller chunks\n" +
                        "2. Add filters to reduce the amount of data returned\n" +
                        "3. Request specific fields instead of all data\n" +
                        "4. Contact your API provider about pagination support",
                actualSizeMB,
                maxSizeMB
        );
    }

    /**
     * Formats an error message for paginated results exceeding limits.
     *
     * @param recordCount the number of records
     * @param limit the maximum allowed records
     * @return formatted error message
     */
    public static String paginatedResultsTooLarge(int recordCount, int limit) {
        return String.format(
                "The number of records (%d) exceeds the limit of %d per page.\n\n" +
                        "Recommended actions:\n" +
                        "1. Reduce the page size parameter\n" +
                        "2. Retrieve data across multiple pages\n" +
                        "3. Add filters to narrow down the results\n" +
                        "4. Use a smaller page size (recommended: 100-500 records)",
                recordCount,
                limit
        );
    }

    // ========== Validation Errors ==========

    /**
     * Returns an error message for invalid URL format.
     *
     * @param url the invalid URL
     * @return formatted error message
     */
    public static String invalidUrl(String url) {
        return String.format(
                "Invalid URL format: %s\n\n" +
                        "Recommended actions:\n" +
                        "1. Ensure the URL starts with http:// or https://\n" +
                        "2. Check for typos in the URL\n" +
                        "3. Verify the URL format matches: https://api.example.com/endpoint\n" +
                        "4. Remove any invalid characters or spaces",
                url
        );
    }

    /**
     * Returns an error message for invalid HTTP method.
     *
     * @param method the invalid method
     * @return formatted error message
     */
    public static String invalidHttpMethod(String method) {
        return String.format(
                "Invalid HTTP method: %s\n\n" +
                        "Supported methods: GET, POST, PUT, PATCH, DELETE\n\n" +
                        "Recommended actions:\n" +
                        "1. Use one of the supported HTTP methods\n" +
                        "2. Check the API documentation for the correct method\n" +
                        "3. Verify the method name is spelled correctly",
                method
        );
    }

    /**
     * Returns an error message for invalid JSON payload.
     *
     * @return formatted error message
     */
    public static String invalidJsonPayload() {
        return "Invalid JSON format in request payload.\n\n" +
                "Recommended actions:\n" +
                "1. Validate your JSON using a JSON validator tool\n" +
                "2. Check for missing commas, brackets, or quotes\n" +
                "3. Ensure all strings are properly quoted\n" +
                "4. Verify the JSON structure matches the API requirements";
    }

    /**
     * Formats an error message for invalid payload type.
     *
     * @param actualType the actual type received
     * @return formatted error message
     */
    public static String invalidPayloadType(String actualType) {
        return String.format(
                "Invalid payload type: %s\n\n" +
                        "Expected: String (JSON format) or File\n\n" +
                        "Recommended actions:\n" +
                        "1. Ensure the payload is a valid JSON string\n" +
                        "2. For file uploads, use the file parameter\n" +
                        "3. Check the API documentation for expected payload format",
                actualType
        );
    }

    // ========== Catalog Request Errors ==========

    /**
     * Formats a generic catalog request error with context.
     *
     * @param operation the operation that failed (e.g., "reading data", "writing data")
     * @param details additional error details
     * @return formatted error message
     */
    public static String catalogRequestError(String operation, String details) {
        return String.format(
                "Error while %s\n\n" +
                        "Details: %s\n\n" +
                        "Recommended actions:\n" +
                        "1. Check the API endpoint URL and parameters\n" +
                        "2. Verify authentication credentials are valid\n" +
                        "3. Review the API documentation for correct usage\n" +
                        "4. Check the application logs for more details\n" +
                        "5. Contact support if the issue persists",
                operation,
                details
        );
    }

    // ========== Helper Methods ==========

    private static String getUserFriendlyStatusMessage(int statusCode) {
        if (statusCode >= 400 && statusCode < 500) {
            return "Client Error: There's an issue with the request.";
        } else if (statusCode >= 500) {
            return "Server Error: The API server encountered an error.";
        }
        return "The request was not successful.";
    }

    private static String getRecommendedActionsForStatusCode(int statusCode) {
        switch (statusCode) {
            case 400:
                return "1. Check the request parameters and payload format\n" +
                        "2. Ensure all required fields are provided\n" +
                        "3. Validate the data types match API requirements";
            case 401:
                return "1. Verify your authentication credentials are correct\n" +
                        "2. Check if your access token has expired\n" +
                        "3. Re-authenticate through the Authentication tab";
            case 403:
                return "1. Verify you have permission to access this resource\n" +
                        "2. Check if your API key has the required scopes\n" +
                        "3. Contact your API administrator for access";
            case 404:
                return "1. Verify the API endpoint URL is correct\n" +
                        "2. Check if the resource exists\n" +
                        "3. Review the API documentation for the correct endpoint";
            case 429:
                return "1. Wait a few moments before retrying\n" +
                        "2. Reduce the frequency of API requests\n" +
                        "3. Check your API rate limits\n" +
                        "4. Consider implementing exponential backoff";
            case 500:
            case 502:
            case 503:
                return "1. Retry the request after a few moments\n" +
                        "2. Check the API status page for known issues\n" +
                        "3. Contact your API provider if the issue persists";
            default:
                return "1. Review the response details above\n" +
                        "2. Check the API documentation\n" +
                        "3. Contact your API provider for assistance";
        }
    }
}

