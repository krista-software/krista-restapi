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
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.auth.AuthPayload;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import okhttp3.RequestBody;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link HTTPRequest} class.
 * <p>
 * Tests verify the HTTPRequest factory method and constructor.
 * Note: Full integration tests for the create() method would require complex mocking
 * of OkHttp components and are better suited for integration testing.
 * </p>
 */
class HTTPRequestTest {

    @Mock
    private RestApiAttributes restApiAttributes;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @Mock
    private Invoker invoker;

    @Mock
    private AttributeStore attributeStore;

    @Mock
    private KristaMediaClient kristaMediaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange & Act
        HTTPRequest httpRequest = new HTTPRequest(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest);
    }

    @Test
    void testConstructor_WithNullParameters_ShouldCreateInstance() {
        // Arrange & Act
        HTTPRequest httpRequest = new HTTPRequest(null, null, null, null, null);

        // Assert
        assertNotNull(httpRequest);
    }

    // ========== Factory Method Tests ==========

    @Test
    void testGetInstance_WithAllParameters_ShouldReturnNewInstance() {
        // Arrange & Act
        HTTPRequest httpRequest = HTTPRequest.getInstance(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest);
    }

    @Test
    void testGetInstance_WithNullParameters_ShouldReturnNewInstance() {
        // Arrange & Act
        HTTPRequest httpRequest = HTTPRequest.getInstance(null, null, null, null, null);

        // Assert
        assertNotNull(httpRequest);
    }

    @Test
    void testGetInstance_CalledMultipleTimes_ShouldReturnNewInstances() {
        // Arrange & Act
        HTTPRequest httpRequest1 = HTTPRequest.getInstance(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );
        HTTPRequest httpRequest2 = HTTPRequest.getInstance(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest1);
        assertNotNull(httpRequest2);
        assertNotSame(httpRequest1, httpRequest2, "Factory should return new instances");
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testGetInstance_WithBasicAuthAttributes_ShouldCreateInstance() {
        // Arrange
        RestApiAttributes basicAuthAttributes = new RestApiAttributes(
                "testuser",
                "testpass",
                null,
                null,
                AuthPayload.AuthType.Basic,
                "https://api.example.com",
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Act
        HTTPRequest httpRequest = HTTPRequest.getInstance(
                basicAuthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest);
    }

    @Test
    void testGetInstance_WithTokenAuthAttributes_ShouldCreateInstance() {
        // Arrange
        RestApiAttributes tokenAuthAttributes = new RestApiAttributes(
                null,
                null,
                "test-token-123",
                "Bearer",
                AuthPayload.AuthType.Token,
                "https://api.example.com",
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Act
        HTTPRequest httpRequest = HTTPRequest.getInstance(
                tokenAuthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest);
    }

    @Test
    void testGetInstance_WithOAuthAttributes_ShouldCreateInstance() {
        // Arrange
        RestApiAttributes oauthAttributes = new RestApiAttributes(
                null,
                null,
                null,
                null,
                AuthPayload.AuthType.OAuth,
                "https://api.example.com",
                "client-id",
                "client-secret",
                "https://auth.example.com",
                "https://token.example.com",
                "random-state",
                "read write"
        );

        // Act
        HTTPRequest httpRequest = HTTPRequest.getInstance(
                oauthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(httpRequest);
    }

    // ========== getBody() Method Tests ==========

    @Nested
    @DisplayName("getBody() Method Tests")
    class GetBodyTests {

        private HTTPRequest httpRequest;

        @BeforeEach
        void setUp() {
            httpRequest = new HTTPRequest(
                    restApiAttributes,
                    refreshTokenStore,
                    invoker,
                    attributeStore,
                    kristaMediaClient
            );
        }

        /**
         * Helper method to invoke private getBody() method using reflection
         */
        private RequestBody invokeGetBody(Map<String, Object> payload, String methodType) throws Exception {
            Method getBodyMethod = HTTPRequest.class.getDeclaredMethod("getBody", Map.class, String.class);
            getBodyMethod.setAccessible(true);
            return (RequestBody) getBodyMethod.invoke(httpRequest, payload, methodType);
        }

        @Test
        @DisplayName("GET request should return null body")
        void testGetBody_WithGetMethod_ShouldReturnNull() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", "{\"test\":\"data\"}");

            // Act
            RequestBody result = invokeGetBody(payload, "GET");

            // Assert
            assertNull(result, "GET requests should have null body");
        }

        @Test
        @DisplayName("POST with explicit multipart/form-data payloadType should create multipart body")
        void testGetBody_WithExplicitMultipartPayloadType_ShouldCreateMultipart() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("payloadType", "multipart/form-data");
            payload.put("value", "{\"key\":\"value\"}");

            // Act
            RequestBody result = invokeGetBody(payload, "POST");

            // Assert
            assertNotNull(result, "Should create request body");
            assertTrue(result.contentType().toString().contains("multipart/form-data"),
                    "Should be multipart/form-data content type");
        }

        @Test
        @DisplayName("POST without payloadType should default to JSON body")
        void testGetBody_WithoutPayloadType_ShouldCreateJsonBody() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", "{\"test\":\"data\"}");

            // Act
            RequestBody result = invokeGetBody(payload, "POST");

            // Assert
            assertNotNull(result, "Should create request body");
            assertTrue(result.contentType().toString().contains("application/json"),
                    "Should be JSON content type");
        }

        @Test
        @DisplayName("POST with empty value should create empty JSON body")
        void testGetBody_WithEmptyValue_ShouldCreateEmptyJsonBody() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", "");

            // Act
            RequestBody result = invokeGetBody(payload, "POST");

            // Assert
            assertNotNull(result, "Should create request body");

            // Read the body content
            Buffer buffer = new Buffer();
            result.writeTo(buffer);
            String bodyContent = buffer.readUtf8();

            assertEquals("", bodyContent, "Body should be empty string");
        }

        @Test
        @DisplayName("POST with null value should create empty JSON body")
        void testGetBody_WithNullValue_ShouldCreateEmptyJsonBody() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", null);

            // Act
            RequestBody result = invokeGetBody(payload, "POST");

            // Assert
            assertNotNull(result, "Should create request body");

            // Read the body content
            Buffer buffer = new Buffer();
            result.writeTo(buffer);
            String bodyContent = buffer.readUtf8();

            assertEquals("", bodyContent, "Body should be empty string for null value");
        }

        @Test
        @DisplayName("POST with JSON value should create JSON body with correct content")
        void testGetBody_WithJsonValue_ShouldCreateJsonBodyWithContent() throws Exception {
            // Arrange
            String jsonContent = "{\"name\":\"test\",\"value\":123}";
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", jsonContent);

            // Act
            RequestBody result = invokeGetBody(payload, "POST");

            // Assert
            assertNotNull(result, "Should create request body");

            // Read the body content
            Buffer buffer = new Buffer();
            result.writeTo(buffer);
            String bodyContent = buffer.readUtf8();

            assertEquals(jsonContent, bodyContent, "Body content should match input JSON");
        }

        @Test
        @DisplayName("PUT request should create JSON body")
        void testGetBody_WithPutMethod_ShouldCreateJsonBody() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", "{\"update\":\"data\"}");

            // Act
            RequestBody result = invokeGetBody(payload, "PUT");

            // Assert
            assertNotNull(result, "Should create request body for PUT");
            assertTrue(result.contentType().toString().contains("application/json"),
                    "PUT should use JSON content type");
        }

        @Test
        @DisplayName("DELETE request should create JSON body")
        void testGetBody_WithDeleteMethod_ShouldCreateJsonBody() throws Exception {
            // Arrange
            Map<String, Object> payload = new HashMap<>();
            payload.put("value", "");

            // Act
            RequestBody result = invokeGetBody(payload, "DELETE");

            // Assert
            assertNotNull(result, "Should create request body for DELETE");
        }
    }

    // ========== getUrl() Method Tests ==========

    @Nested
    @DisplayName("getUrl() Method Tests")
    class GetUrlTests {

        private HTTPRequest httpRequest;

        @BeforeEach
        void setUp() {
            httpRequest = new HTTPRequest(
                    restApiAttributes,
                    refreshTokenStore,
                    invoker,
                    attributeStore,
                    kristaMediaClient
            );
        }

        /**
         * Helper method to invoke private getUrl() method using reflection
         */
        private String invokeGetUrl(String url, List<Map<String, Object>> queryParameters) throws Exception {
            Method getUrlMethod = HTTPRequest.class.getDeclaredMethod("getUrl", String.class, List.class);
            getUrlMethod.setAccessible(true);
            return (String) getUrlMethod.invoke(httpRequest, url, queryParameters);
        }

        @Test
        @DisplayName("URL with spaces should encode spaces as %20")
        void testGetUrl_WithSpacesInUrl_ShouldEncodeSpaces() throws Exception {
            // Arrange
            String url = "https://api.example.com/test endpoint";
            List<Map<String, Object>> queryParams = new ArrayList<>();

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test%20endpoint", result,
                    "Spaces in URL should be encoded as %20");
        }

        @Test
        @DisplayName("URL with query params should append correctly")
        void testGetUrl_WithQueryParams_ShouldAppendCorrectly() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "name");
            param1.put("value", "John");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test?name=John", result,
                    "Query parameters should be appended correctly");
        }

        @Test
        @DisplayName("URL with multiple query params should append all")
        void testGetUrl_WithMultipleQueryParams_ShouldAppendAll() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();

            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "name");
            param1.put("value", "John");
            queryParams.add(param1);

            Map<String, Object> param2 = new HashMap<>();
            param2.put("key", "age");
            param2.put("value", "30");
            queryParams.add(param2);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test?name=John&age=30", result,
                    "Multiple query parameters should be appended with & separator");
        }

        @Test
        @DisplayName("URL already with query params should append with &")
        void testGetUrl_WithExistingQueryParams_ShouldAppendWithAmpersand() throws Exception {
            // Arrange
            String url = "https://api.example.com/test?existing=param";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "new");
            param1.put("value", "value");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test?existing=param&new=value", result,
                    "New params should be appended with & when URL already has query params");
        }

        @Test
        @DisplayName("Query param values with spaces should encode spaces as +")
        void testGetUrl_WithSpacesInQueryParamValue_ShouldEncodeSpaces() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "name");
            param1.put("value", "John Doe");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // URLEncoder.encode() converts spaces to + (application/x-www-form-urlencoded standard)
            assertEquals("https://api.example.com/test?name=John+Doe", result,
                    "Spaces in query param values should be encoded as + by URLEncoder");
        }

        @Test
        @DisplayName("Query param values with special characters should be properly encoded")
        void testGetUrl_WithSpecialCharactersInQueryParamValue_ShouldEncodeCorrectly() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "filter");
            param1.put("value", "test&admin=true");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // URLEncoder should encode & to %26 to prevent parameter injection
            assertEquals("https://api.example.com/test?filter=test%26admin%3Dtrue", result,
                    "Special characters like & and = should be properly encoded to prevent injection");
        }

        @Test
        @DisplayName("Query param values with multiple special characters should all be encoded")
        void testGetUrl_WithMultipleSpecialCharacters_ShouldEncodeAll() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "data");
            param1.put("value", "hello?world#test");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // ? should be encoded as %3F, # as %23
            assertEquals("https://api.example.com/test?data=hello%3Fworld%23test", result,
                    "Special characters ? and # should be properly encoded");
        }

        @Test
        @DisplayName("Query param values with international characters should be encoded")
        void testGetUrl_WithInternationalCharacters_ShouldEncodeCorrectly() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "name");
            param1.put("value", "José García");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // International characters should be UTF-8 encoded
            assertEquals("https://api.example.com/test?name=Jos%C3%A9+Garc%C3%ADa", result,
                    "International characters should be UTF-8 encoded");
        }

        @Test
        @DisplayName("Query params with null key should be skipped but leave trailing ?")
        void testGetUrl_WithNullKey_ShouldSkipParam() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", null);
            param1.put("value", "value");
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // Note: Current implementation leaves trailing ? when all params are skipped
            assertEquals("https://api.example.com/test?", result,
                    "Query params with null key should be skipped (leaves trailing ?)");
        }

        @Test
        @DisplayName("Query params with null value should be skipped but leave trailing ?")
        void testGetUrl_WithNullValue_ShouldSkipParam() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key", "name");
            param1.put("value", null);
            queryParams.add(param1);

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            // Note: Current implementation leaves trailing ? when all params are skipped
            assertEquals("https://api.example.com/test?", result,
                    "Query params with null value should be skipped (leaves trailing ?)");
        }

        @Test
        @DisplayName("Null URL should return null")
        void testGetUrl_WithNullUrl_ShouldReturnNull() throws Exception {
            // Arrange
            String url = null;
            List<Map<String, Object>> queryParams = new ArrayList<>();

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertNull(result, "Null URL should return null");
        }

        @Test
        @DisplayName("Empty URL should return empty")
        void testGetUrl_WithEmptyUrl_ShouldReturnEmpty() throws Exception {
            // Arrange
            String url = "";
            List<Map<String, Object>> queryParams = new ArrayList<>();

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("", result, "Empty URL should return empty string");
        }

        @Test
        @DisplayName("Null query params should return URL as-is")
        void testGetUrl_WithNullQueryParams_ShouldReturnUrlAsIs() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = null;

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test", result,
                    "Null query params should return URL as-is with spaces encoded");
        }

        @Test
        @DisplayName("Empty query params list should return URL as-is")
        void testGetUrl_WithEmptyQueryParams_ShouldReturnUrlAsIs() throws Exception {
            // Arrange
            String url = "https://api.example.com/test";
            List<Map<String, Object>> queryParams = new ArrayList<>();

            // Act
            String result = invokeGetUrl(url, queryParams);

            // Assert
            assertEquals("https://api.example.com/test", result,
                    "Empty query params should return URL as-is");
        }
    }
}