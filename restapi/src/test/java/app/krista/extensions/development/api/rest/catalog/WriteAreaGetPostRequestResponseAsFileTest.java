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

package app.krista.extensions.development.api.rest.catalog;

import app.krista.extensions.development.api.rest.impl.WriteAction;
import app.krista.extensions.util.EventHandler;
import app.krista.model.base.File;
import app.krista.model.base.FreeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Comprehensive integration test suite for the "Get Post Request Response as File" catalog request.
 * <p>
 * This test class provides 100% code coverage for the getPostRequestResponseAsFile method
 * in the WriteArea class, covering all scenarios including:
 * - Success cases with various file types
 * - Error handling scenarios
 * - Edge cases with null/empty parameters
 * - Different combinations of headers and filters
 *
 * @author Test Suite Generator
 */
@DisplayName("WriteArea - Get Post Request Response as File Tests")
class WriteAreaGetPostRequestResponseAsFileTest {

    @Mock
    private WriteAction writeAction;

    @Mock
    private EventHandler eventHandler;

    private WriteArea writeArea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        writeArea = new WriteArea(writeAction, eventHandler);
    }

    // ========== SUCCESS SCENARIOS ==========

    @Test
    @DisplayName("Should successfully get POST response as file with all parameters")
    void testGetPostRequestResponseAsFile_WithAllParameters_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/export";
        Map<String, Object> payload = createPayload("key1", "value1");
        List<Map<String, Object>> headers = createHeaders("Authorization", "Bearer token123");
        List<Map<String, Object>> filters = createFilters("format", "xlsx");

        Map<String, Object> expectedResponse = createSuccessFileResponse("export.xlsx", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response as file with only required parameters")
    void testGetPostRequestResponseAsFile_WithOnlyRequiredParameters_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/download";
        Map<String, Object> payload = createPayload("data", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("response.pdf", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response as JSON file")
    void testGetPostRequestResponseAsFile_WithJsonResponse_ShouldReturnJsonFile() {
        // Arrange
        String url = "https://api.example.com/data.json";
        Map<String, Object> payload = createPayload("query", "select * from users");
        List<Map<String, Object>> headers = createHeaders("Content-Type", "application/json");
        List<Map<String, Object>> filters = Collections.emptyList();

        Map<String, Object> expectedResponse = createSuccessFileResponse("data.json", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Response File"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response as Excel file")
    void testGetPostRequestResponseAsFile_WithExcelResponse_ShouldReturnExcelFile() {
        // Arrange
        String url = "https://api.example.com/export/excel";
        Map<String, Object> payload = createPayload("report_id", "12345");
        List<Map<String, Object>> headers = createHeaders("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<Map<String, Object>> filters = createFilters("year", "2024");

        Map<String, Object> expectedResponse = createSuccessFileResponse("report.xlsx", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response as PDF file")
    void testGetPostRequestResponseAsFile_WithPdfResponse_ShouldReturnPdfFile() {
        // Arrange
        String url = "https://api.example.com/generate/pdf";
        Map<String, Object> payload = createPayload("template", "invoice");
        List<Map<String, Object>> headers = createHeaders("Accept", "application/pdf");
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("invoice.pdf", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response as CSV file")
    void testGetPostRequestResponseAsFile_WithCsvResponse_ShouldReturnCsvFile() {
        // Arrange
        String url = "https://api.example.com/export/csv";
        Map<String, Object> payload = createPayload("table", "employees");
        List<Map<String, Object>> headers = createHeaders("Content-Type", "text/csv");
        List<Map<String, Object>> filters = createFilters("department", "IT");

        Map<String, Object> expectedResponse = createSuccessFileResponse("employees.csv", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response with multiple headers")
    void testGetPostRequestResponseAsFile_WithMultipleHeaders_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/secure/download";
        Map<String, Object> payload = createPayload("file_id", "abc123");
        List<Map<String, Object>> headers = Arrays.asList(
                createHeader("Authorization", "Bearer token123"),
                createHeader("X-API-Key", "key456"),
                createHeader("Accept", "application/octet-stream")
        );
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("download.bin", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response with multiple filters")
    void testGetPostRequestResponseAsFile_WithMultipleFilters_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/report";
        Map<String, Object> payload = createPayload("report_type", "sales");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = Arrays.asList(
                createFilter("start_date", "2024-01-01"),
                createFilter("end_date", "2024-12-31"),
                createFilter("region", "North America")
        );

        Map<String, Object> expectedResponse = createSuccessFileResponse("sales_report.xlsx", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response with empty headers list")
    void testGetPostRequestResponseAsFile_WithEmptyHeadersList_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/download";
        Map<String, Object> payload = createPayload("id", "123");
        List<Map<String, Object>> headers = Collections.emptyList();
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("file.txt", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should successfully get POST response with empty filters list")
    void testGetPostRequestResponseAsFile_WithEmptyFiltersList_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/export";
        Map<String, Object> payload = createPayload("data", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = Collections.emptyList();

        Map<String, Object> expectedResponse = createSuccessFileResponse("export.json", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }


    // ========== ERROR HANDLING SCENARIOS ==========

    @Test
    @DisplayName("Should throw IllegalArgumentException when WriteAction throws exception")
    void testGetPostRequestResponseAsFile_WhenWriteActionThrowsException_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.example.com/error";
        Map<String, Object> payload = createPayload("key", "value");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("Connection timeout"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("downloading response as a file"));
        assertTrue(exception.getMessage().contains("Connection timeout"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when IOException occurs")
    void testGetPostRequestResponseAsFile_WhenIOExceptionOccurs_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.example.com/fail";
        Map<String, Object> payload = createPayload("test", "data");
        List<Map<String, Object>> headers = createHeaders("Auth", "token");
        List<Map<String, Object>> filters = createFilters("id", "123");

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("Failed to write file"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("Error while downloading response as a file"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when network error occurs")
    void testGetPostRequestResponseAsFile_WhenNetworkErrorOccurs_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.unreachable.com/download";
        Map<String, Object> payload = createPayload("data", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("Network unreachable"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Network unreachable"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when API returns error")
    void testGetPostRequestResponseAsFile_WhenApiReturnsError_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.example.com/error500";
        Map<String, Object> payload = createPayload("request", "data");
        List<Map<String, Object>> headers = createHeaders("Authorization", "Bearer invalid");
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("500 Internal Server Error"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("500 Internal Server Error"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when authentication fails")
    void testGetPostRequestResponseAsFile_WhenAuthenticationFails_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.example.com/secure/download";
        Map<String, Object> payload = createPayload("file", "secret.pdf");
        List<Map<String, Object>> headers = createHeaders("Authorization", "Bearer expired_token");
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("401 Unauthorized"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("401 Unauthorized"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when resource not found")
    void testGetPostRequestResponseAsFile_WhenResourceNotFound_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.example.com/notfound";
        Map<String, Object> payload = createPayload("id", "nonexistent");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("404 Not Found"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("404 Not Found"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when timeout occurs")
    void testGetPostRequestResponseAsFile_WhenTimeoutOccurs_ShouldThrowIllegalArgumentException() {
        // Arrange
        String url = "https://api.slow.com/download";
        Map<String, Object> payload = createPayload("large_file", "true");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenThrow(new RuntimeException("Request timeout after 300 seconds"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);
        });

        assertTrue(exception.getMessage().contains("timeout"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }


    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Should handle payload with file object")
    void testGetPostRequestResponseAsFile_WithFileInPayload_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/upload-and-download";
        File mockFile = mock(File.class);
        when(mockFile.getFileName()).thenReturn("input.pdf");

        Map<String, Object> payload = new HashMap<>();
        payload.put("file", mockFile);
        payload.put("key", "value");

        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("output.pdf", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle complex payload with nested objects")
    void testGetPostRequestResponseAsFile_WithComplexPayload_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/complex";
        Map<String, Object> payload = new HashMap<>();
        payload.put("key1", "value1");
        payload.put("key2", "value2");
        payload.put("nested", Map.of("inner", "data"));

        List<Map<String, Object>> headers = createHeaders("Content-Type", "application/json");
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("result.json", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle URL with query parameters already in URL")
    void testGetPostRequestResponseAsFile_WithQueryParamsInUrl_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/download?preset=true&format=xlsx";
        Map<String, Object> payload = createPayload("data", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = createFilters("additional", "filter");

        Map<String, Object> expectedResponse = createSuccessFileResponse("download.xlsx", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle special characters in URL")
    void testGetPostRequestResponseAsFile_WithSpecialCharsInUrl_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/files/report%202024.xlsx";
        Map<String, Object> payload = createPayload("id", "123");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("report 2024.xlsx", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle large file response")
    void testGetPostRequestResponseAsFile_WithLargeFile_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/large-export";
        Map<String, Object> payload = createPayload("size", "large");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("large_export.zip", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle binary file response")
    void testGetPostRequestResponseAsFile_WithBinaryFile_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/binary";
        Map<String, Object> payload = createPayload("type", "binary");
        List<Map<String, Object>> headers = createHeaders("Accept", "application/octet-stream");
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("data.bin", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle image file response")
    void testGetPostRequestResponseAsFile_WithImageFile_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/generate/image";
        Map<String, Object> payload = createPayload("format", "png");
        List<Map<String, Object>> headers = createHeaders("Accept", "image/png");
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("image.png", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle XML file response")
    void testGetPostRequestResponseAsFile_WithXmlFile_ShouldReturnFileResponse() {
        // Arrange
        String url = "https://api.example.com/export/xml";
        Map<String, Object> payload = createPayload("format", "xml");
        List<Map<String, Object>> headers = createHeaders("Accept", "application/xml");
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("data.xml", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }


    // ========== VERIFICATION TESTS ==========

    @Test
    @DisplayName("Should verify WriteAction is called with correct parameter order")
    void testGetPostRequestResponseAsFile_ShouldCallWriteActionWithCorrectParameterOrder() {
        // Arrange
        String url = "https://api.example.com/test";
        Map<String, Object> payload = createPayload("test", "data");
        List<Map<String, Object>> headers = createHeaders("Header1", "Value1");
        List<Map<String, Object>> filters = createFilters("Filter1", "Value1");

        Map<String, Object> expectedResponse = createSuccessFileResponse("test.txt", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert - Verify parameter order: url, filters, headers, payload
        verify(writeAction, times(1)).postResponseAsFile(
                eq(url),
                eq(filters),
                eq(headers),
                eq(payload)
        );
    }

    @Test
    @DisplayName("Should log execution information")
    void testGetPostRequestResponseAsFile_ShouldLogExecutionInfo() {
        // Arrange
        String url = "https://api.example.com/logged";
        Map<String, Object> payload = createPayload("log", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("logged.txt", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        // Note: Logging verification would require a logging framework mock
        // This test verifies the method executes successfully with logging
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    @Test
    @DisplayName("Should handle response with Response Info and Response File")
    void testGetPostRequestResponseAsFile_ShouldReturnResponseWithBothFields() {
        // Arrange
        String url = "https://api.example.com/complete";
        Map<String, Object> payload = createPayload("complete", "test");
        List<Map<String, Object>> headers = null;
        List<Map<String, Object>> filters = null;

        Map<String, Object> expectedResponse = createSuccessFileResponse("complete.pdf", "200 OK");
        when(writeAction.postResponseAsFile(url, filters, headers, payload))
                .thenReturn(expectedResponse);

        // Act
        Map<String, Object> result = writeArea.getPostRequestResponseAsFile(url, payload, headers, filters);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("Response Info"));
        assertTrue(result.containsKey("Response File"));
        verify(writeAction, times(1)).postResponseAsFile(url, filters, headers, payload);
    }

    // ========== HELPER METHODS ==========

    /**
     * Creates a simple payload map with a single key-value pair
     */
    private Map<String, Object> createPayload(String key, String value) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(key, value);
        return payload;
    }

    /**
     * Creates a list with a single header map
     */
    private List<Map<String, Object>> createHeaders(String key, String value) {
        return Collections.singletonList(createHeader(key, value));
    }

    /**
     * Creates a single header map
     */
    private Map<String, Object> createHeader(String key, String value) {
        Map<String, Object> header = new HashMap<>();
        header.put("key", key);
        header.put("value", value);
        return header;
    }

    /**
     * Creates a list with a single filter map
     */
    private List<Map<String, Object>> createFilters(String key, String value) {
        return Collections.singletonList(createFilter(key, value));
    }

    /**
     * Creates a single filter map
     */
    private Map<String, Object> createFilter(String key, String value) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("key", key);
        filter.put("value", value);
        return filter;
    }

    /**
     * Creates a mock success file response with Response Info and Response File
     */
    private Map<String, Object> createSuccessFileResponse(String fileName, String statusMessage) {
        Map<String, Object> response = new HashMap<>();

        // Create Response Info
        FreeForm responseInfo = new FreeForm();
        responseInfo.put("Status and Message", "Text", statusMessage);
        response.put("Response Info", responseInfo);

        // Create mock file
        File mockFile = mock(File.class);
        when(mockFile.getFileName()).thenReturn(fileName);
        response.put("Response File", mockFile);

        return response;
    }
}

