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

import app.krista.extensions.development.api.rest.util.ApiExceptionHandler;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DownloadAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadAction.class);
    private final KristaMediaClient kristaMediaClient;

    public DownloadAction(KristaMediaClient kristaMediaClient) {
        this.kristaMediaClient = kristaMediaClient;
    }

    public app.krista.model.base.File downloadFile(String downloadUrl) {
        try {
            Response fileResponse = fetchFileResponse(downloadUrl);

            if (Objects.requireNonNull(fileResponse).body() == null || !fileResponse.isSuccessful()) {
                String errorMessage = ErrorMessages.apiRequestFailed(
                    fileResponse.code(),
                    fileResponse.message(),
                    "Failed to download file from URL: " + downloadUrl
                );
                LOGGER.error(errorMessage);
                return null;
            }
            String fileName = extractFileName(fileResponse);
            if (fileName.isEmpty()) {
                LOGGER.error("Could not determine the file name from the response. Please check the Download URL or Content-Disposition header.");
                return null;
            }
            try (InputStream inputStream = fileResponse.body().byteStream()) {
                java.io.File localFile = saveFileToLocal(inputStream, fileName);
                return kristaMediaClient.toKristaFile(localFile);
            }
        } catch (IOException cause) {
            ApiExceptionHandler.handleAndLog(cause, downloadUrl, "downloading file");
            return null;
        }
    }

    public String extractFileName(Response fileResponse) {
        String fileNameFromContentDisposition = getFileNameFromContentDisposition(fileResponse);
        if (!fileNameFromContentDisposition.isEmpty()) {
            return fileNameFromContentDisposition;
        }

        String fileNameFromContentType = getFileNameFromContentType(fileResponse);
        if (!fileNameFromContentType.isEmpty()) {
            return fileNameFromContentType;
        }

        return "unknown_file";
    }

    public java.io.File saveFileToLocal(InputStream inputStream, String fileName) throws IOException {
        return saveToLocalTempDir(inputStream, fileName);
    }


    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getFileNameFromContentDisposition(Response fileResponse) {
        String contentDisposition = fileResponse.header("Content-Disposition");
        if (contentDisposition != null) {
            Pattern pattern = Pattern.compile("filename=\"(.*)\"");
            Matcher matcher = pattern.matcher(contentDisposition);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    private String getFileNameFromContentType(Response fileResponse) {
        String contentType = fileResponse.header("Content-Type");
        if (contentType != null) {
            String baseContentType = contentType.split(";")[0];
            switch (baseContentType) {
                case "text/plain":
                    return "file.txt";
                case "audio/mpeg":
                    return "file.mp3";
                case "video/mp4":
                    return "file.mp4";
                case "application/octet-stream":
                    return "file.bin";
                default:
                    String fileType = baseContentType.substring(baseContentType.indexOf('/') + 1);
                    return "file." + fileType;
            }
        }
        return "";
    }

    private java.io.File saveToLocalTempDir(InputStream inputStream, String filePath) throws IOException {
        java.io.File localFile = new java.io.File(filePath);
        Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return localFile;
    }

    public Response fetchFileResponse(String downloadUrl) throws IOException {
        try {
            Request request = buildRequest(downloadUrl);
            return executeRequest(request);
        } catch (IllegalArgumentException cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, downloadUrl, "fetching file");
            throw new IllegalArgumentException(result.getUserMessage(), cause);
        } catch (Exception cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, downloadUrl, "fetching file");
            throw new IOException(result.getUserMessage(), cause);
        }
    }

    private Request buildRequest(String downloadUrl) {
        return new Request.Builder()
                .url(downloadUrl)
                .build();
    }

    private Response executeRequest(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response fileResponse = client.newCall(request).execute();
        if (!fileResponse.isSuccessful()) {
            LOGGER.warn("Failed to download file. HTTP Status Code: {}", fileResponse.code());
        }
        return fileResponse;
    }
}
