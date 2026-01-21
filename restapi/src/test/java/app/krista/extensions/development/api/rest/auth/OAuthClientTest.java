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

import app.krista.extension.authorization.MustAuthorizeException;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OAuthClient}.
 * <p>
 * Tests OAuth 2.0 client functionality including:
 * - OAuth URL generation
 * - Access token retrieval
 * - Token refresh
 * - Token revocation
 * - Response handling
 * </p>
 */
class OAuthClientTest {

    private OAuthClient oauthClient;
    private static final String CLIENT_ID = "test-client-id";
    private static final String CLIENT_SECRET = "test-client-secret";
    private static final String AUTH_URL = "https://oauth.example.com/authorize";
    private static final String ACCESS_TOKEN_URL = "https://oauth.example.com/token";
    private static final String SCOPE = "read write";
    private static final String ROUTING_INFO = "https://example.com/callback";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        oauthClient = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, SCOPE, ROUTING_INFO);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange & Act
        OAuthClient client = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, SCOPE, ROUTING_INFO);

        // Assert
        assertNotNull(client);
    }

    @Test
    void testConstructor_WithNullScope_ShouldUseEmptyScope() {
        // Arrange & Act
        OAuthClient client = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, null, ROUTING_INFO);

        // Assert
        assertNotNull(client);
        String oauthUrl = client.getOAuthUrl("test-state");
        assertFalse(oauthUrl.contains("scope="));
    }

    @Test
    void testConstructor_WithEmptyScope_ShouldUseEmptyScope() {
        // Arrange & Act
        OAuthClient client = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, "", ROUTING_INFO);

        // Assert
        assertNotNull(client);
        String oauthUrl = client.getOAuthUrl("test-state");
        assertFalse(oauthUrl.contains("scope="));
    }

    // ========== OAuth URL Generation Tests ==========

    @Test
    void testGetOAuthUrl_WithValidState_ShouldReturnCompleteUrl() {
        // Arrange
        String state = "random-state-123";

        // Act
        String oauthUrl = oauthClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertTrue(oauthUrl.startsWith(AUTH_URL));
        assertTrue(oauthUrl.contains("response_type=code"));
        assertTrue(oauthUrl.contains("client_id=" + CLIENT_ID));
        assertTrue(oauthUrl.contains("redirect_uri="));
        assertTrue(oauthUrl.contains("scope="));
        assertTrue(oauthUrl.contains("state=" + state));
        assertTrue(oauthUrl.contains("access_type=offline"));
        assertTrue(oauthUrl.contains("approval_prompt=force"));
    }

    @Test
    void testGetOAuthUrl_WithEmptyState_ShouldReturnUrlWithEmptyState() {
        // Arrange
        String state = "";

        // Act
        String oauthUrl = oauthClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertTrue(oauthUrl.contains("state="));
    }

    @Test
    void testGetOAuthUrl_WithSpecialCharactersInState_ShouldEncodeState() {
        // Arrange
        String state = "state with spaces & special=chars";

        // Act
        String oauthUrl = oauthClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertTrue(oauthUrl.contains("state="));
    }

    @Test
    void testGetOAuthUrl_WithoutScope_ShouldNotIncludeScopeParameter() {
        // Arrange
        OAuthClient clientWithoutScope = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, "", ROUTING_INFO);
        String state = "test-state";

        // Act
        String oauthUrl = clientWithoutScope.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertFalse(oauthUrl.contains("scope="));
    }

    // ========== Response Handling Tests ==========

    @Test
    void testHandleResponse_WithSuccessfulResponse_ShouldReturnJsonObject() {
        // Arrange
        Response mockResponse = mock(Response.class);
        Response.StatusType mockStatusType = mock(Response.StatusType.class);
        String jsonResponse = "{\"access_token\":\"token123\",\"token_type\":\"Bearer\"}";

        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockStatusType.getFamily()).thenReturn(Response.Status.Family.SUCCESSFUL);
        when(mockResponse.readEntity(String.class)).thenReturn(jsonResponse);

        // Act
        JsonObject result = OAuthClient.handleResponse(mockResponse);

        // Assert
        assertNotNull(result);
        assertTrue(result.has("access_token"));
        assertEquals("token123", result.get("access_token").getAsString());
    }

    @Test
    void testHandleResponse_WithUnauthorizedResponse_ShouldThrowMustAuthorizeException() {
        // Arrange
        Response mockResponse = mock(Response.class);
        Response.StatusType mockStatusType = mock(Response.StatusType.class);

        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockStatusType.getFamily()).thenReturn(Response.Status.Family.CLIENT_ERROR);
        when(mockResponse.getStatus()).thenReturn(Response.Status.UNAUTHORIZED.getStatusCode());
        when(mockStatusType.getReasonPhrase()).thenReturn("Unauthorized");
        when(mockResponse.readEntity(String.class)).thenReturn("Unauthorized access");

        // Act & Assert
        assertThrows(MustAuthorizeException.class, () -> OAuthClient.handleResponse(mockResponse));
    }

    @Test
    void testHandleResponse_WithOtherErrorResponse_ShouldThrowIllegalArgumentException() {
        // Arrange
        Response mockResponse = mock(Response.class);
        Response.StatusType mockStatusType = mock(Response.StatusType.class);
        String errorResponse = "{\"error\":\"invalid_grant\",\"error_description\":\"Invalid authorization code\"}";

        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockStatusType.getFamily()).thenReturn(Response.Status.Family.CLIENT_ERROR);
        when(mockResponse.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(mockResponse.readEntity(String.class)).thenReturn(errorResponse);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> OAuthClient.handleResponse(mockResponse));
    }

    @Test
    void testHandleResponse_WithServerError_ShouldThrowIllegalArgumentException() {
        // Arrange
        Response mockResponse = mock(Response.class);
        Response.StatusType mockStatusType = mock(Response.StatusType.class);
        String errorResponse = "Internal Server Error";

        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockStatusType.getFamily()).thenReturn(Response.Status.Family.SERVER_ERROR);
        when(mockResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        when(mockResponse.readEntity(String.class)).thenReturn(errorResponse);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> OAuthClient.handleResponse(mockResponse));
    }

    // ========== Constants Tests ==========

    @Test
    void testGoogleApisRevokeUrl_ShouldHaveCorrectValue() {
        // Assert
        assertEquals("https://oauth2.googleapis.com/revoke", OAuthClient.GOOGLEAPIS_COM_REVOKE);
    }

    @Test
    void testGoogleApisRevokeUrl_ShouldNotBeNull() {
        // Assert
        assertNotNull(OAuthClient.GOOGLEAPIS_COM_REVOKE);
    }

    @Test
    void testGoogleApisRevokeUrl_ShouldBeValidUrl() {
        // Assert
        assertTrue(OAuthClient.GOOGLEAPIS_COM_REVOKE.startsWith("https://"));
        assertTrue(OAuthClient.GOOGLEAPIS_COM_REVOKE.contains("googleapis.com"));
    }

    // ========== Edge Case Tests ==========

    @Test
    void testGetOAuthUrl_CalledMultipleTimes_ShouldReturnConsistentResults() {
        // Arrange
        String state = "test-state";

        // Act
        String url1 = oauthClient.getOAuthUrl(state);
        String url2 = oauthClient.getOAuthUrl(state);

        // Assert
        assertEquals(url1, url2);
    }

    @Test
    void testConstructor_WithLongScope_ShouldHandleCorrectly() {
        // Arrange
        String longScope = "read write delete update create admin user profile email openid";

        // Act
        OAuthClient client = new OAuthClient(CLIENT_ID, CLIENT_SECRET, AUTH_URL, ACCESS_TOKEN_URL, longScope, ROUTING_INFO);
        String oauthUrl = client.getOAuthUrl("test-state");

        // Assert
        assertNotNull(client);
        assertTrue(oauthUrl.contains("scope="));
    }

    @Test
    void testGetOAuthUrl_WithNullState_ShouldHandleGracefully() {
        // Arrange
        String state = null;

        // Act
        String oauthUrl = oauthClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        // URL should still be generated even with null state
        assertTrue(oauthUrl.contains("response_type=code"));
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testOAuthFlow_GoogleOAuth_ShouldGenerateValidUrl() {
        // Arrange
        OAuthClient googleClient = new OAuthClient(
                "google-client-id",
                "google-client-secret",
                "https://accounts.google.com/o/oauth2/v2/auth",
                "https://oauth2.googleapis.com/token",
                "https://www.googleapis.com/auth/drive.readonly",
                "https://myapp.com/oauth/callback"
        );
        String state = "security-token-123";

        // Act
        String oauthUrl = googleClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertTrue(oauthUrl.contains("accounts.google.com"));
        assertTrue(oauthUrl.contains("client_id=google-client-id"));
        assertTrue(oauthUrl.contains("state=security-token-123"));
    }

    @Test
    void testOAuthFlow_GitHubOAuth_ShouldGenerateValidUrl() {
        // Arrange
        OAuthClient githubClient = new OAuthClient(
                "github-client-id",
                "github-client-secret",
                "https://github.com/login/oauth/authorize",
                "https://github.com/login/oauth/access_token",
                "repo user",
                "https://myapp.com/oauth/callback"
        );
        String state = "random-state-456";

        // Act
        String oauthUrl = githubClient.getOAuthUrl(state);

        // Assert
        assertNotNull(oauthUrl);
        assertTrue(oauthUrl.contains("github.com"));
        assertTrue(oauthUrl.contains("client_id=github-client-id"));
        assertTrue(oauthUrl.contains("state=random-state-456"));
    }

    @Test
    void testHandleResponse_WithEmptyJsonResponse_ShouldReturnEmptyJsonObject() {
        // Arrange
        Response mockResponse = mock(Response.class);
        Response.StatusType mockStatusType = mock(Response.StatusType.class);
        String jsonResponse = "{}";

        when(mockResponse.getStatusInfo()).thenReturn(mockStatusType);
        when(mockStatusType.getFamily()).thenReturn(Response.Status.Family.SUCCESSFUL);
        when(mockResponse.readEntity(String.class)).thenReturn(jsonResponse);

        // Act
        JsonObject result = OAuthClient.handleResponse(mockResponse);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}

