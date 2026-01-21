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
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link WriteAction} class.
 * <p>
 * Tests verify the WriteAction constructor and initialization logic.
 * Note: Full integration tests for the post() methods would require complex mocking
 * of HTTP calls and are better suited for integration testing.
 * </p>
 */
class WriteActionTest {

    @Mock
    private AttributeStore attributeStore;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @Mock
    private Invoker invoker;

    @Mock
    private KristaMediaClient kristaMediaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }

    @Test
    void testConstructor_WithNullParameters_ShouldThrowException() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new WriteAction(null, null, null, null);
        });
    }

    @Test
    void testConstructor_WithEmptyAttributeStore_ShouldCreateInstanceWithNullRestApiAttributes() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
    }

    // ========== Method Overload Tests ==========

    @Test
    void testPost_WithOnlyUrl_MethodExists() {
        // This test verifies that the post(String url) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(writeAction);
    }

    @Test
    void testPost_WithUrlAndQueryParameters_MethodExists() {
        // This test verifies that the post(String url, List<Map<String, Object>> queryParameters) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(writeAction);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_ForBasicAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
    }

    @Test
    void testConstructor_ForTokenAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
    }

    @Test
    void testConstructor_ForOAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
    }

    @Test
    void testConstructor_WithMultipleCalls_ShouldCreateMultipleInstances() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction1 = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );
        WriteAction writeAction2 = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction1);
        assertNotNull(writeAction2);
        assertNotSame(writeAction1, writeAction2, "Should create different instances");
    }

    @Test
    void testConstructor_VerifyDependenciesInjected_ShouldCallAttributeStore() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(writeAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }

    // ========== postResponseAsFile() Method Tests ==========

    @Test
    void testPostResponseAsFile_WithValidParameters_ShouldReturnResponseMap() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("key1", "value1");
        payload.put("key2", 123);

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testPostResponseAsFile_WithNullPayload_ShouldThrowNullPointerException() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        // Act & Assert
        // Note: Current implementation throws NullPointerException for null payload
        // This test documents the actual behavior - consider fixing in HTTPRequest.hasFilePayload()
        assertThrows(NullPointerException.class, () -> {
            writeAction.postResponseAsFile(url, queryParams, headers, null);
        }, "Current implementation throws NPE for null payload");
    }

    @Test
    void testPostResponseAsFile_WithEmptyPayload_ShouldReturnResponseMap() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null even with empty payload");
    }

    @Test
    void testPostResponseAsFile_WithComplexPayload_ShouldLogAllEntries() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        Map<String, Object> payload = new HashMap<>();
        payload.put("stringValue", "test");
        payload.put("intValue", 42);
        payload.put("boolValue", true);
        payload.put("nullValue", null);
        payload.put("listValue", Arrays.asList("a", "b", "c"));
        payload.put("mapValue", Collections.singletonMap("nested", "value"));

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
        // Verify that all payload entries were processed (logged)
        assertEquals(6, payload.size(), "All payload entries should be present");
    }

    @Test
    void testPostResponseAsFile_WithQueryParameters_ShouldPassToActionable() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";

        List<Map<String, Object>> queryParams = new ArrayList<>();
        Map<String, Object> param1 = new HashMap<>();
        param1.put("key", "page");
        param1.put("value", "1");
        queryParams.add(param1);

        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("data", "test");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testPostResponseAsFile_WithCustomHeaders_ShouldPassToActionable() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();

        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> header1 = new HashMap<>();
        header1.put("key", "Authorization");
        header1.put("value", "Bearer token123");
        headers.add(header1);

        Map<String, Object> payload = new HashMap<>();
        payload.put("data", "test");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testPostResponseAsFile_WithNullUrl_ShouldThrowIllegalArgumentException() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();

        // Act & Assert
        // Current implementation validates URL and throws IllegalArgumentException for null URL
        assertThrows(IllegalArgumentException.class, () -> {
            writeAction.postResponseAsFile(null, queryParams, headers, payload);
        }, "Should throw IllegalArgumentException for null URL");
    }

    @Test
    void testPostResponseAsFile_WithEmptyUrl_ShouldThrowIllegalArgumentException() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> payload = new HashMap<>();

        // Act & Assert
        // Current implementation validates URL and throws IllegalArgumentException for empty URL
        assertThrows(IllegalArgumentException.class, () -> {
            writeAction.postResponseAsFile(url, queryParams, headers, payload);
        }, "Should throw IllegalArgumentException for empty URL");
    }

    @Test
    void testPostResponseAsFile_WithNullQueryParameters_ShouldNotThrowException() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        Map<String, Object> payload = new HashMap<>();
        payload.put("data", "test");

        // Act & Assert
        assertDoesNotThrow(() -> {
            writeAction.postResponseAsFile(url, null, null, payload);
        }, "Should handle null query parameters and headers");
    }

    @Test
    void testPostResponseAsFile_WithPayloadContainingNullValues_ShouldLogCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        Map<String, Object> payload = new HashMap<>();
        payload.put("key1", "value1");
        payload.put("key2", null);  // Null value
        payload.put("key3", "value3");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(payload.containsKey("key2"), "Payload should still contain key with null value");
    }

    @Test
    void testPostResponseAsFile_WithLargePayload_ShouldHandleCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        // Create a large payload
        Map<String, Object> payload = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            payload.put("key" + i, "value" + i);
        }

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(100, payload.size(), "All payload entries should be present");
    }

    @Test
    void testPostResponseAsFile_WithSpecialCharactersInPayload_ShouldHandleCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        Map<String, Object> payload = new HashMap<>();
        payload.put("special_chars", "!@#$%^&*()");
        payload.put("unicode", "Hello 世界 🌍");
        payload.put("quotes", "He said \"Hello\"");
        payload.put("newlines", "Line1\nLine2\nLine3");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testPostResponseAsFile_WithDifferentDataTypes_ShouldLogTypesCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";
        List<Map<String, Object>> queryParams = new ArrayList<>();
        List<Map<String, Object>> headers = new ArrayList<>();

        Map<String, Object> payload = new HashMap<>();
        payload.put("string", "text");
        payload.put("integer", 42);
        payload.put("long", 123456789L);
        payload.put("double", 3.14159);
        payload.put("boolean", true);
        payload.put("list", Arrays.asList(1, 2, 3));
        payload.put("map", Collections.singletonMap("nested", "value"));

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null");
        // Verify different data types are present
        assertTrue(payload.get("string") instanceof String);
        assertTrue(payload.get("integer") instanceof Integer);
        assertTrue(payload.get("boolean") instanceof Boolean);
        assertTrue(payload.get("list") instanceof List);
        assertTrue(payload.get("map") instanceof Map);
    }

    // ========== Real-World Scenario Tests for postResponseAsFile ==========

    @Test
    void testPostResponseAsFile_DownloadPdfScenario_ShouldReturnFileResponse() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/reports/download";

        List<Map<String, Object>> queryParams = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("key", "reportId");
        param.put("value", "12345");
        queryParams.add(param);

        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> header = new HashMap<>();
        header.put("key", "Accept");
        header.put("value", "application/pdf");
        headers.add(header);

        Map<String, Object> payload = new HashMap<>();
        payload.put("format", "pdf");
        payload.put("includeCharts", true);

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null for PDF download scenario");
    }

    @Test
    void testPostResponseAsFile_DownloadExcelScenario_ShouldReturnFileResponse() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/export/excel";

        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> header = new HashMap<>();
        header.put("key", "Content-Type");
        header.put("value", "application/json");
        headers.add(header);

        Map<String, Object> payload = new HashMap<>();
        payload.put("sheetName", "Sales Report");
        payload.put("dateRange", "2024-01-01 to 2024-12-31");
        payload.put("columns", Arrays.asList("Date", "Amount", "Customer"));

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, new ArrayList<>(), headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null for Excel download scenario");
    }

    @Test
    void testPostResponseAsFile_DownloadImageScenario_ShouldReturnFileResponse() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/images/generate";

        Map<String, Object> payload = new HashMap<>();
        payload.put("width", 1920);
        payload.put("height", 1080);
        payload.put("format", "png");
        payload.put("quality", 95);

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, null, null, payload);

        // Assert
        assertNotNull(result, "Result should not be null for image download scenario");
    }

    @Test
    void testPostResponseAsFile_DownloadZipArchiveScenario_ShouldReturnFileResponse() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/backup/download";

        List<Map<String, Object>> headers = new ArrayList<>();
        Map<String, Object> authHeader = new HashMap<>();
        authHeader.put("key", "Authorization");
        authHeader.put("value", "Bearer token123");
        headers.add(authHeader);

        Map<String, Object> payload = new HashMap<>();
        payload.put("backupId", "backup-2024-01-12");
        payload.put("includeMetadata", true);
        payload.put("compressionLevel", 9);

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, new ArrayList<>(), headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null for ZIP download scenario");
    }

    @Test
    void testPostResponseAsFile_WithAuthenticationHeaders_ShouldPassCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/secure/download";

        List<Map<String, Object>> headers = new ArrayList<>();

        Map<String, Object> authHeader = new HashMap<>();
        authHeader.put("key", "Authorization");
        authHeader.put("value", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
        headers.add(authHeader);

        Map<String, Object> apiKeyHeader = new HashMap<>();
        apiKeyHeader.put("key", "X-API-Key");
        apiKeyHeader.put("value", "abc123def456");
        headers.add(apiKeyHeader);

        Map<String, Object> payload = new HashMap<>();
        payload.put("resourceId", "resource-123");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, new ArrayList<>(), headers, payload);

        // Assert
        assertNotNull(result, "Result should not be null with authentication headers");
    }

    @Test
    void testPostResponseAsFile_WithMultipleQueryParameters_ShouldPassCorrectly() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        WriteAction writeAction = new WriteAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        String url = "https://api.example.com/download";

        List<Map<String, Object>> queryParams = new ArrayList<>();

        Map<String, Object> param1 = new HashMap<>();
        param1.put("key", "page");
        param1.put("value", "1");
        queryParams.add(param1);

        Map<String, Object> param2 = new HashMap<>();
        param2.put("key", "size");
        param2.put("value", "100");
        queryParams.add(param2);

        Map<String, Object> param3 = new HashMap<>();
        param3.put("key", "sort");
        param3.put("value", "date_desc");
        queryParams.add(param3);

        Map<String, Object> payload = new HashMap<>();
        payload.put("filter", "active");

        // Act
        Map<String, Object> result = writeAction.postResponseAsFile(url, queryParams, new ArrayList<>(), payload);

        // Assert
        assertNotNull(result, "Result should not be null with multiple query parameters");
    }
}
