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
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.ApiExceptionHandler;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.extensions.development.api.rest.util.HttpResponseException;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import app.krista.model.base.FreeForm;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.util.*;

public class ActionableImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionableImpl.class);
    private static final String STATUS_AND_MESSAGE = "Status and Message";
    private static final String RESPONSE = "Response";
    private static final String RESPONSE_INFO = "Response Info";
    private static final String ERROR = "Error";
    private static final int MAX_ALLOWED_SIZE = 4 * 1024 * 1024;
    public static final String RESPONSE_FILE = "Response File";

    // HTTP Client Configuration Constants
    private static final int HTTP_TIMEOUT_SECONDS = 300;
    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000;

    private final RestApiAttributes restApiAttributes;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;
    private final OkHttpClient httpClient;

    @Inject
    public ActionableImpl(RestApiAttributes restApiAttributes, RefreshTokenStore refreshTokenStore, Invoker invoker,
                          AttributeStore attributeStore, KristaMediaClient kristaMediaClient) {
        this.restApiAttributes = restApiAttributes;
        this.refreshTokenStore = refreshTokenStore;
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.kristaMediaClient = kristaMediaClient;

        // Initialize reusable HTTP client with configured timeouts
        this.httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(HTTP_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        LOGGER.debug("ActionableImpl initialized with HTTP timeout: {} seconds", HTTP_TIMEOUT_SECONDS);
    }

    public RestApiAttributes getRestApiAttributes() {
        return restApiAttributes;
    }

    public Map<String, Object> executeApiRequest(String url, String methodType,
                                                 List<Map<String, Object>> queryParameters,
                                                 List<Map<String, Object>> headers, Map<String, Object> payload) throws IOException {
        int retryCount = 0; // Counter for the number of attempts

        while (retryCount < MAX_RETRIES) {
            Response response = null; // Declare response outside of try block
            try {
                response = getResponse(url, methodType, queryParameters, headers, payload);
                if (response.isSuccessful()) {
                    FreeForm freeForm = new FreeForm();
                    freeForm.put(STATUS_AND_MESSAGE, "Text", response.code() + " " + response.message());

                    if (response.body() != null) {
                        ArrayList<LinkedHashMap<Object, Object>> responseBodyJsonToMap = convertResponseToMappedList(
                                response);
                        rearrangeResponseListByMaxSize(responseBodyJsonToMap);
                        return createApiResponseDetails(responseBodyJsonToMap, freeForm);
                    } else {
                        String errorDetails = ErrorMessages.emptyResponseBody(response.code(), response.message());
                        throw new IOException(errorDetails);
                    }
                } else {
                    String responseBody = response.body() != null ? response.body().string() : "No response body";
                    String errorDetails = ErrorMessages.apiRequestFailed(response.code(), response.message(),
                            responseBody);
                    throw new HttpResponseException(response.code(), response.message(), responseBody, errorDetails);
                }

            } catch (IOException | IllegalArgumentException cause) {
                retryCount++;
                if (retryCount >= MAX_RETRIES) {
                    return createApiErrorResponse(url, cause);
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt(); // Restore interrupted state
                    throw new IOException("Request retry was interrupted. Please try again.", interruptedException);
                }
            } finally {
                if (response != null && response.body() != null) {
                    response.body().close();
                }
            }
        }

        // Fallback return if loop exits without returning (should not happen in normal flow)
        return createApiErrorResponse(url, new IOException("Maximum retry attempts exceeded"));
    }


    public Map<String, Object> getApiResponseFile(String url, String methodType,
            List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers) {
        return getApiResponseFile(url, methodType, queryParameters, headers, Map.of());
    }

    public Map<String, Object> getApiResponseFile(String url, String methodType,
            List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers, Map<String, Object> payload) {
        File tempFile = null;
        try {
            LOGGER.info("Payload keys: {}", payload != null ? payload.keySet() : "null");
            Response response = getResponse(url, methodType, queryParameters, headers, payload);

            LOGGER.info("Response Code: {}", response.code());
            LOGGER.info("Response Message: {}", response.message());

            // Log response body for debugging (only if error)
            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                LOGGER.error("Error Response Body: {}", responseBody);
                throw new IOException("API request failed with status " + response.code() + ": " + responseBody);
            }

            FreeForm freeForm = new FreeForm();
            freeForm.put(STATUS_AND_MESSAGE, "Text", response.code() + " " + response.message());
            tempFile = convertResponseToFile(response);
            LOGGER.info("File converted successfully: {}", tempFile.getName());

            // Upload to Krista media server
            Map<String, Object> responseDetails = new HashMap<>();
            responseDetails.put(RESPONSE_FILE, kristaMediaClient.toKristaFile(tempFile));
            responseDetails.put(RESPONSE_INFO, freeForm);

            // Clean up temp file after successful upload
            cleanupTempFile(tempFile);

            return responseDetails;

        } catch (IOException cause) {
            LOGGER.error("ActionableImpl.getApiResponseFile - ERROR", cause);
            // Clean up temp file on error
            if (tempFile != null) {
                cleanupTempFile(tempFile);
            }
            return createApiErrorResponse(url, cause);
        }
    }

    /**
     * Safely deletes a temporary file and logs the result.
     *
     * @param tempFile the temporary file to delete
     */
    private void cleanupTempFile(File tempFile) {
        if (tempFile != null && tempFile.exists()) {
            boolean deleted = tempFile.delete();
            if (deleted) {
                LOGGER.debug("Successfully cleaned up temporary file: {}", tempFile.getAbsolutePath());
            } else {
                LOGGER.warn("Failed to delete temporary file: {}. It will be cleaned up on JVM exit.",
                           tempFile.getAbsolutePath());
            }
        }
    }

    private File convertResponseToFile(Response response) throws IOException {
        if (response == null) {
            throw new IllegalArgumentException("Response must not be null");
        }

        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new IOException(ErrorMessages.responseBodyNull());
        }

        String contentType = response.header("Content-Type");
        String contentDisposition = response.header("Content-Disposition");

        String fileName = extractFileNameFromHeaders(contentDisposition, contentType);

        // Sanitize filename to prevent path traversal
        String sanitizedFileName = sanitizeFileName(fileName);

        // Create temp file with original filename in temp directory
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File tempFile = new File(tempDir, sanitizedFileName);

        // Mark for deletion on JVM exit as safety net
        tempFile.deleteOnExit();

        LOGGER.debug("Created temporary file: {} (will be cleaned up after upload)", tempFile.getAbsolutePath());

        try (BufferedSink sink = Okio.buffer(Okio.sink(tempFile))) {
            sink.writeAll(responseBody.source());
            LOGGER.debug("Written {} bytes to temporary file", tempFile.length());
        } catch (IOException cause) {
            // Clean up temp file on failure
            if (tempFile.exists()) {
                boolean deleted = tempFile.delete();
                LOGGER.debug("Cleaned up temp file on error: {}", deleted);
            }
            throw new RuntimeException(ErrorMessages.failedToWriteFile(), cause);
        }
        return tempFile;
    }

    /**
     * Sanitizes a filename to prevent path traversal attacks and ensure valid filenames.
     *
     * @param fileName the original filename
     * @return sanitized filename safe for file system operations
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "download.bin";
        }

        // Remove path separators and parent directory references
        String sanitized = fileName.replaceAll("[/\\\\]", "_")
                                  .replaceAll("\\.\\.", "_")
                                  .trim();

        // Ensure filename is not empty after sanitization
        if (sanitized.isEmpty()) {
            return "download.bin";
        }

        // Limit filename length to prevent filesystem issues
        if (sanitized.length() > 255) {
            String extension = "";
            int dotIndex = sanitized.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = sanitized.substring(dotIndex);
            }
            sanitized = sanitized.substring(0, Math.min(200, sanitized.length())) + extension;
        }

        return sanitized;
    }

    private String extractFileNameFromHeaders(String contentDisposition, String contentType) {
        // Try to extract filename from Content-Disposition header
        if (contentDisposition != null) {
            LOGGER.info("Parsing Content-Disposition: {}", contentDisposition);
            // Look for filename="..." or filename*=UTF-8''...
            if (contentDisposition.contains("filename=")) {
                String[] parts = contentDisposition.split("filename=");
                if (parts.length > 1) {
                    String fileName = parts[1].trim();
                    // Remove quotes if present
                    fileName = fileName.replaceAll("^\"|\"$", "");
                    // Remove anything after semicolon
                    fileName = fileName.split(";")[0].trim();
                    LOGGER.info("Extracted filename from Content-Disposition: {}", fileName);
                    return fileName;
                }
            }
        }

        // Fallback to Content-Type based extension
        String extension = getFileExtension(contentType);
        String defaultFileName = "api_response" + extension;
        LOGGER.info("Using default filename: {}", defaultFileName);
        return defaultFileName;
    }

    private static final Map<String, String> CONTENT_TYPE_TO_EXTENSION = createContentTypeMap();

    private static Map<String, String> createContentTypeMap() {
        Map<String, String> map = new HashMap<>();
        map.put("application/json", ".json");
        map.put("application/xml", ".xml");
        map.put("text/xml", ".xml");
        map.put("text/html", ".html");
        map.put("text/plain", ".txt");
        map.put("text/csv", ".csv");
        map.put("application/pdf", ".pdf");
        map.put("image/jpeg", ".jpeg");
        map.put("image/png", ".png");
        map.put("image/gif", ".gif");
        map.put("image/svg+xml", ".svg");
        map.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");
        map.put("application/vnd.ms-excel", ".xls");
        map.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        map.put("application/msword", ".doc");
        map.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
        map.put("application/vnd.ms-powerpoint", ".ppt");
        map.put("application/zip", ".zip");
        // Note: application/octet-stream is intentionally NOT mapped here
        // It's a generic binary type and should fall through to .bin default
        return Collections.unmodifiableMap(map);
    }

    private String getFileExtension(String contentType) {
        if (contentType == null) {
            LOGGER.warn("Content-Type is null, defaulting to .bin");
            return ".bin";
        }

        String baseContentType = contentType.split(";")[0].trim();
        LOGGER.info("Base Content-Type: {}", baseContentType);

        String extension = CONTENT_TYPE_TO_EXTENSION.get(baseContentType);
        if (extension != null) {
            LOGGER.debug("Mapped Content-Type '{}' to extension '{}'", baseContentType, extension);
            return extension;
        }

        // For unknown types including application/octet-stream, use .bin
        LOGGER.warn("Unknown or generic Content-Type: '{}', defaulting to .bin. " +
                "Consider extracting extension from Content-Disposition header if available.", baseContentType);
        return ".bin";
    }

    public Map<String, Object> executeCustomPaginatedApiRequest(String url, String methodType,
            List<Map<String, Object>> queryParameters,
            List<Map<String, Object>> headers, Double pageSize, Double pageIndex) {
        try {
            Response response = getResponse(url, methodType, queryParameters, headers, Map.of());
            FreeForm freeForm = new FreeForm();
            freeForm.put(STATUS_AND_MESSAGE, "Text", response.code() + " " + response.message());
            Map<String, Object> paginatedResponseMap = new HashMap<>(
                    getPaginatedResponseMap(response, pageSize, pageIndex));
            @SuppressWarnings("unchecked")
            ArrayList<LinkedHashMap<Object, Object>> responseBodyJsonToMap = (ArrayList<LinkedHashMap<Object, Object>>) paginatedResponseMap
                    .get(RESPONSE);
            rearrangeResponseListByMaxSize(responseBodyJsonToMap);
            paginatedResponseMap.put(RESPONSE, responseBodyJsonToMap);
            paginatedResponseMap.put(RESPONSE_INFO, freeForm);
            return paginatedResponseMap;
        } catch (IOException cause) {
            return createApiErrorResponse(url, cause);
        }
    }

    /**
     * Executes an HTTP request using the configured OkHttpClient.
     * <p>
     * This method reuses a single OkHttpClient instance for all requests to enable
     * connection pooling and improve performance.
     * </p>
     *
     * @param url the target URL
     * @param methodType the HTTP method (GET, POST, PUT, DELETE, etc.)
     * @param queryParameters query parameters to append to the URL
     * @param headers HTTP headers to include in the request
     * @param payload request body payload
     * @return the HTTP response
     * @throws IOException if the request fails
     */
    @NotNull
    public Response getResponse(String url, String methodType, List<Map<String, Object>> queryParameters,
            List<Map<String, Object>> headers, Map<String, Object> payload) throws IOException {
        Request request = HTTPRequest
                .getInstance(restApiAttributes, refreshTokenStore, invoker, attributeStore, kristaMediaClient)
                .create(url, methodType, headers, queryParameters, payload);
        LOGGER.info("Request Details: \n {}", request);
        // Use the reusable httpClient instance for connection pooling and better performance
        return httpClient.newCall(request).execute();
    }

    public static ArrayList<LinkedHashMap<Object, Object>> extractPaginatedResults(
            List<LinkedHashMap<Object, Object>> allResults, Double pageSize, Double pageIndex) {
        int totalResults = allResults.size();
        int startIndex = (int) (pageSize * pageIndex);
        int endIndex = (int) Math.min(startIndex + pageSize, totalResults);

        if (startIndex >= totalResults || startIndex < 0) {
            return new ArrayList<>();
        }

        return new ArrayList<>(allResults.subList(startIndex, endIndex));
    }

    public Response testConnection() throws IOException {
        return getResponse(restApiAttributes.getApiUrl(), Constants.HTTP_GET, null, null, null);
    }

    private static Map<String, Object> getPaginatedResponseMap(Response response, Double pageSize, Double pageIndex) {
        byte[] responseBytes = extractResponseBytes(response);
        ArrayList<LinkedHashMap<Object, Object>> resultList = parseJsonResponseToList(responseBytes);
        ArrayList<LinkedHashMap<Object, Object>> paginatedResults = extractPaginatedResults(resultList, pageSize,
                pageIndex);
        int resultSize = paginatedResults.size();
        int limit = 500;

        if (resultSize > limit) {
            String errorMessage = ErrorMessages.paginatedResultsTooLarge(resultSize, limit);
            LOGGER.warn("Paginated results exceed limit: {} > {}", resultSize, limit);
            LinkedHashMap<Object, Object> errorMsg = new LinkedHashMap<>();
            errorMsg.put("Error", errorMessage);
            paginatedResults.clear();
            paginatedResults.add(errorMsg);
        }

        int totalRecords = resultList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        return Map.of(
                "Response", paginatedResults,
                "Total Records", totalRecords,
                "Total Pages", totalPages,
                "Page Size", pageSize,
                "Page Index", pageIndex);
    }

    private static ArrayList<LinkedHashMap<Object, Object>> parseJsonResponseToList(byte[] responseBytes) {
        ArrayList<LinkedHashMap<Object, Object>> resultList = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(new ByteArrayInputStream(responseBytes))) {
            ObjectMapper mapper = new ObjectMapper();

            // Check the first token
            JsonToken token = parser.nextToken();
            if (token == JsonToken.START_OBJECT) {
                Map<String, Object> singleObject = mapper.readValue(parser, new TypeReference<Map<String, Object>>() {
                });
                LinkedHashMap<Object, Object> result = new LinkedHashMap<>(singleObject);
                renameReservedKeys(result);
                resultList.add(result);
            } else if (token == JsonToken.START_ARRAY) {
                // Iterate over each token
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    LinkedHashMap<Object, Object> map = mapper.readValue(parser, new TypeReference<LinkedHashMap<Object, Object>>() {
                    });
                    renameReservedKeys(map);
                    resultList.add(map);
                }
            } else {
                throw new IOException(ErrorMessages.invalidJsonPayload());
            }
        } catch (IOException cause) {
            LOGGER.warn("Failed to parse JSON response: {}", cause.getMessage());
            return new ArrayList<>();
        }
        return resultList;
    }

    /**
     * Recursively renames reserved keys in the response map that conflict with Krista's type system.
     * The keys "type" and "description" are renamed to "_type" and "_description" respectively
     * to avoid IllegalStateException.
     *
     * @param map the map to process (modified in place)
     */
    @SuppressWarnings("unchecked")
    private static void renameReservedKeys(Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }

        // Collect keys to rename to avoid ConcurrentModificationException
        List<Object> typeKeysToRename = new ArrayList<>();
        List<Object> descriptionKeysToRename = new ArrayList<>();

        for (Object key : map.keySet()) {
            String keyStr = String.valueOf(key);
            if ("type".equalsIgnoreCase(keyStr)) {
                typeKeysToRename.add(key);
            } else if ("description".equalsIgnoreCase(keyStr)) {
                descriptionKeysToRename.add(key);
            }
        }

        // Rename type keys
        for (Object oldKey : typeKeysToRename) {
            Object value = map.remove(oldKey);
            map.put("_type", value);
        }

        // Rename description keys
        for (Object oldKey : descriptionKeysToRename) {
            Object value = map.remove(oldKey);
            map.put("_description", value);
        }

        // Recursively process nested maps and lists
        for (Object value : map.values()) {
            if (value instanceof Map) {
                renameReservedKeys((Map<Object, Object>) value);
            } else if (value instanceof List) {
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        renameReservedKeys((Map<Object, Object>) item);
                    }
                }
            }
        }
    }

    private static ArrayList<LinkedHashMap<Object, Object>> convertResponseToMappedList(Response response) {
        assert response.body() != null;
        byte[] responseBytes = extractResponseBytes(response);
        long responseSize = responseBytes.length;

        if (responseSize > MAX_ALLOWED_SIZE) {
            String errorMessage = ErrorMessages.responseSizeTooLarge(responseSize, MAX_ALLOWED_SIZE);
            LOGGER.warn("Response size {} exceeds maximum allowed size {}", responseSize, MAX_ALLOWED_SIZE);
            LinkedHashMap<Object, Object> errorMsg = new LinkedHashMap<>();
            errorMsg.put("Error", errorMessage);
            ArrayList<LinkedHashMap<Object, Object>> errorList = new ArrayList<>();
            errorList.add(errorMsg);
            return errorList;
        }

        return parseJsonResponseToList(responseBytes);
    }

    private static byte[] extractResponseBytes(Response response) {
        byte[] responseBytes = new byte[0];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            InputStream is = response.body().byteStream();
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            responseBytes = baos.toByteArray();
        } catch (IOException cause) {
            LOGGER.error("Failed to extract response bytes: {}", cause.getMessage());
        }
        return responseBytes;
    }

    private void rearrangeResponseListByMaxSize(ArrayList<LinkedHashMap<Object, Object>> responseBodyJsonToMap) {
        if (!responseBodyJsonToMap.isEmpty()) {
            int maxSize = 0;
            int maxIndex = 0;
            for (int i = 0; i < responseBodyJsonToMap.size(); i++) {
                int currentSize = responseBodyJsonToMap.get(i).size();
                if (currentSize > maxSize) {
                    maxSize = currentSize;
                    maxIndex = i;
                }
            }
            if (maxIndex > 0) {
                Collections.swap(responseBodyJsonToMap, 0, maxIndex);
            }
        }
    }

    private Map<String, Object> createApiResponseDetails(ArrayList<LinkedHashMap<Object, Object>> responseBodyJsonToMap,
            FreeForm freeForm) {
        Map<String, Object> responseDetails = new HashMap<>();
        responseDetails.put(RESPONSE, responseBodyJsonToMap);
        responseDetails.put(RESPONSE_INFO, freeForm);
        return responseDetails;
    }

    private Map<String, Object> createApiErrorResponse(String url, Exception cause) {
        // Use centralized exception handler for consistent error handling and logging
        ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, url, "API request");

        FreeForm freeForm = new FreeForm();
        freeForm.put(STATUS_AND_MESSAGE, "Text", result.getUserMessage());
        freeForm.put(ERROR, "Text", result.getCategory().name());
        return Map.of(RESPONSE_INFO, freeForm);
    }

}
