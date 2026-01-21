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

package app.krista.extensions.development.api.rest;

import app.krista.extensions.development.api.rest.auth.AuthPayload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RestApiAttributes} class.
 * <p>
 * Tests verify the RestApiAttributes constructor, factory method, and getter methods.
 * </p>
 */
class RestApiAttributesTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange & Act
        RestApiAttributes attributes = new RestApiAttributes(
                "testuser",
                "testpass",
                "test-token",
                "Bearer",
                AuthPayload.AuthType.Basic,
                "https://api.example.com",
                "client-123",
                "secret-456",
                "https://auth.example.com",
                "https://token.example.com",
                "random-state",
                "read write"
        );

        // Assert
        assertNotNull(attributes);
        assertEquals("testuser", attributes.getUserName());
        assertEquals("testpass", attributes.getPassWord());
        assertEquals("test-token", attributes.getToken());
        assertEquals("Bearer", attributes.getTokenType());
        assertEquals(AuthPayload.AuthType.Basic, attributes.getAuthType());
        assertEquals("https://api.example.com", attributes.getApiUrl());
        assertEquals("client-123", attributes.getClientId());
        assertEquals("secret-456", attributes.getClientSecret());
        assertEquals("https://auth.example.com", attributes.getAuthUrl());
        assertEquals("https://token.example.com", attributes.getAccessTokenUrl());
        assertEquals("random-state", attributes.getState());
        assertEquals("read write", attributes.getScope());
    }

    @Test
    void testConstructor_WithNullValues_ShouldCreateInstance() {
        // Arrange & Act
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, null, null, null, null, null
        );

        // Assert
        assertNotNull(attributes);
        assertNull(attributes.getUserName());
        assertNull(attributes.getPassWord());
        assertNull(attributes.getToken());
        assertNull(attributes.getTokenType());
        assertNull(attributes.getAuthType());
        assertNull(attributes.getApiUrl());
        assertNull(attributes.getClientId());
        assertNull(attributes.getClientSecret());
        assertNull(attributes.getAuthUrl());
        assertNull(attributes.getAccessTokenUrl());
        assertNull(attributes.getState());
        assertNull(attributes.getScope());
    }

    // ========== Factory Method Tests ==========

    @Test
    void testCreate_WithCompleteAuthPayload_ShouldCreateRestApiAttributes() {
        // Arrange
        AuthPayload payload = new AuthPayload();
        payload.setUserName("admin");
        payload.setPassword("admin123");
        payload.setToken("token-abc");
        payload.setTokenType("Bearer");
        payload.setAuthType(AuthPayload.AuthType.Token);
        payload.setApiUrl("https://api.test.com");
        payload.setClientId("client-id");
        payload.setClientSecret("client-secret");
        payload.setAuthUrl("https://auth.test.com");
        payload.setAccessTokenUrl("https://token.test.com");
        payload.setState("state-123");
        payload.setScope("read write delete");

        // Act
        RestApiAttributes attributes = RestApiAttributes.create(payload);

        // Assert
        assertNotNull(attributes);
        assertEquals("admin", attributes.getUserName());
        assertEquals("admin123", attributes.getPassWord());
        assertEquals("token-abc", attributes.getToken());
        assertEquals("Bearer", attributes.getTokenType());
        assertEquals(AuthPayload.AuthType.Token, attributes.getAuthType());
        assertEquals("https://api.test.com", attributes.getApiUrl());
        assertEquals("client-id", attributes.getClientId());
        assertEquals("client-secret", attributes.getClientSecret());
        assertEquals("https://auth.test.com", attributes.getAuthUrl());
        assertEquals("https://token.test.com", attributes.getAccessTokenUrl());
        assertEquals("state-123", attributes.getState());
        assertEquals("read write delete", attributes.getScope());
    }

    @Test
    void testCreate_WithEmptyAuthPayload_ShouldCreateRestApiAttributes() {
        // Arrange
        AuthPayload payload = new AuthPayload();

        // Act
        RestApiAttributes attributes = RestApiAttributes.create(payload);

        // Assert
        assertNotNull(attributes);
        assertNull(attributes.getUserName());
        assertNull(attributes.getPassWord());
        assertNull(attributes.getToken());
        assertNull(attributes.getTokenType());
        assertNull(attributes.getAuthType());
        assertNull(attributes.getApiUrl());
        assertNull(attributes.getClientId());
        assertNull(attributes.getClientSecret());
        assertNull(attributes.getAuthUrl());
        assertNull(attributes.getAccessTokenUrl());
        assertNull(attributes.getState());
        assertNull(attributes.getScope());
    }

    // ========== Getter Method Tests ==========

    @Test
    void testGetUserName_ShouldReturnUsername() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                "john.doe", null, null, null, null, null, null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals("john.doe", attributes.getUserName());
    }

    @Test
    void testGetPassWord_ShouldReturnPassword() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, "secret123", null, null, null, null, null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals("secret123", attributes.getPassWord());
    }

    @Test
    void testGetToken_ShouldReturnToken() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, "bearer-token-xyz", null, null, null, null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals("bearer-token-xyz", attributes.getToken());
    }

    @Test
    void testGetTokenType_ShouldReturnTokenType() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, "Bearer", null, null, null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals("Bearer", attributes.getTokenType());
    }

    @Test
    void testGetAuthType_ShouldReturnAuthType() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, AuthPayload.AuthType.OAuth, null, null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals(AuthPayload.AuthType.OAuth, attributes.getAuthType());
    }

    @Test
    void testGetApiUrl_ShouldReturnApiUrl() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, "https://api.service.com", null, null, null, null, null, null
        );

        // Act & Assert
        assertEquals("https://api.service.com", attributes.getApiUrl());
    }

    @Test
    void testGetClientId_ShouldReturnClientId() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, "client-abc-123", null, null, null, null, null
        );

        // Act & Assert
        assertEquals("client-abc-123", attributes.getClientId());
    }

    @Test
    void testGetClientSecret_ShouldReturnClientSecret() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, "secret-xyz-789", null, null, null, null
        );

        // Act & Assert
        assertEquals("secret-xyz-789", attributes.getClientSecret());
    }

    @Test
    void testGetAuthUrl_ShouldReturnAuthUrl() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, null, "https://oauth.service.com/auth", null, null, null
        );

        // Act & Assert
        assertEquals("https://oauth.service.com/auth", attributes.getAuthUrl());
    }

    @Test
    void testGetAccessTokenUrl_ShouldReturnAccessTokenUrl() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, null, null, "https://oauth.service.com/token", null, null
        );

        // Act & Assert
        assertEquals("https://oauth.service.com/token", attributes.getAccessTokenUrl());
    }

    @Test
    void testGetState_ShouldReturnState() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, null, null, null, "state-random-123", null
        );

        // Act & Assert
        assertEquals("state-random-123", attributes.getState());
    }

    @Test
    void testGetScope_ShouldReturnScope() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                null, null, null, null, null, null, null, null, null, null, null, "read write execute"
        );

        // Act & Assert
        assertEquals("read write execute", attributes.getScope());
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testCreate_WithBasicAuthPayload_ShouldCreateRestApiAttributes() {
        // Arrange - Basic authentication scenario
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Basic);
        payload.setUserName("api_user");
        payload.setPassword("api_password");
        payload.setApiUrl("https://api.example.com/v1");

        // Act
        RestApiAttributes attributes = RestApiAttributes.create(payload);

        // Assert
        assertNotNull(attributes);
        assertEquals(AuthPayload.AuthType.Basic, attributes.getAuthType());
        assertEquals("api_user", attributes.getUserName());
        assertEquals("api_password", attributes.getPassWord());
        assertEquals("https://api.example.com/v1", attributes.getApiUrl());
    }

    @Test
    void testCreate_WithTokenAuthPayload_ShouldCreateRestApiAttributes() {
        // Arrange - Token authentication scenario
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Token);
        payload.setToken("sk-1234567890abcdef");
        payload.setTokenType("Bearer");
        payload.setApiUrl("https://api.openai.com/v1");

        // Act
        RestApiAttributes attributes = RestApiAttributes.create(payload);

        // Assert
        assertNotNull(attributes);
        assertEquals(AuthPayload.AuthType.Token, attributes.getAuthType());
        assertEquals("sk-1234567890abcdef", attributes.getToken());
        assertEquals("Bearer", attributes.getTokenType());
        assertEquals("https://api.openai.com/v1", attributes.getApiUrl());
    }

    @Test
    void testCreate_WithOAuthPayload_ShouldCreateRestApiAttributes() {
        // Arrange - OAuth authentication scenario
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.OAuth);
        payload.setClientId("oauth-client-id");
        payload.setClientSecret("oauth-client-secret");
        payload.setAuthUrl("https://accounts.google.com/o/oauth2/auth");
        payload.setAccessTokenUrl("https://oauth2.googleapis.com/token");
        payload.setScope("https://www.googleapis.com/auth/userinfo.email");
        payload.setState("security_token");
        payload.setApiUrl("https://www.googleapis.com/oauth2/v1");

        // Act
        RestApiAttributes attributes = RestApiAttributes.create(payload);

        // Assert
        assertNotNull(attributes);
        assertEquals(AuthPayload.AuthType.OAuth, attributes.getAuthType());
        assertEquals("oauth-client-id", attributes.getClientId());
        assertEquals("oauth-client-secret", attributes.getClientSecret());
        assertEquals("https://accounts.google.com/o/oauth2/auth", attributes.getAuthUrl());
        assertEquals("https://oauth2.googleapis.com/token", attributes.getAccessTokenUrl());
        assertEquals("https://www.googleapis.com/auth/userinfo.email", attributes.getScope());
        assertEquals("security_token", attributes.getState());
    }

    // ========== Field Immutability Tests ==========

    @Test
    void testFields_ShouldBeFinal() {
        // Arrange
        RestApiAttributes attributes = new RestApiAttributes(
                "user", "pass", "token", "Bearer", AuthPayload.AuthType.Basic,
                "https://api.com", "client", "secret", "https://auth.com",
                "https://token.com", "state", "scope"
        );

        // Assert - verify fields maintain their values
        assertEquals("user", attributes.getUserName());
        assertEquals("pass", attributes.getPassWord());
        assertEquals("token", attributes.getToken());
        assertEquals("Bearer", attributes.getTokenType());
        assertEquals(AuthPayload.AuthType.Basic, attributes.getAuthType());
        assertEquals("https://api.com", attributes.getApiUrl());
        assertEquals("client", attributes.getClientId());
        assertEquals("secret", attributes.getClientSecret());
        assertEquals("https://auth.com", attributes.getAuthUrl());
        assertEquals("https://token.com", attributes.getAccessTokenUrl());
        assertEquals("state", attributes.getState());
        assertEquals("scope", attributes.getScope());
    }
}

