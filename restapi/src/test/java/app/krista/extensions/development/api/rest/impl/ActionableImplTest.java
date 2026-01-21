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
import okhttp3.*;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ActionableImpl} class.
 * <p>
 * Tests verify the ActionableImpl constructor and basic methods.
 * Note: Full integration tests for the execute() and testConnection() methods would require
 * complex mocking of HTTP calls and are better suited for integration testing.
 * </p>
 */
class ActionableImplTest {

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
        ActionableImpl actionable = new ActionableImpl(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable);
    }

    @Test
    void testConstructor_WithNullRestApiAttributes_ShouldCreateInstance() {
        // Arrange & Act
        ActionableImpl actionable = new ActionableImpl(
                null,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable);
    }

    @Test
    void testConstructor_WithAllNullParameters_ShouldCreateInstance() {
        // Arrange & Act
        ActionableImpl actionable = new ActionableImpl(null, null, null, null, null);

        // Assert
        assertNotNull(actionable);
    }

    // ========== Getter Method Tests ==========

    @Test
    void testGetRestApiAttributes_WithValidAttributes_ShouldReturnAttributes() {
        // Arrange
        ActionableImpl actionable = new ActionableImpl(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Act
        RestApiAttributes result = actionable.getRestApiAttributes();

        // Assert
        assertEquals(restApiAttributes, result);
    }

    @Test
    void testGetRestApiAttributes_WithNullAttributes_ShouldReturnNull() {
        // Arrange
        ActionableImpl actionable = new ActionableImpl(
                null,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Act
        RestApiAttributes result = actionable.getRestApiAttributes();

        // Assert
        assertNull(result);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_ForBasicAuthScenario_ShouldCreateInstance() {
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
        ActionableImpl actionable = new ActionableImpl(
                basicAuthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable);
        assertEquals(basicAuthAttributes, actionable.getRestApiAttributes());
    }

    @Test
    void testConstructor_ForTokenAuthScenario_ShouldCreateInstance() {
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
        ActionableImpl actionable = new ActionableImpl(
                tokenAuthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable);
        assertEquals(tokenAuthAttributes, actionable.getRestApiAttributes());
    }

    @Test
    void testConstructor_ForOAuthScenario_ShouldCreateInstance() {
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
        ActionableImpl actionable = new ActionableImpl(
                oauthAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable);
        assertEquals(oauthAttributes, actionable.getRestApiAttributes());
    }

    @Test
    void testConstructor_WithMultipleCalls_ShouldCreateMultipleInstances() {
        // Arrange & Act
        ActionableImpl actionable1 = new ActionableImpl(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );
        ActionableImpl actionable2 = new ActionableImpl(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Assert
        assertNotNull(actionable1);
        assertNotNull(actionable2);
        assertNotSame(actionable1, actionable2, "Should create different instances");
    }

    @Test
    void testGetRestApiAttributes_AfterConstruction_ShouldReturnSameInstance() {
        // Arrange
        ActionableImpl actionable = new ActionableImpl(
                restApiAttributes,
                refreshTokenStore,
                invoker,
                attributeStore,
                kristaMediaClient
        );

        // Act
        RestApiAttributes result1 = actionable.getRestApiAttributes();
        RestApiAttributes result2 = actionable.getRestApiAttributes();

        // Assert
        assertSame(result1, result2, "Should return the same instance");
        assertEquals(restApiAttributes, result1);
    }

    // ========== RenameReservedKeys Tests ==========

    @Nested
    @DisplayName("renameReservedKeys method tests")
    class RenameReservedKeysTests {

        private Method renameReservedKeysMethod;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            renameReservedKeysMethod = ActionableImpl.class.getDeclaredMethod("renameReservedKeys", Map.class);
            renameReservedKeysMethod.setAccessible(true);
        }

        private void invokeRenameReservedKeys(Map<Object, Object> map) throws Exception {
            renameReservedKeysMethod.invoke(null, map);
        }

        @Test
        @DisplayName("Should rename 'type' key to '_type' at root level")
        void testRenameTypeKeyAtRootLevel() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("type", "City");
            map.put("name", "New Delhi");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertFalse(map.containsKey("type"), "Original 'type' key should be removed");
            assertTrue(map.containsKey("_type"), "Renamed '_type' key should exist");
            assertEquals("City", map.get("_type"), "Value should be preserved");
            assertEquals("New Delhi", map.get("name"), "Other keys should be unchanged");
        }

        @Test
        @DisplayName("Should rename 'type' key in nested object")
        void testRenameTypeKeyInNestedObject() throws Exception {
            // Arrange
            Map<Object, Object> nestedMap = new LinkedHashMap<>();
            nestedMap.put("type", "City");
            nestedMap.put("query", "New Delhi");

            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("request", nestedMap);
            map.put("status", "success");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            @SuppressWarnings("unchecked")
            Map<Object, Object> resultNested = (Map<Object, Object>) map.get("request");
            assertFalse(resultNested.containsKey("type"), "Nested 'type' key should be removed");
            assertTrue(resultNested.containsKey("_type"), "Nested '_type' key should exist");
            assertEquals("City", resultNested.get("_type"), "Nested value should be preserved");
        }

        @Test
        @DisplayName("Should rename 'type' key in list of objects")
        void testRenameTypeKeyInListOfObjects() throws Exception {
            // Arrange
            Map<Object, Object> item1 = new LinkedHashMap<>();
            item1.put("type", "City");
            item1.put("name", "Delhi");

            Map<Object, Object> item2 = new LinkedHashMap<>();
            item2.put("type", "Country");
            item2.put("name", "India");

            List<Map<Object, Object>> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);

            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("items", list);

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            @SuppressWarnings("unchecked")
            List<Map<Object, Object>> resultList = (List<Map<Object, Object>>) map.get("items");
            assertFalse(resultList.get(0).containsKey("type"));
            assertTrue(resultList.get(0).containsKey("_type"));
            assertEquals("City", resultList.get(0).get("_type"));
            assertFalse(resultList.get(1).containsKey("type"));
            assertTrue(resultList.get(1).containsKey("_type"));
            assertEquals("Country", resultList.get(1).get("_type"));
        }

        @Test
        @DisplayName("Should handle case insensitive 'TYPE' key")
        void testRenameTypeKeyCaseInsensitive() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("TYPE", "City");
            map.put("Type", "Country");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertFalse(map.containsKey("TYPE"), "Uppercase 'TYPE' should be removed");
            assertFalse(map.containsKey("Type"), "Mixed case 'Type' should be removed");
            assertTrue(map.containsKey("_type"), "Renamed '_type' key should exist");
        }

        @Test
        @DisplayName("Should not modify map without 'type' key")
        void testNoChangeWhenNoTypeKey() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("name", "New Delhi");
            map.put("country", "India");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertEquals(2, map.size());
            assertEquals("New Delhi", map.get("name"));
            assertEquals("India", map.get("country"));
            assertFalse(map.containsKey("_type"));
        }

        @Test
        @DisplayName("Should handle empty map")
        void testEmptyMap() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertTrue(map.isEmpty());
        }

        @Test
        @DisplayName("Should handle null map without exception")
        void testNullMap() throws Exception {
            // Act & Assert - should not throw exception
            assertDoesNotThrow(() -> invokeRenameReservedKeys(null));
        }

        @Test
        @DisplayName("Should handle deeply nested structures")
        void testDeeplyNestedStructure() throws Exception {
            // Arrange
            Map<Object, Object> level3 = new LinkedHashMap<>();
            level3.put("type", "DeepValue");

            Map<Object, Object> level2 = new LinkedHashMap<>();
            level2.put("nested", level3);
            level2.put("type", "Level2Value");

            Map<Object, Object> level1 = new LinkedHashMap<>();
            level1.put("data", level2);
            level1.put("type", "Level1Value");

            // Act
            invokeRenameReservedKeys(level1);

            // Assert
            assertTrue(level1.containsKey("_type"));
            assertEquals("Level1Value", level1.get("_type"));

            @SuppressWarnings("unchecked")
            Map<Object, Object> resultLevel2 = (Map<Object, Object>) level1.get("data");
            assertTrue(resultLevel2.containsKey("_type"));
            assertEquals("Level2Value", resultLevel2.get("_type"));

            @SuppressWarnings("unchecked")
            Map<Object, Object> resultLevel3 = (Map<Object, Object>) resultLevel2.get("nested");
            assertTrue(resultLevel3.containsKey("_type"));
            assertEquals("DeepValue", resultLevel3.get("_type"));
        }

        @Test
        @DisplayName("Should rename 'description' key to '_description' at root level")
        void testRenameDescriptionKeyAtRootLevel() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("description", "A beautiful city");
            map.put("name", "New Delhi");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertFalse(map.containsKey("description"), "Original 'description' key should be removed");
            assertTrue(map.containsKey("_description"), "Renamed '_description' key should exist");
            assertEquals("A beautiful city", map.get("_description"), "Value should be preserved");
            assertEquals("New Delhi", map.get("name"), "Other keys should be unchanged");
        }

        @Test
        @DisplayName("Should rename 'description' key in nested object")
        void testRenameDescriptionKeyInNestedObject() throws Exception {
            // Arrange
            Map<Object, Object> nestedMap = new LinkedHashMap<>();
            nestedMap.put("description", "Capital city");
            nestedMap.put("population", "30 million");

            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("city", nestedMap);
            map.put("status", "active");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            @SuppressWarnings("unchecked")
            Map<Object, Object> resultNested = (Map<Object, Object>) map.get("city");
            assertFalse(resultNested.containsKey("description"), "Nested 'description' key should be removed");
            assertTrue(resultNested.containsKey("_description"), "Nested '_description' key should exist");
            assertEquals("Capital city", resultNested.get("_description"), "Nested value should be preserved");
        }

        @Test
        @DisplayName("Should rename 'description' key in list of objects")
        void testRenameDescriptionKeyInListOfObjects() throws Exception {
            // Arrange
            Map<Object, Object> item1 = new LinkedHashMap<>();
            item1.put("description", "Metropolitan city");
            item1.put("name", "Delhi");

            Map<Object, Object> item2 = new LinkedHashMap<>();
            item2.put("description", "South Asian nation");
            item2.put("name", "India");

            List<Map<Object, Object>> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);

            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("items", list);

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            @SuppressWarnings("unchecked")
            List<Map<Object, Object>> resultList = (List<Map<Object, Object>>) map.get("items");
            assertFalse(resultList.get(0).containsKey("description"));
            assertTrue(resultList.get(0).containsKey("_description"));
            assertEquals("Metropolitan city", resultList.get(0).get("_description"));
            assertFalse(resultList.get(1).containsKey("description"));
            assertTrue(resultList.get(1).containsKey("_description"));
            assertEquals("South Asian nation", resultList.get(1).get("_description"));
        }

        @Test
        @DisplayName("Should handle case insensitive 'DESCRIPTION' key")
        void testRenameDescriptionKeyCaseInsensitive() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("DESCRIPTION", "Uppercase description");
            map.put("Description", "Mixed case description");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertFalse(map.containsKey("DESCRIPTION"), "Uppercase 'DESCRIPTION' should be removed");
            assertFalse(map.containsKey("Description"), "Mixed case 'Description' should be removed");
            assertTrue(map.containsKey("_description"), "Renamed '_description' key should exist");
        }

        @Test
        @DisplayName("Should rename both 'type' and 'description' keys together")
        void testRenameBothTypeAndDescription() throws Exception {
            // Arrange
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("type", "City");
            map.put("description", "A beautiful city");
            map.put("name", "New Delhi");

            // Act
            invokeRenameReservedKeys(map);

            // Assert
            assertFalse(map.containsKey("type"), "Original 'type' key should be removed");
            assertFalse(map.containsKey("description"), "Original 'description' key should be removed");
            assertTrue(map.containsKey("_type"), "Renamed '_type' key should exist");
            assertTrue(map.containsKey("_description"), "Renamed '_description' key should exist");
            assertEquals("City", map.get("_type"), "Type value should be preserved");
            assertEquals("A beautiful city", map.get("_description"), "Description value should be preserved");
            assertEquals("New Delhi", map.get("name"), "Other keys should be unchanged");
        }

        @Test
        @DisplayName("Should handle deeply nested structures with description")
        void testDeeplyNestedStructureWithDescription() throws Exception {
            // Arrange
            Map<Object, Object> level3 = new LinkedHashMap<>();
            level3.put("description", "Deep description");
            level3.put("type", "DeepValue");

            Map<Object, Object> level2 = new LinkedHashMap<>();
            level2.put("nested", level3);
            level2.put("description", "Level 2 description");

            Map<Object, Object> level1 = new LinkedHashMap<>();
            level1.put("data", level2);
            level1.put("description", "Level 1 description");

            // Act
            invokeRenameReservedKeys(level1);

            // Assert
            assertTrue(level1.containsKey("_description"));
            assertEquals("Level 1 description", level1.get("_description"));

            @SuppressWarnings("unchecked")
            Map<Object, Object> resultLevel2 = (Map<Object, Object>) level1.get("data");
            assertTrue(resultLevel2.containsKey("_description"));
            assertEquals("Level 2 description", resultLevel2.get("_description"));

            @SuppressWarnings("unchecked")
            Map<Object, Object> resultLevel3 = (Map<Object, Object>) resultLevel2.get("nested");
            assertTrue(resultLevel3.containsKey("_description"));
            assertEquals("Deep description", resultLevel3.get("_description"));
            assertTrue(resultLevel3.containsKey("_type"));
            assertEquals("DeepValue", resultLevel3.get("_type"));
        }

    }

    // ========== Sanitize Filename Tests ==========

    @Nested
    @DisplayName("Sanitize Filename Tests")
    class SanitizeFileNameTests {

        private ActionableImpl actionable;

        @BeforeEach
        void setUp() {
            actionable = new ActionableImpl(
                    restApiAttributes,
                    refreshTokenStore,
                    invoker,
                    attributeStore,
                    kristaMediaClient
            );
        }

        private String invokeSanitizeFileName(String fileName) throws Exception {
            Method method = ActionableImpl.class.getDeclaredMethod("sanitizeFileName", String.class);
            method.setAccessible(true);
            return (String) method.invoke(actionable, fileName);
        }

        @Test
        @DisplayName("Should sanitize path traversal attack with parent directory references")
        void testSanitizeFileName_WithPathTraversal_ShouldSanitize() throws Exception {
            // Arrange
            String maliciousFileName = "../../etc/passwd";

            // Act
            String result = invokeSanitizeFileName(maliciousFileName);

            // Assert
            assertFalse(result.contains(".."), "Should remove parent directory references");
            assertFalse(result.contains("/"), "Should remove forward slashes");
            // ../../etc/passwd -> first / becomes _, then .. becomes _, then / becomes _, then .. becomes _
            // Result: ____etc_passwd (4 underscores before etc, 1 underscore before passwd)
            assertEquals("____etc_passwd", result);
        }

        @Test
        @DisplayName("Should sanitize filename with forward slashes")
        void testSanitizeFileName_WithForwardSlashes_ShouldReplaceThem() throws Exception {
            // Arrange
            String fileName = "path/to/file.pdf";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertFalse(result.contains("/"), "Should remove forward slashes");
            assertEquals("path_to_file.pdf", result);
        }

        @Test
        @DisplayName("Should sanitize filename with backslashes")
        void testSanitizeFileName_WithBackslashes_ShouldReplaceThem() throws Exception {
            // Arrange
            String fileName = "path\\to\\file.pdf";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertFalse(result.contains("\\"), "Should remove backslashes");
            assertEquals("path_to_file.pdf", result);
        }

        @Test
        @DisplayName("Should return default filename for null input")
        void testSanitizeFileName_WithNull_ShouldReturnDefault() throws Exception {
            // Arrange
            String fileName = null;

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("download.bin", result);
        }

        @Test
        @DisplayName("Should return default filename for empty string")
        void testSanitizeFileName_WithEmptyString_ShouldReturnDefault() throws Exception {
            // Arrange
            String fileName = "";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("download.bin", result);
        }

        @Test
        @DisplayName("Should return default filename for blank string")
        void testSanitizeFileName_WithBlankString_ShouldReturnDefault() throws Exception {
            // Arrange
            String fileName = "   ";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("download.bin", result);
        }

        @Test
        @DisplayName("Should preserve valid filename")
        void testSanitizeFileName_WithValidFilename_ShouldPreserve() throws Exception {
            // Arrange
            String fileName = "report.pdf";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("report.pdf", result);
        }

        @Test
        @DisplayName("Should preserve filename with spaces and special characters")
        void testSanitizeFileName_WithSpacesAndSpecialChars_ShouldPreserve() throws Exception {
            // Arrange
            String fileName = "my-document (1).pdf";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("my-document (1).pdf", result);
        }

        @Test
        @DisplayName("Should truncate very long filename while preserving extension")
        void testSanitizeFileName_WithVeryLongFilename_ShouldTruncate() throws Exception {
            // Arrange
            String longName = "a".repeat(300) + ".pdf";

            // Act
            String result = invokeSanitizeFileName(longName);

            // Assert
            assertTrue(result.length() <= 255, "Should truncate to max 255 characters");
            assertTrue(result.endsWith(".pdf"), "Should preserve extension");
        }

        @Test
        @DisplayName("Should handle filename with multiple dots")
        void testSanitizeFileName_WithMultipleDots_ShouldPreserve() throws Exception {
            // Arrange
            String fileName = "my.file.name.pdf";

            // Act
            String result = invokeSanitizeFileName(fileName);

            // Assert
            assertEquals("my.file.name.pdf", result);
        }
    }

    // ========== Convert Response To File Tests ==========

    @Nested
    @DisplayName("Convert Response To File Tests")
    class ConvertResponseToFileTests {

        private ActionableImpl actionable;

        @BeforeEach
        void setUp() {
            actionable = new ActionableImpl(
                    restApiAttributes,
                    refreshTokenStore,
                    invoker,
                    attributeStore,
                    kristaMediaClient
            );
        }

        private File invokeConvertResponseToFile(Response response) throws Exception {
            Method method = ActionableImpl.class.getDeclaredMethod("convertResponseToFile", Response.class);
            method.setAccessible(true);
            return (File) method.invoke(actionable, response);
        }

        private Response createMockResponse(String contentDisposition, String contentType, String content) throws IOException {
            Response mockResponse = mock(Response.class);

            // Create response body with content
            Buffer buffer = new Buffer();
            buffer.writeUtf8(content);
            ResponseBody responseBody = ResponseBody.create(
                    MediaType.parse(contentType != null ? contentType : "application/octet-stream"),
                    buffer.readByteArray()
            );

            when(mockResponse.body()).thenReturn(responseBody);
            when(mockResponse.header("Content-Disposition")).thenReturn(contentDisposition);
            when(mockResponse.header("Content-Type")).thenReturn(contentType);

            return mockResponse;
        }

        @Test
        @DisplayName("Should preserve original filename from Content-Disposition header")
        void testConvertResponseToFile_WithContentDisposition_ShouldPreserveFilename() throws Exception {
            // Arrange
            String expectedFileName = "report.pdf";
            Response response = createMockResponse(
                    "attachment; filename=\"" + expectedFileName + "\"",
                    "application/pdf",
                    "PDF content here"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertEquals(expectedFileName, result.getName(), "Filename should be preserved from Content-Disposition");
            assertTrue(result.exists(), "File should exist");

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should NOT use random filename like api_response_*")
        void testConvertResponseToFile_ShouldNotUseRandomFilename() throws Exception {
            // Arrange
            String expectedFileName = "document.pdf";
            Response response = createMockResponse(
                    "attachment; filename=\"" + expectedFileName + "\"",
                    "application/pdf",
                    "PDF content"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertFalse(result.getName().startsWith("api_response_"),
                    "Filename should NOT start with api_response_");
            assertEquals(expectedFileName, result.getName());

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should use default filename when Content-Disposition is missing")
        void testConvertResponseToFile_WithoutContentDisposition_ShouldUseDefault() throws Exception {
            // Arrange
            Response response = createMockResponse(
                    null,
                    "application/pdf",
                    "PDF content"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertTrue(result.getName().startsWith("api_response"),
                    "Should use default filename when Content-Disposition is missing");
            assertTrue(result.getName().endsWith(".pdf"),
                    "Should use extension from Content-Type");

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should create file in temp directory")
        void testConvertResponseToFile_ShouldCreateInTempDirectory() throws Exception {
            // Arrange
            Response response = createMockResponse(
                    "attachment; filename=\"test.pdf\"",
                    "application/pdf",
                    "PDF content"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            String tempDir = System.getProperty("java.io.tmpdir");
            assertTrue(result.getAbsolutePath().startsWith(tempDir),
                    "File should be created in temp directory");

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should write content to file correctly")
        void testConvertResponseToFile_ShouldWriteContentCorrectly() throws Exception {
            // Arrange
            String expectedContent = "This is test content for the file";
            Response response = createMockResponse(
                    "attachment; filename=\"test.txt\"",
                    "text/plain",
                    expectedContent
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertTrue(result.exists());
            String actualContent = Files.readString(result.toPath());
            assertEquals(expectedContent, actualContent, "File content should match");

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should throw exception when response is null")
        void testConvertResponseToFile_WithNullResponse_ShouldThrowException() {
            // Arrange
            Response response = null;

            // Act & Assert
            Exception exception = assertThrows(Exception.class, () -> {
                invokeConvertResponseToFile(response);
            });

            assertTrue(exception.getCause() instanceof IllegalArgumentException ||
                      exception.getMessage().contains("null"),
                      "Should throw exception for null response");
        }

        @Test
        @DisplayName("Should sanitize malicious filename from Content-Disposition")
        void testConvertResponseToFile_WithMaliciousFilename_ShouldSanitize() throws Exception {
            // Arrange
            Response response = createMockResponse(
                    "attachment; filename=\"../../etc/passwd\"",
                    "text/plain",
                    "malicious content"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertFalse(result.getName().contains(".."), "Should sanitize parent directory references");
            assertFalse(result.getName().contains("/"), "Should sanitize path separators");

            // Cleanup
            result.delete();
        }

        @Test
        @DisplayName("Should handle filename with spaces and special characters")
        void testConvertResponseToFile_WithSpecialCharsInFilename_ShouldHandle() throws Exception {
            // Arrange
            String fileName = "my-document (1).pdf";
            Response response = createMockResponse(
                    "attachment; filename=\"" + fileName + "\"",
                    "application/pdf",
                    "PDF content"
            );

            // Act
            File result = invokeConvertResponseToFile(response);

            // Assert
            assertNotNull(result);
            assertEquals(fileName, result.getName());

            // Cleanup
            result.delete();
        }
    }
}
