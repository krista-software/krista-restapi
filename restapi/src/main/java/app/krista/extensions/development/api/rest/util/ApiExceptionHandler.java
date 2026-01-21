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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Centralized exception handler for REST API operations.
 * Usage example:
 * <pre>
 * try {
 *     // API call
 * } catch (Exception e) {
 *     ApiExceptionHandler.Result result = ApiExceptionHandler.handle(e, url);
 *     logger.error(result.getLogMessage());
 *     return result.getUserMessage();
 * }
 * </pre>
 * </p>
 *
 * @see ErrorMessages
 */
public final class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ApiExceptionHandler() {
        // Utility class - prevent instantiation
    }

    /**
     * Handles an exception and returns a structured result with user-friendly messages.
     *
     * @param exception the exception to handle
     * @param url       the API URL that was being called (may be null)
     * @return a Result object containing user message, log message, and metadata
     */
    public static Result handle(Exception exception, String url) {
        return handle(exception, url, null);
    }

    /**
     * Handles an exception with additional context and returns a structured result.
     *
     * @param exception the exception to handle
     * @param url       the API URL that was being called (may be null)
     * @param operation the operation being performed (e.g., "fetching data", "uploading file")
     * @return a Result object containing user message, log message, and metadata
     */
    public static Result handle(Exception exception, String url, String operation) {
        if (exception == null) {
            return createResult(
                    ExceptionCategory.UNKNOWN,
                    "An unknown error occurred.",
                    "Null exception received",
                    url,
                    operation,
                    false
            );
        }

        String safeUrl = url != null ? url : "unknown URL";
        String safeOperation = operation != null ? operation : "API request";

        // Categorize and handle the exception
        if (exception instanceof HttpResponseException) {
            return handleHttpResponse((HttpResponseException) exception, safeUrl, safeOperation);
        } else if (exception instanceof UnknownHostException) {
            return handleUnknownHost(exception, safeUrl, safeOperation);
        } else if (exception instanceof SSLHandshakeException) {
            return handleSslHandshake(exception, safeUrl, safeOperation);
        } else if (exception instanceof SSLException) {
            return handleSsl(exception, safeUrl, safeOperation);
        } else if (exception instanceof ConnectException) {
            return handleConnection(exception, safeUrl, safeOperation);
        } else if (exception instanceof SocketTimeoutException) {
            return handleTimeout(exception, safeUrl, safeOperation);
        } else if (exception instanceof MalformedURLException) {
            return handleMalformedUrl(exception, safeUrl, safeOperation);
        } else if (exception instanceof IOException) {
            return handleIoException(exception, safeUrl, safeOperation);
        } else if (exception instanceof IllegalArgumentException) {
            return handleIllegalArgument(exception, safeUrl, safeOperation);
        } else if (exception instanceof SecurityException) {
            return handleSecurity(exception, safeUrl, safeOperation);
        } else if (exception instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            return handleInterrupted(exception, safeUrl, safeOperation);
        } else {
            return handleGeneric(exception, safeUrl, safeOperation);
        }
    }

    /**
     * Logs the exception with appropriate level based on category.
     *
     * @param result the Result from handle() method
     */
    public static void log(Result result) {
        switch (result.getCategory()) {
            case NETWORK:
            case TIMEOUT:
                LOGGER.warn("[API Warning] {}", result.getLogMessage());
                break;
            case AUTHENTICATION:
            case SSL:
            case SECURITY:
                LOGGER.error("[API Security] {}", result.getLogMessage());
                break;
            case VALIDATION:
                LOGGER.info("[API Validation] {}", result.getLogMessage());
                break;
            case SERVER_ERROR:
            case UNKNOWN:
            default:
                LOGGER.error("[API Error] {}", result.getLogMessage());
                break;
        }
    }

    /**
     * Convenience method to handle and log an exception in one call.
     *
     * @param exception the exception to handle
     * @param url       the API URL
     * @return the Result object
     */
    public static Result handleAndLog(Exception exception, String url) {
        Result result = handle(exception, url);
        log(result);
        return result;
    }

    /**
     * Convenience method to handle and log an exception with operation context.
     *
     * @param exception the exception to handle
     * @param url       the API URL
     * @param operation the operation being performed
     * @return the Result object
     */
    public static Result handleAndLog(Exception exception, String url, String operation) {
        Result result = handle(exception, url, operation);
        log(result);
        return result;
    }

    // ========== Exception Handlers ==========

    private static Result handleHttpResponse(HttpResponseException e, String url, String operation) {
        int statusCode = e.getStatusCode();
        String statusMessage = e.getStatusMessage();

        ExceptionCategory category;
        boolean retryable;
        String logPrefix;

        if (e.isUnauthorized()) {
            category = ExceptionCategory.AUTHENTICATION;
            retryable = false;
            logPrefix = "Authentication Failed";
        } else if (e.isForbidden()) {
            category = ExceptionCategory.AUTHENTICATION;
            retryable = false;
            logPrefix = "Access Forbidden";
        } else if (e.isNotFound()) {
            category = ExceptionCategory.VALIDATION;
            retryable = false;
            logPrefix = "Resource Not Found";
        } else if (e.isClientError()) {
            category = ExceptionCategory.VALIDATION;
            retryable = false;
            logPrefix = "Client Error";
        } else if (e.isServerError()) {
            category = ExceptionCategory.SERVER_ERROR;
            retryable = true;
            logPrefix = "Server Error";
        } else {
            category = ExceptionCategory.UNKNOWN;
            retryable = true;
            logPrefix = "HTTP Error";
        }

        String userMessage = e.getMessage();
        String logMessage = String.format(
                "%s: HTTP %d %s while %s with '%s'. Response: %s",
                logPrefix, statusCode, statusMessage, operation, extractHost(url),
                e.getResponseBody() != null ? truncate(e.getResponseBody(), 200) : "No response body");

        return createResult(category, userMessage, logMessage, url, operation, retryable);
    }

    private static Result handleUnknownHost(Exception e, String url, String operation) {
        String host = extractHost(url);
        String userMessage = String.format(
                "Cannot reach the server at: %s\n\n" +
                        "Possible causes:\n" +
                        "• The hostname is incorrect or misspelled\n" +
                        "• DNS resolution failed\n" +
                        "• The server does not exist\n" +
                        "• Network connectivity issues\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify the URL hostname is correct\n" +
                        "2. Check your internet connection\n" +
                        "3. Try accessing the URL in a browser\n" +
                        "4. Contact your network administrator if the issue persists",
                host
        );
        String logMessage = String.format(
                "Server not found: Unable to reach '%s' while %s. " +
                        "The server hostname could not be resolved. Please verify the URL is correct and your network connection is working.",
                host, operation);
        return createResult(ExceptionCategory.NETWORK, userMessage, logMessage, url, operation, true);
    }

    private static Result handleSslHandshake(Exception e, String url, String operation) {
        String userMessage = String.format(
                "SSL/TLS handshake failed for: %s\n\n" +
                        "Possible causes:\n" +
                        "• The server's SSL certificate is invalid or expired\n" +
                        "• Certificate is not trusted by your system\n" +
                        "• SSL/TLS version mismatch\n" +
                        "• Man-in-the-middle attack (security concern)\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify the server's SSL certificate is valid\n" +
                        "2. Check if the certificate has expired\n" +
                        "3. Ensure your system trusts the certificate authority\n" +
                        "4. Contact your API provider about certificate issues",
                url
        );
        String logMessage = String.format(
                "SSL Certificate Error: Failed to establish secure connection to '%s' while %s. " +
                        "The server's SSL certificate may be invalid, expired, or not trusted. " +
                        "Please verify the certificate or contact your API provider.",
                extractHost(url), operation);
        return createResult(ExceptionCategory.SSL, userMessage, logMessage, url, operation, false);
    }

    private static Result handleSsl(Exception e, String url, String operation) {
        String userMessage = String.format(
                "SSL/TLS error while connecting to: %s\n\n" +
                        "Possible causes:\n" +
                        "• SSL/TLS protocol error\n" +
                        "• Connection was interrupted during SSL handshake\n" +
                        "• Incompatible SSL/TLS versions\n\n" +
                        "Recommended actions:\n" +
                        "1. Retry the request\n" +
                        "2. Verify the server supports modern TLS versions\n" +
                        "3. Check your network connection stability\n" +
                        "4. Contact your API provider if the issue persists",
                url
        );
        String logMessage = String.format(
                "Secure Connection Failed: Could not establish SSL/TLS connection to '%s' while %s. " +
                        "This may be a temporary network issue. Please retry the request.",
                extractHost(url), operation);
        return createResult(ExceptionCategory.SSL, userMessage, logMessage, url, operation, true);
    }

    private static Result handleConnection(Exception e, String url, String operation) {
        String userMessage = ErrorMessages.connectionError(url);
        String logMessage = String.format(
                "Connection Refused: Unable to connect to '%s' while %s. " +
                        "The server may be down, the port may be blocked, or the URL may be incorrect. " +
                        "Please verify the server is running and accessible.",
                extractHost(url), operation);
        return createResult(ExceptionCategory.NETWORK, userMessage, logMessage, url, operation, true);
    }

    private static Result handleTimeout(Exception e, String url, String operation) {
        String userMessage = ErrorMessages.timeoutError(url);
        String logMessage = String.format(
                "Request Timeout: The server '%s' did not respond in time while %s. " +
                        "The server may be overloaded or experiencing issues. Please try again later.",
                extractHost(url), operation);
        return createResult(ExceptionCategory.TIMEOUT, userMessage, logMessage, url, operation, true);
    }

    private static Result handleMalformedUrl(Exception e, String url, String operation) {
        String userMessage = ErrorMessages.invalidUrl(url);
        String logMessage = String.format(
                "Invalid URL Format: The URL '%s' is not valid while %s. " +
                        "Please check the URL format and ensure it starts with http:// or https://",
                url, operation);
        return createResult(ExceptionCategory.VALIDATION, userMessage, logMessage, url, operation, false);
    }



    private static Result handleIoException(Exception e, String url, String operation) {
        String message = e.getMessage();
        String userMessage;
        if (message != null && !message.isBlank()) {
            userMessage = message;
        } else {
            userMessage = String.format(
                    "An I/O error occurred while %s.\n\n" +
                            "URL: %s\n\n" +
                            "Recommended actions:\n" +
                            "1. Check your network connection\n" +
                            "2. Verify the API endpoint is accessible\n" +
                            "3. Retry the request\n" +
                            "4. Contact support if the issue persists",
                    operation, url
            );
        }
        String logMessage = String.format(
                "Network Error: A communication error occurred while %s with '%s'. " +
                        "Please check your network connection and try again.",
                operation, extractHost(url));
        return createResult(ExceptionCategory.NETWORK, userMessage, logMessage, url, operation, true);
    }

    private static Result handleIllegalArgument(Exception e, String url, String operation) {
        String message = e.getMessage();
        String userMessage = message != null && !message.isBlank() ? message :
                "Invalid request parameters. Please check your input and try again.";
        String logMessage = String.format(
                "Invalid Input: The request for %s failed due to invalid parameters. " +
                        "Please review the input values and correct any errors. Details: %s",
                operation, message != null ? message : "No additional details available");
        return createResult(ExceptionCategory.VALIDATION, userMessage, logMessage, url, operation, false);
    }

    private static Result handleSecurity(Exception e, String url, String operation) {
        String userMessage = String.format(
                "Security error while accessing: %s\n\n" +
                        "Possible causes:\n" +
                        "• Insufficient permissions\n" +
                        "• Security policy violation\n" +
                        "• Access denied by security manager\n\n" +
                        "Recommended actions:\n" +
                        "1. Verify you have the required permissions\n" +
                        "2. Check security policies and configurations\n" +
                        "3. Contact your system administrator",
                url
        );
        String logMessage = String.format(
                "Access Denied: Security restrictions prevented %s to '%s'. " +
                        "Please verify you have the required permissions or contact your administrator.",
                operation, extractHost(url));
        return createResult(ExceptionCategory.SECURITY, userMessage, logMessage, url, operation, false);
    }

    private static Result handleInterrupted(Exception e, String url, String operation) {
        String userMessage = String.format(
                "The %s was interrupted.\n\n" +
                        "The operation did not complete. Please try again.",
                operation
        );
        String logMessage = String.format(
                "Operation Cancelled: The %s was interrupted before completion. " +
                        "This may happen due to system shutdown or manual cancellation. Please retry if needed.",
                operation);
        return createResult(ExceptionCategory.UNKNOWN, userMessage, logMessage, url, operation, true);
    }

    private static Result handleGeneric(Exception e, String url, String operation) {
        String message = e.getMessage();
        String userMessage = String.format(
                "An unexpected error occurred while %s.\n\n" +
                        "Error: %s\n\n" +
                        "Recommended actions:\n" +
                        "1. Retry the request\n" +
                        "2. Check the API endpoint and parameters\n" +
                        "3. Review the application logs for more details\n" +
                        "4. Contact support if the issue persists",
                operation,
                message != null ? message : "Unknown error"
        );
        String logMessage = String.format(
                "Unexpected Error: Something went wrong while %s with '%s'. " +
                        "Error details: %s. Please retry or contact support if the issue persists.",
                operation, extractHost(url), message != null ? message : "No details available");
        return createResult(ExceptionCategory.UNKNOWN, userMessage, logMessage, url, operation, true);
    }

    // ========== Helper Methods ==========

    private static Result createResult(ExceptionCategory category, String userMessage,
                                        String logMessage, String url, String operation, boolean retryable) {
        return new Result(category, userMessage, logMessage, url, operation, retryable);
    }

    private static String extractHost(String url) {
        if (url == null || url.isBlank()) {
            return "unknown";
        }
        try {
            String withoutProtocol = url.replaceFirst("^https?://", "");
            int slashIndex = withoutProtocol.indexOf('/');
            int colonIndex = withoutProtocol.indexOf(':');
            int endIndex = withoutProtocol.length();
            if (slashIndex > 0) endIndex = Math.min(endIndex, slashIndex);
            if (colonIndex > 0) endIndex = Math.min(endIndex, colonIndex);
            return withoutProtocol.substring(0, endIndex);
        } catch (Exception e) {
            return url;
        }
    }

    private static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    // ========== Inner Classes ==========

    /**
     * Categories of exceptions for appropriate handling and logging.
     */
    public enum ExceptionCategory {
        /** Network-related errors (connection refused, host unreachable) */
        NETWORK,
        /** Timeout errors (socket timeout, connection timeout) */
        TIMEOUT,
        /** SSL/TLS related errors */
        SSL,
        /** Authentication and authorization errors */
        AUTHENTICATION,
        /** Security policy violations */
        SECURITY,
        /** Input validation errors */
        VALIDATION,
        /** Server-side errors (5xx) */
        SERVER_ERROR,
        /** Unknown or uncategorized errors */
        UNKNOWN
    }

    /**
     * Result object containing structured exception handling information.
     */
    public static final class Result {
        private final ExceptionCategory category;
        private final String userMessage;
        private final String logMessage;
        private final String url;
        private final String operation;
        private final boolean retryable;

        Result(ExceptionCategory category, String userMessage, String logMessage,
               String url, String operation, boolean retryable) {
            this.category = category;
            this.userMessage = userMessage;
            this.logMessage = logMessage;
            this.url = url;
            this.operation = operation;
            this.retryable = retryable;
        }

        /** Returns the exception category for handling decisions. */
        public ExceptionCategory getCategory() {
            return category;
        }

        /** Returns a user-friendly message suitable for display. */
        public String getUserMessage() {
            return userMessage;
        }

        /** Returns a detailed message suitable for logging. */
        public String getLogMessage() {
            return logMessage;
        }

        /** Returns the URL that was being accessed. */
        public String getUrl() {
            return url;
        }

        /** Returns the operation that was being performed. */
        public String getOperation() {
            return operation;
        }

        /** Returns true if the operation can be retried. */
        public boolean isRetryable() {
            return retryable;
        }

        @Override
        public String toString() {
            return String.format("Result{category=%s, retryable=%s, url='%s', operation='%s'}",
                    category, retryable, url, operation);
        }
    }
}

