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
 * Unit tests for the {@link AuthPayload} class and its nested {@link AuthPayload.AuthType} enum.
 * <p>
 * Tests verify the AuthType enum values and AuthPayload getter/setter methods.
 * </p>
 */
class AuthPayloadTest {

    // ========== AuthType Enum Tests ==========

    @Test
    void testAuthType_ShouldHaveBasicValue() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.Basic;
        assertNotNull(authType, "Basic auth type should not be null");
        assertEquals("Basic", authType.name());
    }

    @Test
    void testAuthType_ShouldHaveTokenValue() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.Token;
        assertNotNull(authType, "Token auth type should not be null");
        assertEquals("Token", authType.name());
    }

    @Test
    void testAuthType_ShouldHaveOAuthValue() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.OAuth;
        assertNotNull(authType, "OAuth auth type should not be null");
        assertEquals("OAuth", authType.name());
    }

    @Test
    void testAuthType_ShouldHaveExactlyThreeValues() {
        AuthPayload.AuthType[] values = AuthPayload.AuthType.values();
        assertEquals(3, values.length, "AuthType should have exactly 3 values");
    }

    @Test
    void testAuthType_ValuesShouldBeDistinct() {
        assertNotEquals(AuthPayload.AuthType.Basic, AuthPayload.AuthType.Token);
        assertNotEquals(AuthPayload.AuthType.Basic, AuthPayload.AuthType.OAuth);
        assertNotEquals(AuthPayload.AuthType.Token, AuthPayload.AuthType.OAuth);
    }

    @Test
    void testAuthType_ValueOfBasic_ShouldReturnBasic() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.valueOf("Basic");
        assertEquals(AuthPayload.AuthType.Basic, authType);
    }

    @Test
    void testAuthType_ValueOfToken_ShouldReturnToken() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.valueOf("Token");
        assertEquals(AuthPayload.AuthType.Token, authType);
    }

    @Test
    void testAuthType_ValueOfOAuth_ShouldReturnOAuth() {
        AuthPayload.AuthType authType = AuthPayload.AuthType.valueOf("OAuth");
        assertEquals(AuthPayload.AuthType.OAuth, authType);
    }

    @Test
    void testAuthType_ValueOfInvalid_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AuthPayload.AuthType.valueOf("Invalid");
        });
    }

    @Test
    void testAuthType_ValuesArray_ShouldContainAllTypes() {
        AuthPayload.AuthType[] values = AuthPayload.AuthType.values();
        boolean hasBasic = false;
        boolean hasToken = false;
        boolean hasOAuth = false;

        for (AuthPayload.AuthType type : values) {
            if (type == AuthPayload.AuthType.Basic) hasBasic = true;
            if (type == AuthPayload.AuthType.Token) hasToken = true;
            if (type == AuthPayload.AuthType.OAuth) hasOAuth = true;
        }

        assertTrue(hasBasic, "Values should contain Basic");
        assertTrue(hasToken, "Values should contain Token");
        assertTrue(hasOAuth, "Values should contain OAuth");
    }

    // ========== AuthPayload Constructor Tests ==========

    @Test
    void testAuthPayload_DefaultConstructor_ShouldCreateInstance() {
        AuthPayload payload = new AuthPayload();
        assertNotNull(payload, "AuthPayload should be created");
    }

    // ========== AuthPayload Getter/Setter Tests ==========

    @Test
    void testAuthPayload_SetAndGetAuthType_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Basic);
        assertEquals(AuthPayload.AuthType.Basic, payload.getAuthType());
    }

    @Test
    void testAuthPayload_SetAndGetUserName_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setUserName("testuser");
        assertEquals("testuser", payload.getUserName());
    }

    @Test
    void testAuthPayload_SetAndGetPassword_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setPassword("testpass");
        assertEquals("testpass", payload.getPassword());
    }

    @Test
    void testAuthPayload_SetAndGetApiUrl_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setApiUrl("https://api.example.com");
        assertEquals("https://api.example.com", payload.getApiUrl());
    }

    @Test
    void testAuthPayload_SetAndGetToken_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setToken("test-token-123");
        assertEquals("test-token-123", payload.getToken());
    }

    @Test
    void testAuthPayload_SetAndGetTokenType_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setTokenType("Bearer");
        assertEquals("Bearer", payload.getTokenType());
    }

    @Test
    void testAuthPayload_SetAndGetState_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setState("test-state");
        assertEquals("test-state", payload.getState());
    }

    @Test
    void testAuthPayload_SetAndGetScope_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setScope("read write");
        assertEquals("read write", payload.getScope());
    }

    @Test
    void testAuthPayload_SetAndGetClientId_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setClientId("client-123");
        assertEquals("client-123", payload.getClientId());
    }

    @Test
    void testAuthPayload_SetAndGetClientSecret_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setClientSecret("secret-456");
        assertEquals("secret-456", payload.getClientSecret());
    }

    @Test
    void testAuthPayload_SetAndGetAuthUrl_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthUrl("https://auth.example.com");
        assertEquals("https://auth.example.com", payload.getAuthUrl());
    }

    @Test
    void testAuthPayload_SetAndGetAccessTokenUrl_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAccessTokenUrl("https://token.example.com");
        assertEquals("https://token.example.com", payload.getAccessTokenUrl());
    }

    // ========== AuthPayload Null Value Tests ==========

    @Test
    void testAuthPayload_SetNullValues_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(null);
        payload.setUserName(null);
        payload.setPassword(null);
        payload.setApiUrl(null);
        payload.setToken(null);

        assertNull(payload.getAuthType());
        assertNull(payload.getUserName());
        assertNull(payload.getPassword());
        assertNull(payload.getApiUrl());
        assertNull(payload.getToken());
    }

    // ========== AuthPayload Empty String Tests ==========

    @Test
    void testAuthPayload_SetEmptyStrings_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setUserName("");
        payload.setPassword("");
        payload.setApiUrl("");
        payload.setToken("");

        assertEquals("", payload.getUserName());
        assertEquals("", payload.getPassword());
        assertEquals("", payload.getApiUrl());
        assertEquals("", payload.getToken());
    }

    // ========== AuthPayload Complete Object Tests ==========

    @Test
    void testAuthPayload_SetAllBasicAuthFields_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Basic);
        payload.setUserName("admin");
        payload.setPassword("password123");
        payload.setApiUrl("https://api.example.com");

        assertEquals(AuthPayload.AuthType.Basic, payload.getAuthType());
        assertEquals("admin", payload.getUserName());
        assertEquals("password123", payload.getPassword());
        assertEquals("https://api.example.com", payload.getApiUrl());
    }

    @Test
    void testAuthPayload_SetAllTokenAuthFields_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Token);
        payload.setToken("abc123xyz");
        payload.setTokenType("Bearer");
        payload.setApiUrl("https://api.example.com");

        assertEquals(AuthPayload.AuthType.Token, payload.getAuthType());
        assertEquals("abc123xyz", payload.getToken());
        assertEquals("Bearer", payload.getTokenType());
        assertEquals("https://api.example.com", payload.getApiUrl());
    }

    @Test
    void testAuthPayload_SetAllOAuthFields_ShouldWork() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.OAuth);
        payload.setClientId("client-123");
        payload.setClientSecret("secret-456");
        payload.setAuthUrl("https://auth.example.com");
        payload.setAccessTokenUrl("https://token.example.com");
        payload.setState("random-state");
        payload.setScope("read write");
        payload.setApiUrl("https://api.example.com");

        assertEquals(AuthPayload.AuthType.OAuth, payload.getAuthType());
        assertEquals("client-123", payload.getClientId());
        assertEquals("secret-456", payload.getClientSecret());
        assertEquals("https://auth.example.com", payload.getAuthUrl());
        assertEquals("https://token.example.com", payload.getAccessTokenUrl());
        assertEquals("random-state", payload.getState());
        assertEquals("read write", payload.getScope());
        assertEquals("https://api.example.com", payload.getApiUrl());
    }

    // ========== AuthType Enum Comparison Tests ==========

    @Test
    void testAuthType_Equals_ShouldWorkCorrectly() {
        AuthPayload.AuthType type1 = AuthPayload.AuthType.Basic;
        AuthPayload.AuthType type2 = AuthPayload.AuthType.Basic;
        AuthPayload.AuthType type3 = AuthPayload.AuthType.Token;

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
    }

    @Test
    void testAuthType_ToString_ShouldReturnName() {
        assertEquals("Basic", AuthPayload.AuthType.Basic.toString());
        assertEquals("Token", AuthPayload.AuthType.Token.toString());
        assertEquals("OAuth", AuthPayload.AuthType.OAuth.toString());
    }
}

