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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ErrorMessages} utility class.
 * <p>
 * Tests verify that all error message methods return non-null, non-empty strings
 * with appropriate content and formatting.
 * </p>
 */
class ErrorMessagesTest {

    // ========== Connection Error Tests ==========

    @Test
    void testConnectionError_WithValidUrl_ShouldReturnFormattedMessage() {
        // Arrange
        String url = "https://api.example.com/users";

        // Act
        String errorMessage = ErrorMessages.connectionError(url);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(url), "Error message should contain the URL");
        assertTrue(errorMessage.contains("Unable to connect"), "Error message should mention connection failure");
    }

    @Test
    void testTimeoutError_WithValidUrl_ShouldReturnFormattedMessage() {
        // Arrange
        String url = "https://api.example.com/users";

        // Act
        String errorMessage = ErrorMessages.timeoutError(url);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(url), "Error message should contain the URL");
        assertTrue(errorMessage.contains("timed out"), "Error message should mention timeout");
    }

    // ========== Authentication Error Tests ==========

    @Test
    void testNoAuthenticationConfigured_ShouldReturnFormattedMessage() {
        // Act
        String errorMessage = ErrorMessages.noAuthenticationConfigured();

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains("authentication"), "Error message should mention authentication");
    }

    @Test
    void testOAuthAuthorizationFailed_WithReason_ShouldReturnFormattedMessage() {
        // Arrange
        String reason = "Invalid client credentials";

        // Act
        String errorMessage = ErrorMessages.oauthAuthorizationFailed(reason);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(reason), "Error message should contain the reason");
        assertTrue(errorMessage.contains("OAuth"), "Error message should mention OAuth");
    }

    // ========== Request/Response Error Tests ==========

    @Test
    void testApiRequestFailed_WithStatusCodeAndMessage_ShouldReturnFormattedMessage() {
        // Arrange
        int statusCode = 404;
        String statusMessage = "Not Found";
        String responseBody = "{\"error\": \"Resource not found\"}";

        // Act
        String errorMessage = ErrorMessages.apiRequestFailed(statusCode, statusMessage, responseBody);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(String.valueOf(statusCode)), "Error message should contain status code");
        assertTrue(errorMessage.contains(statusMessage), "Error message should contain status message");
    }

    @Test
    void testEmptyResponseBody_WithStatusCode_ShouldReturnFormattedMessage() {
        // Arrange
        int statusCode = 204;
        String statusMessage = "No Content";

        // Act
        String errorMessage = ErrorMessages.emptyResponseBody(statusCode, statusMessage);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(String.valueOf(statusCode)), "Error message should contain status code");
    }

    @Test
    void testResponseBodyNull_ShouldReturnFormattedMessage() {
        // Act
        String errorMessage = ErrorMessages.responseBodyNull();

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains("empty"), "Error message should mention empty response");
    }

    @Test
    void testFailedToWriteFile_ShouldReturnFormattedMessage() {
        // Act
        String errorMessage = ErrorMessages.failedToWriteFile();

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains("Failed to save"), "Error message should mention save failure");
    }

    // ========== Validation Error Tests ==========

    @Test
    void testInvalidUrl_WithUrl_ShouldReturnFormattedMessage() {
        // Arrange
        String url = "not-a-valid-url";

        // Act
        String errorMessage = ErrorMessages.invalidUrl(url);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(url), "Error message should contain the invalid URL");
    }

    @Test
    void testInvalidHttpMethod_WithMethod_ShouldReturnFormattedMessage() {
        // Arrange
        String method = "INVALID";

        // Act
        String errorMessage = ErrorMessages.invalidHttpMethod(method);

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains(method), "Error message should contain the invalid method");
    }

    @Test
    void testInvalidJsonPayload_ShouldReturnFormattedMessage() {
        // Act
        String errorMessage = ErrorMessages.invalidJsonPayload();

        // Assert
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        assertTrue(errorMessage.contains("JSON"), "Error message should mention JSON");
    }

    // ========== Edge Cases Tests ==========

    @Test
    void testConnectionError_WithNullUrl_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> ErrorMessages.connectionError(null));
    }

    @Test
    void testTimeoutError_WithEmptyUrl_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> ErrorMessages.timeoutError(""));
    }

    @Test
    void testOAuthAuthorizationFailed_WithNullReason_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> ErrorMessages.oauthAuthorizationFailed(null));
    }

    @Test
    void testApiRequestFailed_WithNullValues_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> ErrorMessages.apiRequestFailed(500, null, null));
    }

    // ========== Message Content Tests ==========

    @Test
    void testAllErrorMessages_ShouldContainRecommendedActions() {
        // Test that key error messages contain "Recommended actions"
        assertTrue(ErrorMessages.connectionError("http://test.com").contains("Recommended actions"));
        assertTrue(ErrorMessages.timeoutError("http://test.com").contains("Recommended actions"));
        assertTrue(ErrorMessages.noAuthenticationConfigured().contains("Recommended actions"));
        assertTrue(ErrorMessages.oauthAuthorizationFailed("test").contains("Recommended actions"));
    }

    @Test
    void testAllErrorMessages_ShouldContainPossibleCauses() {
        // Test that key error messages contain "Possible causes"
        assertTrue(ErrorMessages.connectionError("http://test.com").contains("Possible causes"));
        assertTrue(ErrorMessages.timeoutError("http://test.com").contains("Possible causes"));
        assertTrue(ErrorMessages.responseBodyNull().contains("Possible causes"));
    }
}

