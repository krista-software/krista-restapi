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

package app.krista.extensions.development.api.rest.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TestConnectionResponse} class.
 * <p>
 * Tests verify the TestConnectionResponse constructor, getter methods, and field values.
 * </p>
 */
class TestConnectionResponseTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithSuccessfulConnection_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(
                true,
                null,
                "https://api.example.com"
        );

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void testConstructor_WithFailedConnection_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "Connection timeout",
                "https://api.example.com"
        );

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_WithAllNullValues_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(false, null, null);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    // ========== Getter Method Tests ==========

    @Test
    void testIsSuccess_WithSuccessfulConnection_ShouldReturnTrue() {
        // Arrange
        TestConnectionResponse response = new TestConnectionResponse(
                true,
                null,
                "https://api.example.com"
        );

        // Act
        boolean isSuccess = response.isSuccess();

        // Assert
        assertTrue(isSuccess);
    }

    @Test
    void testIsSuccess_WithFailedConnection_ShouldReturnFalse() {
        // Arrange
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "Authentication failed",
                "https://api.example.com"
        );

        // Act
        boolean isSuccess = response.isSuccess();

        // Assert
        assertFalse(isSuccess);
    }

    // ========== Field Immutability Tests ==========

    @Test
    void testFields_ShouldBeFinal() {
        // Arrange
        TestConnectionResponse response = new TestConnectionResponse(
                true,
                "Test message",
                "https://test.com"
        );

        // Assert - verify fields maintain their values
        assertTrue(response.isSuccess());
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_SuccessfulApiConnection_ShouldCreateValidResponse() {
        // Arrange & Act - successful API connection
        TestConnectionResponse response = new TestConnectionResponse(
                true,
                null,
                "https://api.github.com"
        );

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void testConstructor_ConnectionTimeout_ShouldCreateValidResponse() {
        // Arrange & Act - connection timeout error
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "Connection timeout after 30 seconds",
                "https://api.slow-server.com"
        );

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_AuthenticationError_ShouldCreateValidResponse() {
        // Arrange & Act - authentication error
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "Invalid credentials: 401 Unauthorized",
                "https://api.secure-service.com"
        );

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_NetworkError_ShouldCreateValidResponse() {
        // Arrange & Act - network error
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "Network unreachable: Unable to resolve host",
                "https://api.nonexistent.com"
        );

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_SslError_ShouldCreateValidResponse() {
        // Arrange & Act - SSL certificate error
        TestConnectionResponse response = new TestConnectionResponse(
                false,
                "SSL certificate validation failed",
                "https://api.invalid-cert.com"
        );

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    // ========== Edge Case Tests ==========

    @Test
    void testConstructor_WithEmptyStrings_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(false, "", "");

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_WithLongErrorMessage_ShouldCreateInstance() {
        // Arrange
        String longMessage = "This is a very long error message that contains detailed information " +
                "about what went wrong during the connection attempt. It includes stack traces, " +
                "network diagnostics, and other debugging information that might be useful for " +
                "troubleshooting the connection issue.";

        // Act
        TestConnectionResponse response = new TestConnectionResponse(false, longMessage, "https://api.example.com");

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_WithSpecialCharactersInUrl_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(
                true,
                null,
                "https://api.example.com/v1/users?filter=active&sort=name"
        );

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    // ========== Multiple Instance Tests ==========

    @Test
    void testMultipleInstances_ShouldBeIndependent() {
        // Arrange & Act
        TestConnectionResponse response1 = new TestConnectionResponse(true, null, "https://api1.com");
        TestConnectionResponse response2 = new TestConnectionResponse(false, "Error", "https://api2.com");
        TestConnectionResponse response3 = new TestConnectionResponse(true, null, "https://api3.com");

        // Assert - all instances should be independent
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        assertTrue(response1.isSuccess());
        assertFalse(response2.isSuccess());
        assertTrue(response3.isSuccess());
    }

    // ========== Null Handling Tests ==========

    @Test
    void testConstructor_WithNullErrorMessage_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(true, null, "https://api.example.com");

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void testConstructor_WithNullUrl_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response = new TestConnectionResponse(false, "Error occurred", null);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void testConstructor_WithAllNullExceptSuccess_ShouldCreateInstance() {
        // Arrange & Act
        TestConnectionResponse response1 = new TestConnectionResponse(true, null, null);
        TestConnectionResponse response2 = new TestConnectionResponse(false, null, null);

        // Assert
        assertNotNull(response1);
        assertNotNull(response2);
        assertTrue(response1.isSuccess());
        assertFalse(response2.isSuccess());
    }
}

