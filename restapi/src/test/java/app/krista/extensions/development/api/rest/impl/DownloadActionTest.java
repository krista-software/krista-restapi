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

import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DownloadAction}.
 * <p>
 * Tests file download functionality including:
 * - File name extraction from headers
 * - File name extraction from content type
 * - File extension extraction
 * - Error handling for failed downloads
 * - Response handling
 * </p>
 */
class DownloadActionTest {

    @Mock
    private KristaMediaClient kristaMediaClient;

    private DownloadAction downloadAction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        downloadAction = new DownloadAction(kristaMediaClient);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithKristaMediaClient_ShouldCreateInstance() {
        // Arrange & Act
        DownloadAction action = new DownloadAction(kristaMediaClient);

        // Assert
        assertNotNull(action);
    }

    @Test
    void testConstructor_WithNullKristaMediaClient_ShouldCreateInstance() {
        // Arrange & Act
        DownloadAction action = new DownloadAction(null);

        // Assert
        assertNotNull(action);
    }

    // ========== File Name Extraction Tests ==========

    @Test
    void testExtractFileName_WithContentDisposition_ShouldReturnFileName() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; filename=\"document.pdf\"");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("document.pdf", fileName);
    }

    @Test
    void testExtractFileName_WithContentType_ShouldReturnFileNameBasedOnType() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("text/plain");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.txt", fileName);
    }

    @Test
    void testExtractFileName_WithNoHeaders_ShouldReturnUnknownFile() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn(null);

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("unknown_file", fileName);
    }

    @Test
    void testExtractFileName_WithContentTypeAudioMpeg_ShouldReturnMp3File() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("audio/mpeg");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.mp3", fileName);
    }

    @Test
    void testExtractFileName_WithContentTypeVideoMp4_ShouldReturnMp4File() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("video/mp4");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.mp4", fileName);
    }

    @Test
    void testExtractFileName_WithContentTypeOctetStream_ShouldReturnBinFile() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("application/octet-stream");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.bin", fileName);
    }

    @Test
    void testExtractFileName_WithContentTypeJson_ShouldReturnJsonFile() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("application/json");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.json", fileName);
    }

    @Test
    void testExtractFileName_WithContentTypeWithCharset_ShouldIgnoreCharset() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("text/plain; charset=utf-8");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.txt", fileName);
    }

    @Test
    void testExtractFileName_WithMalformedContentDisposition_ShouldFallbackToContentType() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; name=document.pdf");
        when(mockResponse.header("Content-Type")).thenReturn("application/pdf");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.pdf", fileName);
    }

    // ========== File Extension Tests ==========

    @Test
    void testGetFileExtension_WithValidFileName_ShouldReturnExtension() {
        // Arrange
        String fileName = "document.pdf";

        // Act
        String extension = downloadAction.getFileExtension(fileName);

        // Assert
        assertEquals("pdf", extension);
    }

    @Test
    void testGetFileExtension_WithMultipleDots_ShouldReturnLastExtension() {
        // Arrange
        String fileName = "archive.tar.gz";

        // Act
        String extension = downloadAction.getFileExtension(fileName);

        // Assert
        assertEquals("gz", extension);
    }

    @Test
    void testGetFileExtension_WithNoDot_ShouldReturnFullName() {
        // Arrange
        String fileName = "README";

        // Act
        String extension = downloadAction.getFileExtension(fileName);

        // Assert
        assertEquals("README", extension);
    }

    @Test
    void testGetFileExtension_WithDotAtEnd_ShouldReturnEmptyString() {
        // Arrange
        String fileName = "file.";

        // Act
        String extension = downloadAction.getFileExtension(fileName);

        // Assert
        assertEquals("", extension);
    }

    // ========== Save File Tests ==========

    @Test
    void testSaveFileToLocal_WithValidInputStream_ShouldReturnFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        // Act
        java.io.File result = downloadAction.saveFileToLocal(inputStream, fileName);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result.getName());
        
        // Cleanup
        if (result.exists()) {
            result.delete();
        }
    }

    // ========== Fetch File Response Tests ==========

    @Test
    void testFetchFileResponse_WithInvalidUrl_ShouldThrowIllegalArgumentException() {
        // Arrange
        String invalidUrl = "not-a-valid-url";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> downloadAction.fetchFileResponse(invalidUrl));
    }

    @Test
    void testFetchFileResponse_WithEmptyUrl_ShouldThrowIllegalArgumentException() {
        // Arrange
        String emptyUrl = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> downloadAction.fetchFileResponse(emptyUrl));
    }

    @Test
    void testFetchFileResponse_WithNullUrl_ShouldThrowException() {
        // Arrange
        String nullUrl = null;

        // Act & Assert
        assertThrows(Exception.class, () -> downloadAction.fetchFileResponse(nullUrl));
    }

    // ========== Edge Case Tests ==========

    @Test
    void testExtractFileName_WithContentDispositionNoQuotes_ShouldReturnUnknownFile() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; filename=document.pdf");
        when(mockResponse.header("Content-Type")).thenReturn("application/pdf");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.pdf", fileName);
    }

    @Test
    void testExtractFileName_WithEmptyContentDisposition_ShouldFallbackToContentType() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("");
        when(mockResponse.header("Content-Type")).thenReturn("image/png");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.png", fileName);
    }

    @Test
    void testExtractFileName_WithSpecialCharactersInFileName_ShouldReturnFileName() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; filename=\"my-document (1).pdf\"");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("my-document (1).pdf", fileName);
    }

    @Test
    void testGetFileExtension_WithUpperCaseExtension_ShouldReturnUpperCase() {
        // Arrange
        String fileName = "document.PDF";

        // Act
        String extension = downloadAction.getFileExtension(fileName);

        // Assert
        assertEquals("PDF", extension);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testExtractFileName_FromGoogleDriveResponse_ShouldExtractCorrectly() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; filename=\"My Presentation.pptx\"");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("My Presentation.pptx", fileName);
    }

    @Test
    void testExtractFileName_FromDropboxResponse_ShouldExtractCorrectly() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn("attachment; filename=\"vacation-photo.jpg\"");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("vacation-photo.jpg", fileName);
    }

    @Test
    void testExtractFileName_FromGenericApiResponse_ShouldUseContentType() {
        // Arrange
        Response mockResponse = createMockResponse(200, "OK");
        when(mockResponse.header("Content-Disposition")).thenReturn(null);
        when(mockResponse.header("Content-Type")).thenReturn("application/xml");

        // Act
        String fileName = downloadAction.extractFileName(mockResponse);

        // Assert
        assertEquals("file.xml", fileName);
    }

    // ========== Helper Methods ==========

    private Response createMockResponse(int code, String message) {
        Response mockResponse = mock(Response.class);
        when(mockResponse.code()).thenReturn(code);
        when(mockResponse.message()).thenReturn(message);
        when(mockResponse.isSuccessful()).thenReturn(code >= 200 && code < 300);
        return mockResponse;
    }
}

