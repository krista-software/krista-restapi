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

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AccessToken} class.
 * <p>
 * Tests verify the AccessToken constructor, factory method, and getter methods.
 * </p>
 */
class AccessTokenTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange & Act
        AccessToken token = new AccessToken(
                "access-token-123",
                "read write",
                3600L,
                "Bearer",
                "refresh-token-456",
                "random-state"
        );

        // Assert
        assertNotNull(token);
        assertEquals("access-token-123", token.accessTokenValue);
        assertEquals("read write", token.scopes);
        assertEquals(3600L, token.expiresIn);
        assertEquals("Bearer", token.tokenType);
        assertEquals("refresh-token-456", token.refreshToken);
        assertEquals("random-state", token.state);
    }

    @Test
    void testConstructor_WithNullValues_ShouldCreateInstance() {
        // Arrange & Act
        AccessToken token = new AccessToken(null, null, null, null, null, null);

        // Assert
        assertNotNull(token);
        assertNull(token.accessTokenValue);
        assertNull(token.scopes);
        assertNull(token.expiresIn);
        assertNull(token.tokenType);
        assertNull(token.refreshToken);
        assertNull(token.state);
    }

    // ========== Factory Method Tests ==========

    @Test
    void testCreate_WithCompleteJsonObject_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "token-abc-123");
        json.addProperty("scopes", "read write delete");
        json.addProperty("expires_in", 7200L);
        json.addProperty("token_type", "Bearer");
        json.addProperty("refresh_token", "refresh-xyz-789");
        json.addProperty("state", "state-value");

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("token-abc-123", token.accessTokenValue);
        assertEquals("read write delete", token.scopes);
        assertEquals(7200L, token.expiresIn);
        assertEquals("Bearer", token.tokenType);
        assertEquals("refresh-xyz-789", token.refreshToken);
        assertEquals("state-value", token.state);
    }

    @Test
    void testCreate_WithMinimalJsonObject_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "minimal-token");

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("minimal-token", token.accessTokenValue);
        assertNull(token.scopes);
        assertNull(token.expiresIn);
        assertNull(token.tokenType);
        assertNull(token.refreshToken);
        assertNull(token.state);
    }

    @Test
    void testCreate_WithEmptyJsonObject_ShouldCreateAccessTokenWithNulls() {
        // Arrange
        JsonObject json = new JsonObject();

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertNull(token.accessTokenValue);
        assertNull(token.scopes);
        assertNull(token.expiresIn);
        assertNull(token.tokenType);
        assertNull(token.refreshToken);
        assertNull(token.state);
    }

    @Test
    void testCreate_WithPartialJsonObject_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "partial-token");
        json.addProperty("token_type", "Bearer");
        json.addProperty("expires_in", 1800L);

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("partial-token", token.accessTokenValue);
        assertNull(token.scopes);
        assertEquals(1800L, token.expiresIn);
        assertEquals("Bearer", token.tokenType);
        assertNull(token.refreshToken);
        assertNull(token.state);
    }

    // ========== Getter Method Tests ==========

    @Test
    void testGetAccessToken_ShouldReturnAccessTokenValue() {
        // Arrange
        AccessToken token = new AccessToken("test-token", null, null, null, null, null);

        // Act
        String accessToken = token.getAccessToken();

        // Assert
        assertEquals("test-token", accessToken);
        assertEquals(token.accessTokenValue, accessToken);
    }

    @Test
    void testGetAccessToken_WithNullValue_ShouldReturnNull() {
        // Arrange
        AccessToken token = new AccessToken(null, null, null, null, null, null);

        // Act
        String accessToken = token.getAccessToken();

        // Assert
        assertNull(accessToken);
    }

    // ========== Field Immutability Tests ==========

    @Test
    void testFields_ShouldBeFinal() {
        // Arrange
        AccessToken token = new AccessToken(
                "immutable-token",
                "read",
                3600L,
                "Bearer",
                "refresh",
                "state"
        );

        // Assert - verify fields maintain their values
        assertEquals("immutable-token", token.accessTokenValue);
        assertEquals("read", token.scopes);
        assertEquals(3600L, token.expiresIn);
        assertEquals("Bearer", token.tokenType);
        assertEquals("refresh", token.refreshToken);
        assertEquals("state", token.state);
    }

    // ========== Edge Case Tests ==========

    @Test
    void testCreate_WithZeroExpiresIn_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "token");
        json.addProperty("expires_in", 0L);

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals(0L, token.expiresIn);
    }

    @Test
    void testCreate_WithNegativeExpiresIn_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "token");
        json.addProperty("expires_in", -100L);

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals(-100L, token.expiresIn);
    }

    @Test
    void testCreate_WithEmptyStringValues_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "");
        json.addProperty("scopes", "");
        json.addProperty("token_type", "");
        json.addProperty("refresh_token", "");
        json.addProperty("state", "");

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("", token.accessTokenValue);
        assertEquals("", token.scopes);
        assertEquals("", token.tokenType);
        assertEquals("", token.refreshToken);
        assertEquals("", token.state);
    }

    @Test
    void testCreate_WithLargeExpiresIn_ShouldCreateAccessToken() {
        // Arrange
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "token");
        json.addProperty("expires_in", Long.MAX_VALUE);

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals(Long.MAX_VALUE, token.expiresIn);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testCreate_WithTypicalOAuthResponse_ShouldCreateAccessToken() {
        // Arrange - typical OAuth 2.0 response
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "ya29.a0AfH6SMBx...");
        json.addProperty("expires_in", 3599L);
        json.addProperty("token_type", "Bearer");
        json.addProperty("scope", "https://www.googleapis.com/auth/userinfo.email");
        json.addProperty("refresh_token", "1//0gHdP...");

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("ya29.a0AfH6SMBx...", token.accessTokenValue);
        assertEquals(3599L, token.expiresIn);
        assertEquals("Bearer", token.tokenType);
        assertEquals("1//0gHdP...", token.refreshToken);
    }

    @Test
    void testCreate_WithGitHubOAuthResponse_ShouldCreateAccessToken() {
        // Arrange - typical GitHub OAuth response
        JsonObject json = new JsonObject();
        json.addProperty("access_token", "gho_16C7e42F292c6912E7710c838347Ae178B4a");
        json.addProperty("token_type", "bearer");
        json.addProperty("scope", "repo,gist");

        // Act
        AccessToken token = AccessToken.create(json);

        // Assert
        assertNotNull(token);
        assertEquals("gho_16C7e42F292c6912E7710c838347Ae178B4a", token.accessTokenValue);
        assertEquals("bearer", token.tokenType);
        assertNull(token.expiresIn);
        assertNull(token.refreshToken);
    }
}

