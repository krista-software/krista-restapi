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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ApiExceptionHandler}.
 */
@DisplayName("ApiExceptionHandler Tests")
class ApiExceptionHandlerTest {

    private static final String TEST_URL = "https://api.example.com/endpoint";
    private static final String TEST_OPERATION = "fetching data";

    @Nested
    @DisplayName("handle() method tests")
    class HandleMethodTests {

        @Test
        @DisplayName("Should handle null exception gracefully")
        void testHandleNullException() {
            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(null, TEST_URL);

            // Assert
            assertNotNull(result);
            assertEquals(ApiExceptionHandler.ExceptionCategory.UNKNOWN, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getUserMessage().contains("unknown error"));
        }

        @Test
        @DisplayName("Should handle null URL gracefully")
        void testHandleNullUrl() {
            // Arrange
            Exception exception = new IOException("Test error");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, null);

            // Assert
            assertNotNull(result);
            assertNotNull(result.getLogMessage());
            assertTrue(result.getLogMessage().contains("unknown URL"));
        }

        @Test
        @DisplayName("Should handle UnknownHostException")
        void testHandleUnknownHostException() {
            // Arrange
            Exception exception = new UnknownHostException("api.example.com");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.NETWORK, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getUserMessage().contains("Cannot reach the server"));
            assertTrue(result.getUserMessage().contains("DNS resolution failed"));
        }

        @Test
        @DisplayName("Should handle SSLHandshakeException")
        void testHandleSslHandshakeException() {
            // Arrange
            Exception exception = new SSLHandshakeException("Certificate validation failed");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.SSL, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getUserMessage().contains("SSL/TLS handshake failed"));
            assertTrue(result.getUserMessage().contains("certificate"));
        }

        @Test
        @DisplayName("Should handle SSLException")
        void testHandleSslException() {
            // Arrange
            Exception exception = new SSLException("SSL protocol error");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.SSL, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getUserMessage().contains("SSL/TLS error"));
        }

        @Test
        @DisplayName("Should handle ConnectException")
        void testHandleConnectException() {
            // Arrange
            Exception exception = new ConnectException("Connection refused");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.NETWORK, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getUserMessage().contains("Unable to connect"));
        }

        @Test
        @DisplayName("Should handle SocketTimeoutException")
        void testHandleSocketTimeoutException() {
            // Arrange
            Exception exception = new SocketTimeoutException("Read timed out");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.TIMEOUT, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getUserMessage().contains("timed out"));
        }

        @Test
        @DisplayName("Should handle MalformedURLException")
        void testHandleMalformedUrlException() {
            // Arrange
            Exception exception = new MalformedURLException("Invalid URL format");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, "not-a-valid-url");

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.VALIDATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getUserMessage().contains("Invalid URL"));
        }

        @Test
        @DisplayName("Should handle IOException with message")
        void testHandleIoExceptionWithMessage() {
            // Arrange
            String errorMessage = "Custom IO error message";
            Exception exception = new IOException(errorMessage);

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.NETWORK, result.getCategory());
            assertTrue(result.isRetryable());
            assertEquals(errorMessage, result.getUserMessage());
        }

        @Test
        @DisplayName("Should handle IllegalArgumentException")
        void testHandleIllegalArgumentException() {
            // Arrange
            String errorMessage = "Invalid parameter value";
            Exception exception = new IllegalArgumentException(errorMessage);

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.VALIDATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertEquals(errorMessage, result.getUserMessage());
        }

        @Test
        @DisplayName("Should handle SecurityException")
        void testHandleSecurityException() {
            // Arrange
            Exception exception = new SecurityException("Access denied");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.SECURITY, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getUserMessage().contains("Security error"));
        }

        @Test
        @DisplayName("Should handle generic RuntimeException")
        void testHandleGenericException() {
            // Arrange
            Exception exception = new RuntimeException("Unexpected error");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.UNKNOWN, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getUserMessage().contains("unexpected error"));
        }

        @Test
        @DisplayName("Should include operation in log message")
        void testOperationIncludedInLogMessage() {
            // Arrange
            Exception exception = new IOException("Test error");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertTrue(result.getLogMessage().contains(TEST_OPERATION));
            // Log message contains the host extracted from URL for readability
            assertTrue(result.getLogMessage().contains("api.example.com"));
        }

        @Test
        @DisplayName("Should handle HttpResponseException with 404 Not Found")
        void testHandleHttpResponseException404() {
            // Arrange
            HttpResponseException exception = new HttpResponseException(
                    404, "Not Found", "{\"error\": \"Resource not found\"}", "API request failed with status 404");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.VALIDATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getLogMessage().contains("Resource Not Found"));
            assertTrue(result.getLogMessage().contains("404"));
        }

        @Test
        @DisplayName("Should handle HttpResponseException with 401 Unauthorized")
        void testHandleHttpResponseException401() {
            // Arrange
            HttpResponseException exception = new HttpResponseException(
                    401, "Unauthorized", "{\"error\": \"Invalid credentials\"}", "API request failed with status 401");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.AUTHENTICATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getLogMessage().contains("Authentication Failed"));
        }

        @Test
        @DisplayName("Should handle HttpResponseException with 500 Server Error")
        void testHandleHttpResponseException500() {
            // Arrange
            HttpResponseException exception = new HttpResponseException(
                    500, "Internal Server Error", "{\"error\": \"Server error\"}", "API request failed with status 500");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.SERVER_ERROR, result.getCategory());
            assertTrue(result.isRetryable());
            assertTrue(result.getLogMessage().contains("Server Error"));
        }

        @Test
        @DisplayName("Should handle HttpResponseException with 403 Forbidden")
        void testHandleHttpResponseException403() {
            // Arrange
            HttpResponseException exception = new HttpResponseException(
                    403, "Forbidden", "{\"error\": \"Access denied\"}", "API request failed with status 403");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.AUTHENTICATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getLogMessage().contains("Access Forbidden"));
        }

        @Test
        @DisplayName("Should handle HttpResponseException with 400 Bad Request")
        void testHandleHttpResponseException400() {
            // Arrange
            HttpResponseException exception = new HttpResponseException(
                    400, "Bad Request", "{\"error\": \"Invalid input\"}", "API request failed with status 400");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertEquals(ApiExceptionHandler.ExceptionCategory.VALIDATION, result.getCategory());
            assertFalse(result.isRetryable());
            assertTrue(result.getLogMessage().contains("Client Error"));
        }
    }

    @Nested
    @DisplayName("Result class tests")
    class ResultClassTests {

        @Test
        @DisplayName("Result should contain all properties")
        void testResultProperties() {
            // Arrange
            Exception exception = new ConnectException("Connection refused");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertNotNull(result.getCategory());
            assertNotNull(result.getUserMessage());
            assertNotNull(result.getLogMessage());
            assertEquals(TEST_URL, result.getUrl());
            assertEquals(TEST_OPERATION, result.getOperation());
        }

        @Test
        @DisplayName("Result toString should be informative")
        void testResultToString() {
            // Arrange
            Exception exception = new IOException("Test");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handle(exception, TEST_URL, TEST_OPERATION);
            String toString = result.toString();

            // Assert
            assertTrue(toString.contains("Result{"));
            assertTrue(toString.contains("category="));
            assertTrue(toString.contains("retryable="));
        }
    }

    @Nested
    @DisplayName("handleAndLog() method tests")
    class HandleAndLogTests {

        @Test
        @DisplayName("handleAndLog should return valid result")
        void testHandleAndLog() {
            // Arrange
            Exception exception = new SocketTimeoutException("Timeout");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(exception, TEST_URL);

            // Assert
            assertNotNull(result);
            assertEquals(ApiExceptionHandler.ExceptionCategory.TIMEOUT, result.getCategory());
        }

        @Test
        @DisplayName("handleAndLog with operation should return valid result")
        void testHandleAndLogWithOperation() {
            // Arrange
            Exception exception = new ConnectException("Connection refused");

            // Act
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(exception, TEST_URL, TEST_OPERATION);

            // Assert
            assertNotNull(result);
            assertEquals(TEST_OPERATION, result.getOperation());
        }
    }
}

