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

package app.krista.extensions.development.api.rest.util;

import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link KristaMediaClient}.
 * <p>
 * Tests file conversion functionality including:
 * - Java file to Krista file conversion
 * - Krista file to Java file conversion
 * - Unsupported file format detection
 * - ZIP compression for unsupported formats
 * - File extension extraction
 * </p>
 */
class KristaMediaClientTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileHandle fileHandle;

    @Mock
    private app.krista.model.base.File kristaFile;

    private KristaMediaClient kristaMediaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kristaMediaClient = new KristaMediaClient();
        // Use reflection to inject the mock FileRepository
        try {
            java.lang.reflect.Field field = KristaMediaClient.class.getDeclaredField("fileRepository");
            field.setAccessible(true);
            field.set(kristaMediaClient, fileRepository);
        } catch (Exception e) {
            fail("Failed to inject mock FileRepository: " + e.getMessage());
        }
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        KristaMediaClient client = new KristaMediaClient();

        // Assert
        assertNotNull(client);
    }

    // ========== Constants Tests ==========

    @Test
    void testZipDir_ShouldHaveCorrectValue() {
        // Assert
        assertEquals("/tmp/", KristaMediaClient.ZIP_DIR);
    }

    @Test
    void testZipDir_ShouldNotBeNull() {
        // Assert
        assertNotNull(KristaMediaClient.ZIP_DIR);
    }

    @Test
    void testZipDir_ShouldEndWithSlash() {
        // Assert
        assertTrue(KristaMediaClient.ZIP_DIR.endsWith("/"));
    }

    // ========== Unsupported File Format Tests ==========

    @Test
    void testToKristaFile_WithSupportedFormat_ShouldUploadDirectly() throws IOException {
        // Arrange
        File tempFile = createTempFile("test.txt", "Test content");
        when(fileRepository.createNewFileByName(anyString())).thenReturn(fileHandle);
        when(fileHandle.getFile()).thenReturn(kristaFile);
        doNothing().when(fileHandle).setContent(any(InputStream.class));
        doNothing().when(fileHandle).close();

        // Act
        app.krista.model.base.File result = kristaMediaClient.toKristaFile(tempFile);

        // Assert
        assertNotNull(result);
        verify(fileRepository).createNewFileByName("test.txt");
        verify(fileHandle).setContent(any(InputStream.class));
        verify(fileHandle).getFile();

        // Cleanup
        tempFile.delete();
    }

    @Test
    void testToKristaFile_WithPdfFile_ShouldUploadDirectly() throws IOException {
        // Arrange
        File tempFile = createTempFile("document.pdf", "PDF content");
        when(fileRepository.createNewFileByName(anyString())).thenReturn(fileHandle);
        when(fileHandle.getFile()).thenReturn(kristaFile);
        doNothing().when(fileHandle).setContent(any(InputStream.class));
        doNothing().when(fileHandle).close();

        // Act
        app.krista.model.base.File result = kristaMediaClient.toKristaFile(tempFile);

        // Assert
        assertNotNull(result);
        verify(fileRepository).createNewFileByName("document.pdf");

        // Cleanup
        tempFile.delete();
    }

    @Test
    void testToKristaFile_WithImageFile_ShouldUploadDirectly() throws IOException {
        // Arrange
        File tempFile = createTempFile("photo.jpg", "Image content");
        when(fileRepository.createNewFileByName(anyString())).thenReturn(fileHandle);
        when(fileHandle.getFile()).thenReturn(kristaFile);
        doNothing().when(fileHandle).setContent(any(InputStream.class));
        doNothing().when(fileHandle).close();

        // Act
        app.krista.model.base.File result = kristaMediaClient.toKristaFile(tempFile);

        // Assert
        assertNotNull(result);
        verify(fileRepository).createNewFileByName("photo.jpg");

        // Cleanup
        tempFile.delete();
    }

    // ========== ZIP Compression Tests ==========

    @Test
    void testCompressFile_WithValidFile_ShouldCreateZipFile() throws IOException {
        // Arrange
        File sourceFile = createTempFile("test.txt", "Test content for compression");
        String zipFilePath = KristaMediaClient.ZIP_DIR + "test.zip";

        // Act
        KristaMediaClient.compressFile(zipFilePath, sourceFile.getAbsolutePath());

        // Assert
        File zipFile = new File(zipFilePath);
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);

        // Verify ZIP content
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry);
            assertEquals("test.txt", entry.getName());
        }

        // Cleanup
        sourceFile.delete();
        zipFile.delete();
    }

    @Test
    void testCompressFile_WithLargeFile_ShouldCompressSuccessfully() throws IOException {
        // Arrange
        File sourceFile = createTempFile("large.txt", generateLargeContent(10000));
        String zipFilePath = KristaMediaClient.ZIP_DIR + "large.zip";

        // Act
        KristaMediaClient.compressFile(zipFilePath, sourceFile.getAbsolutePath());

        // Assert
        File zipFile = new File(zipFilePath);
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
        assertTrue(zipFile.length() < sourceFile.length()); // Compressed should be smaller

        // Cleanup
        sourceFile.delete();
        zipFile.delete();
    }

    @Test
    void testCompressFile_WithEmptyFile_ShouldCreateZipFile() throws IOException {
        // Arrange
        File sourceFile = createTempFile("empty.txt", "");
        String zipFilePath = KristaMediaClient.ZIP_DIR + "empty.zip";

        // Act
        KristaMediaClient.compressFile(zipFilePath, sourceFile.getAbsolutePath());

        // Assert
        File zipFile = new File(zipFilePath);
        assertTrue(zipFile.exists());

        // Cleanup
        sourceFile.delete();
        zipFile.delete();
    }

    // ========== Krista File to Java File Tests ==========

    @Test
    void testToJavaFile_WithValidKristaFile_ShouldReturnJavaFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        when(kristaFile.getFileName()).thenReturn(fileName);
        when(fileRepository.getFile(kristaFile)).thenReturn(fileHandle);
        when(fileHandle.getContent()).thenReturn(inputStream);
        doNothing().when(fileHandle).close();

        // Act
        File result = kristaMediaClient.toJavaFile(kristaFile);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());
        assertTrue(result.exists());
        assertTrue(result.length() > 0);

        // Cleanup
        result.delete();
    }

    @Test
    void testToJavaFile_WithEmptyKristaFile_ShouldReturnEmptyJavaFile() throws IOException {
        // Arrange
        String fileName = "empty.txt";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        when(kristaFile.getFileName()).thenReturn(fileName);
        when(fileRepository.getFile(kristaFile)).thenReturn(fileHandle);
        when(fileHandle.getContent()).thenReturn(inputStream);
        doNothing().when(fileHandle).close();

        // Act
        File result = kristaMediaClient.toJavaFile(kristaFile);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());
        assertTrue(result.exists());
        assertEquals(0, result.length());

        // Cleanup
        result.delete();
    }

    @Test
    void testToJavaFile_WithLargeKristaFile_ShouldHandleCorrectly() throws IOException {
        // Arrange
        String fileName = "large.txt";
        String content = generateLargeContent(5000);
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        when(kristaFile.getFileName()).thenReturn(fileName);
        when(fileRepository.getFile(kristaFile)).thenReturn(fileHandle);
        when(fileHandle.getContent()).thenReturn(inputStream);
        doNothing().when(fileHandle).close();

        // Act
        File result = kristaMediaClient.toJavaFile(kristaFile);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());
        assertTrue(result.exists());
        assertTrue(result.length() > 0);

        // Cleanup
        result.delete();
    }

    // ========== Edge Case Tests ==========

    @Test
    void testToKristaFile_WithFileNameWithoutExtension_ShouldThrowException() throws IOException {
        // Arrange
        File tempFile = createTempFile("README", "Content");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> kristaMediaClient.toKristaFile(tempFile));

        // Cleanup
        tempFile.delete();
    }

    @Test
    void testToJavaFile_WithSpecialCharactersInFileName_ShouldHandleCorrectly() throws IOException {
        // Arrange
        String fileName = "my-file (1).txt";
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        when(kristaFile.getFileName()).thenReturn(fileName);
        when(fileRepository.getFile(kristaFile)).thenReturn(fileHandle);
        when(fileHandle.getContent()).thenReturn(inputStream);
        doNothing().when(fileHandle).close();

        // Act
        File result = kristaMediaClient.toJavaFile(kristaFile);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());

        // Cleanup
        result.delete();
    }

    @Test
    void testCompressFile_WithNonExistentFile_ShouldThrowException() {
        // Arrange
        String nonExistentFile = "/tmp/non-existent-file.txt";
        String zipFilePath = KristaMediaClient.ZIP_DIR + "test.zip";

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> 
            KristaMediaClient.compressFile(zipFilePath, nonExistentFile)
        );
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testToKristaFile_WithDocxFile_ShouldUploadDirectly() throws IOException {
        // Arrange
        File tempFile = createTempFile("document.docx", "Word document content");
        when(fileRepository.createNewFileByName(anyString())).thenReturn(fileHandle);
        when(fileHandle.getFile()).thenReturn(kristaFile);
        doNothing().when(fileHandle).setContent(any(InputStream.class));
        doNothing().when(fileHandle).close();

        // Act
        app.krista.model.base.File result = kristaMediaClient.toKristaFile(tempFile);

        // Assert
        assertNotNull(result);
        verify(fileRepository).createNewFileByName("document.docx");

        // Cleanup
        tempFile.delete();
    }

    @Test
    void testToKristaFile_WithXlsxFile_ShouldUploadDirectly() throws IOException {
        // Arrange
        File tempFile = createTempFile("spreadsheet.xlsx", "Excel content");
        when(fileRepository.createNewFileByName(anyString())).thenReturn(fileHandle);
        when(fileHandle.getFile()).thenReturn(kristaFile);
        doNothing().when(fileHandle).setContent(any(InputStream.class));
        doNothing().when(fileHandle).close();

        // Act
        app.krista.model.base.File result = kristaMediaClient.toKristaFile(tempFile);

        // Assert
        assertNotNull(result);
        verify(fileRepository).createNewFileByName("spreadsheet.xlsx");

        // Cleanup
        tempFile.delete();
    }

    @Test
    void testToJavaFile_WithPdfKristaFile_ShouldConvertCorrectly() throws IOException {
        // Arrange
        String fileName = "report.pdf";
        String content = "PDF content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        when(kristaFile.getFileName()).thenReturn(fileName);
        when(fileRepository.getFile(kristaFile)).thenReturn(fileHandle);
        when(fileHandle.getContent()).thenReturn(inputStream);
        doNothing().when(fileHandle).close();

        // Act
        File result = kristaMediaClient.toJavaFile(kristaFile);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());
        assertTrue(result.exists());

        // Cleanup
        result.delete();
    }

    // ========== Helper Methods ==========

    private File createTempFile(String fileName, String content) throws IOException {
        File tempFile = new File(fileName);
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    private String generateLargeContent(int lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            sb.append("This is line ").append(i).append(" of the large content file.\n");
        }
        return sb.toString();
    }
}

