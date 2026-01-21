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

package app.krista.extensions.development.api.rest.impl;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.auth.AuthPayload;
import app.krista.extensions.development.api.rest.auth.TestConnectionResponse;
import app.krista.extensions.development.api.rest.connectors.ActionableImplProvider;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AuthHelper} class.
 * <p>
 * Tests verify the authentication helper methods, particularly focusing on the bug fix
 * for KE-2793 where attributes were being set to null after clicking Test Connection.
 * </p>
 */
class AuthHelperTest {

    @Mock
    private AttributeStore attributeStore;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @Mock
    private Invoker invoker;

    @Mock
    private ActionableImplProvider actionableImplProvider;

    @Mock
    private ActionableImpl actionable;

    @Mock
    private RoutingInfo routingInfo;

    private AuthHelper authHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authHelper = new AuthHelper(attributeStore, refreshTokenStore, invoker);
    }

    // ========== Test Connection Tests (Bug Fix KE-2793) ==========

    /**
     * Test that testConnection does not modify attribute store on successful connection.
     * This is the core test for bug fix KE-2793.
     */
    @Test
    void testConnection_WithSuccessfulResponse_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(actionable.testConnection()).thenReturn(mockResponse);

        // Act
        String result = authHelper.testConnection(actionable);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertTrue(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    /**
     * Test that testConnection does not modify attribute store on failed connection.
     * This verifies the bug fix for KE-2793.
     */
    @Test
    void testConnection_WithFailedResponse_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        Response mockResponse = createMockResponse(401, "Unauthorized");
        when(actionable.testConnection()).thenReturn(mockResponse);

        // Act
        String result = authHelper.testConnection(actionable);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertFalse(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    /**
     * Test that testConnection does not modify attribute store when exception occurs.
     * This verifies the bug fix for KE-2793.
     */
    @Test
    void testConnection_WithException_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        when(actionable.testConnection()).thenThrow(new IOException("Connection timeout"));

        // Act
        String result = authHelper.testConnection(actionable);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertFalse(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    // ========== Validate and Test Connection Tests (Bug Fix KE-2793) ==========

    /**
     * Test that validateAndTestConnection does not modify attribute store on successful connection.
     * This verifies the bug fix for KE-2793.
     */
    @Test
    void validateAndTestConnection_WithSuccessfulResponse_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        AuthPayload authPayload = createBasicAuthPayload();
        Response mockResponse = createMockResponse(200, "OK");
        
        when(actionableImplProvider.getRestClientForAdmin()).thenReturn(actionable);
        when(actionable.testConnection()).thenReturn(mockResponse);

        // Act
        String result = authHelper.validateAndTestConnection(actionableImplProvider, authPayload);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertTrue(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    /**
     * Test that validateAndTestConnection does not modify attribute store when actionable is null.
     * This verifies the bug fix for KE-2793.
     */
    @Test
    void validateAndTestConnection_WithNullActionable_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        AuthPayload authPayload = createBasicAuthPayload();
        when(actionableImplProvider.getRestClientForAdmin()).thenReturn(null);

        // Act
        String result = authHelper.validateAndTestConnection(actionableImplProvider, authPayload);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertFalse(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    /**
     * Test that validateAndTestConnection does not modify attribute store on failed connection.
     * This verifies the bug fix for KE-2793.
     */
    @Test
    void validateAndTestConnection_WithFailedResponse_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange
        AuthPayload authPayload = createBasicAuthPayload();
        Response mockResponse = createMockResponse(500, "Internal Server Error");
        
        when(actionableImplProvider.getRestClientForAdmin()).thenReturn(actionable);
        when(actionable.testConnection()).thenReturn(mockResponse);

        // Act
        String result = authHelper.validateAndTestConnection(actionableImplProvider, authPayload);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertFalse(response.isSuccess());
        
        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    /**
     * Test that validateAndTestConnection handles retry without modifying attribute store.
     * This verifies the bug fix for KE-2793 in the retry flow for non-Google OAuth.
     */
    @Test
    void validateAndTestConnection_WithRetrySuccess_ShouldNotModifyAttributeStore() throws IOException {
        // Arrange - using non-Google OAuth to avoid OAuthClient.revokeAccessToken call
        AuthPayload authPayload = createOAuthPayload();
        Response failedResponse = createMockResponse(401, "Unauthorized");
        Response successResponse = createMockResponse(200, "OK");

        when(invoker.getRoutingInfo()).thenReturn(routingInfo);
        when(routingInfo.getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE))
                .thenReturn("https://test.krista.com");

        when(actionableImplProvider.getRestClientForAdmin())
                .thenReturn(actionable)  // First call fails
                .thenReturn(actionable); // Second call succeeds

        when(actionable.testConnection())
                .thenReturn(failedResponse)  // First attempt fails
                .thenReturn(successResponse); // Retry succeeds

        // Act
        String result = authHelper.validateAndTestConnection(actionableImplProvider, authPayload);

        // Assert
        TestConnectionResponse response = GSON_JSON_MAPPER.fromString(result, TestConnectionResponse.class);
        assertTrue(response.isSuccess());

        // Verify that attribute store was never updated (bug fix verification)
        verify(attributeStore, never()).put(any(), any());
        verify(attributeStore, never()).remove(any());
    }

    // ========== Helper Methods ==========

    /**
     * Creates a mock HTTP response with the specified status code and message.
     */
    private Response createMockResponse(int code, String message) {
        return new Response.Builder()
                .request(new Request.Builder().url("http://test.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .body(ResponseBody.create(null, ""))
                .build();
    }

    /**
     * Creates a basic auth payload for testing.
     */
    private AuthPayload createBasicAuthPayload() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.Basic);
        payload.setUserName("testuser");
        payload.setPassword("testpass");
        payload.setApiUrl("https://api.test.com");
        return payload;
    }

    /**
     * Creates a generic OAuth payload for testing retry scenarios.
     */
    private AuthPayload createOAuthPayload() {
        AuthPayload payload = new AuthPayload();
        payload.setAuthType(AuthPayload.AuthType.OAuth);
        payload.setClientId("test-client-id");
        payload.setClientSecret("test-client-secret");
        payload.setAuthUrl("https://auth.example.com/oauth2/auth");
        payload.setAccessTokenUrl("https://auth.example.com/oauth2/token");
        payload.setScope("read write");
        payload.setApiUrl("https://api.test.com");
        return payload;
    }
}

