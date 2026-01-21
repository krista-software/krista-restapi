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
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AccessToken;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.auth.AuthPayload;
import app.krista.extensions.development.api.rest.auth.OAuthClient;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.AuthUtils;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import com.google.gson.reflect.TypeToken;
import com.kristasoft.common.jaxrs.impl.BasicAuthorizationHeader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.development.api.rest.impl.Constants.*;

/**
 * Builder class for constructing HTTP requests with authentication and custom
 * headers.
 * <p>
 * This class handles the creation of OkHttp {@link Request} objects with
 * support for:
 * <ul>
 * <li>Multiple authentication types (Basic, OAuth 2.0, Token-based)</li>
 * <li>Custom headers and query parameters</li>
 * <li>JSON and multipart/form-data payloads</li>
 * <li>File uploads with automatic conversion from Krista files</li>
 * </ul>
 * Authentication credentials are retrieved from the {@link AttributeStore} and
 * applied
 * automatically to each request based on the configured authentication type.
 * </p>
 *
 * @see RestApiAttributes
 * @see OAuthClient
 * @see KristaMediaClient
 */
public class HTTPRequest {

    private final Logger LOGGER = LoggerFactory.getLogger(HTTPRequest.class);
    public static final String BEARER = "Bearer ";
    private static final MediaType STREAM_MIME_TYPE = MediaType.parse("application/octet-stream");
    private static HTTPRequest instance;
    private final RestApiAttributes restApiAttributes;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private KristaMediaClient kristaMediaClient;

    /**
     * Constructs a new HTTPRequest instance with the specified dependencies.
     *
     * @param restApiAttributes the REST API configuration attributes
     * @param refreshTokenStore the store for OAuth refresh tokens
     * @param invoker           the Krista invoker for routing information
     * @param attributeStore    the store for authentication attributes
     * @param kristaMediaClient the client for file conversion operations
     */
    public HTTPRequest(RestApiAttributes restApiAttributes, RefreshTokenStore refreshTokenStore, Invoker invoker,
            AttributeStore attributeStore, KristaMediaClient kristaMediaClient) {
        this.restApiAttributes = restApiAttributes;
        this.refreshTokenStore = refreshTokenStore;
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.kristaMediaClient = kristaMediaClient;
    }

    /**
     * Creates a new HTTPRequest instance.
     * <p>
     * Factory method for creating HTTPRequest instances with all required
     * dependencies.
     * </p>
     *
     * @param restApiAttributes the REST API configuration attributes
     * @param refreshTokenStore the store for OAuth refresh tokens
     * @param invoker           the Krista invoker for routing information
     * @param attributeStore    the store for authentication attributes
     * @param kristaMediaClient the client for file conversion operations
     * @return a new HTTPRequest instance
     */
    public static HTTPRequest getInstance(RestApiAttributes restApiAttributes, RefreshTokenStore refreshTokenStore,
            Invoker invoker, AttributeStore attributeStore, KristaMediaClient kristaMediaClient) {
        return new HTTPRequest(restApiAttributes, refreshTokenStore, invoker, attributeStore, kristaMediaClient);
    }

    /**
     * Creates an HTTP request with authentication, headers, query parameters, and
     * payload.
     * <p>
     * This method constructs a complete OkHttp {@link Request} object by:
     * <ul>
     * <li>Appending query parameters to the URL</li>
     * <li>Validating the URL and HTTP method</li>
     * <li>Adding authentication headers based on configured auth type</li>
     * <li>Adding custom headers</li>
     * <li>Creating the request body from the payload</li>
     * </ul>
     * </p>
     *
     * @param url             the target URL for the request
     * @param methodType      the HTTP method (GET, POST, PUT, PATCH, DELETE)
     * @param headers         a list of custom HTTP header key-value pairs
     * @param queryParameters a list of query parameter key-value pairs
     * @param payload         a map containing the request body data
     * @return a fully constructed OkHttp Request object
     * @throws IOException if an error occurs during authentication or request
     *                     construction
     */
    public Request create(String url, String methodType, List<Map<String, Object>> headers,
            List<Map<String, Object>> queryParameters, Map<String, Object> payload) throws IOException {

        String urlWithQueryParams = getUrl(url, queryParameters);
        LOGGER.info("URL with query params: {}", urlWithQueryParams);

        if (!isUrlValid(urlWithQueryParams)) {
            LOGGER.error("Invalid URL format: {}", urlWithQueryParams);
            throw new IllegalArgumentException(ErrorMessages.invalidUrl(urlWithQueryParams));
        }
        if (!isMethodTypeValid(methodType)) {
            LOGGER.error("Invalid HTTP method: {}", methodType);
            throw new IllegalArgumentException(ErrorMessages.invalidHttpMethod(methodType));
        }

        RequestBody body = getBody(payload, methodType);
        LOGGER.info("Request body created: {}", body != null ? body.getClass().getSimpleName() : "null");

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlWithQueryParams)
                .method(methodType, body);

        createAuthHeader(requestBuilder);
        addCustomHeaders(headers, requestBuilder);

        return requestBuilder.build();
    }

    /**
     * Adds authentication headers to the request based on the configured
     * authentication type.
     * <p>
     * Supports three authentication types:
     * <ul>
     * <li><b>Basic Authentication:</b> Adds "Authorization: Basic {credentials}"
     * header</li>
     * <li><b>OAuth 2.0:</b> Refreshes access token and adds "Authorization: Bearer
     * {token}" header</li>
     * <li><b>Token-based:</b> Adds custom token header (e.g., "Authorization:
     * Bearer {token}" or custom header)</li>
     * </ul>
     * Authentication credentials are retrieved from the {@link AttributeStore}.
     * </p>
     *
     * @param requestBuilder the OkHttp request builder to add authentication
     *                       headers to
     * @throws IOException if an error occurs during OAuth token refresh
     */
    private void createAuthHeader(Request.Builder requestBuilder) throws IOException {

        String key = AuthUtils.getAuthTypeKey(attributeStore);
        if (key == null) {
            LOGGER.warn("No authentication type configured. Request will be sent without authentication.");
            return; // Skip authentication if not configured
        } else {
            RestApiAttributes restApiAttributesForHeader = AuthUtils.getRestApiAttributes(attributeStore, key);
            if (restApiAttributesForHeader.getAuthType().equals(AuthPayload.AuthType.Basic)) {
                BasicAuthorizationHeader basicAuthorizationHeader = BasicAuthorizationHeader
                        .create(restApiAttributesForHeader.getUserName(), restApiAttributesForHeader.getPassWord());
                String authorization = basicAuthorizationHeader.getAuthorization();
                requestBuilder.addHeader("Authorization", "Basic " + authorization);
            } else if (restApiAttributesForHeader.getAuthType().equals(AuthPayload.AuthType.OAuth)) {
                String refreshToken = refreshTokenStore.get(restApiAttributes.getClientId());
                String routingUrl = invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME,
                        RoutingInfo.Type.APPLIANCE) + "/" + "rest/callback";
                OAuthClient oAuthClient = new OAuthClient(restApiAttributesForHeader.getClientId(),
                        restApiAttributesForHeader.getClientSecret(),
                        restApiAttributesForHeader.getAuthUrl(), restApiAttributesForHeader.getAccessTokenUrl(),
                        restApiAttributesForHeader.getScope(), routingUrl);
                try {
                    AccessToken accessToken = oAuthClient.refreshAccessToken(refreshToken);
                    requestBuilder.addHeader("Authorization", BEARER + accessToken.getAccessToken());
                } catch (Exception cause) {
                    requestBuilder.addHeader("Authorization", BEARER + refreshToken);
                }
            } else {
                if (restApiAttributesForHeader.getTokenType().equalsIgnoreCase("Bearer")) {
                    requestBuilder.addHeader("Authorization", BEARER + restApiAttributesForHeader.getToken());
                } else {
                    requestBuilder.addHeader(restApiAttributesForHeader.getTokenType(),
                            restApiAttributesForHeader.getToken());
                }
            }
        }
    }

    /**
     * Constructs the full URL with query parameters appended.
     * Uses proper URL encoding to prevent injection attacks and handle special characters.
     *
     * @param url             the base URL (may already contain query parameters)
     * @param queryParameters a list of query parameter key-value pairs to append
     * @return the URL with properly encoded query parameters
     */
    private String getUrl(String url, List<Map<String, Object>> queryParameters) {
        // Step 1: Handle null URL
        if (url == null || url.isEmpty()) {
            return url;
        }
        String cleanUrl = url.replace(" ", "%20");
        if (queryParameters == null || queryParameters.isEmpty()) {
            return cleanUrl;
        }

        // Step 2: Append additional query parameters with proper encoding
        StringBuilder queryParamBuilder = new StringBuilder();

        if (cleanUrl.contains("?")) {
            queryParamBuilder.append(cleanUrl).append("&");
        } else {
            queryParamBuilder.append(cleanUrl).append("?");
        }

        for (Map<String, Object> queryParam : queryParameters) {
            Object key = queryParam.get("key");
            Object value = queryParam.get("value");
            if (key != null && value != null) {
                String encodedValue = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);
                queryParamBuilder.append(key).append("=").append(encodedValue).append("&");
            }
        }

        if (queryParamBuilder.charAt(queryParamBuilder.length() - 1) == '&') {
            queryParamBuilder.deleteCharAt(queryParamBuilder.length() - 1);
        }

        return queryParamBuilder.toString();
    }

    /**
     * Adds custom headers to the HTTP request.
     * <p>
     * Each header is expected to have "key" and "value" fields in the map.
     * Headers with null keys or values are skipped.
     * </p>
     *
     * @param headers        a list of header key-value pairs
     * @param requestBuilder the OkHttp request builder to add headers to
     */
    private void addCustomHeaders(List<Map<String, Object>> headers, Request.Builder requestBuilder) {
        if (headers != null && requestBuilder != null) {
            int headerCount = 0;
            for (Map<String, Object> headerMap : headers) {
                Object key = headerMap.get("key");
                Object value = headerMap.get("value");
                LOGGER.info("Processing header #{}: key='{}', value='{}'", ++headerCount, key, value);

                if (key != null && value != null) {
                    String headerKey = key.toString();
                    String headerValue = value.toString();
                    requestBuilder.addHeader(headerKey, headerValue);
                    LOGGER.info("Added header: {} = {}", headerKey, headerValue);
                } else {
                    LOGGER.warn("Skipping header with null key or value: key={}, value={}", key, value);
                }
            }
            LOGGER.info("Total headers added: {}", headerCount);
        } else {
            LOGGER.info("Headers is null or requestBuilder is null - no headers added");
        }
    }

    /**
     * Creates a multipart/form-data request body for file uploads.
     * If file is null, creates multipart form data with only the form fields.
     *
     * @param file    the file to be uploaded (can be null)
     * @param payload the request payload map containing other form fields
     * @return a RequestBody containing the file as multipart/form-data
     */
    private RequestBody createRequestBodyFromFile(java.io.File file, Map<String, Object> payload) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // Add the file (if present)
        if (file != null) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(STREAM_MIME_TYPE, file));
            LOGGER.info("Added file field: name='file', filename='{}'", file.getName());
        } else {
            LOGGER.info("Skipping file field (file is null)");
        }

        // Check if payload has a "value" field containing JSON
        Object valueObj = payload.get("value");
        if (valueObj != null) {
            LOGGER.info("Found 'value' field in payload: {}", valueObj);
            try {
                // Try to parse the value as JSON to extract nested fields
                String valueStr = valueObj.toString();
                LOGGER.info("Attempting to parse value as JSON: {}", valueStr);

                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> parsedFields = GSON_JSON_MAPPER.fromString(valueStr, type);

                if (parsedFields != null && !parsedFields.isEmpty()) {
                    LOGGER.info("Successfully parsed JSON. Found {} fields", parsedFields.size());
                    // Add each parsed field as a form field
                    int fieldCount = 0;
                    for (Map.Entry<String, Object> field : parsedFields.entrySet()) {
                        String fieldKey = field.getKey();
                        String fieldValue = field.getValue() != null ? field.getValue().toString() : "";
                        LOGGER.info("Adding parsed form field #{}: key='{}', value='{}'",
                            ++fieldCount, fieldKey, fieldValue);
                        builder.addFormDataPart(fieldKey, fieldValue);
                    }
                } else {
                    LOGGER.warn("Parsed JSON is null or empty");
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to parse 'value' as JSON: {}. Will add as-is.", e.getMessage());
                // If parsing fails, add the value field as-is
                builder.addFormDataPart("value", valueObj.toString());
            }
        }
        return builder.build();
    }

    /**
     * Creates the request body based on the payload and HTTP method.
     * <p>
     * GET requests return null (no body).
     * If the payload contains files, creates a multipart/form-data body.
     * If payloadType is explicitly set to "multipart/form-data", creates multipart body.
     * Otherwise, creates a JSON request body from the "value" field.
     * </p>
     *
     * @param payload    a map containing either "file" (for file uploads), "value" (for JSON data),
     *                   or "payloadType" (to explicitly specify the content type)
     * @param methodType the HTTP method type
     * @return the request body, or null for GET requests
     */
    private RequestBody getBody(Map<String, Object> payload, String methodType) {
        if (HTTP_GET.equalsIgnoreCase(methodType)) {
            LOGGER.info("GET request - returning null body");
            return null;
        }

        // Check for explicit payloadType to control request body format
        String payloadType = (String) payload.get("payloadType");
        LOGGER.info("Payload type: {}", payloadType);

        if (hasFilePayload(payload)) {
            List<java.io.File> ticketFiles = downloadFilesFromPayload(payload);
            if (!ticketFiles.isEmpty()) {
                LOGGER.info("Files downloaded: {}", ticketFiles.size());
                return createRequestBodyFromFile(ticketFiles.get(0), payload);
            } else {
                LOGGER.warn("File payload detected but no files downloaded - falling back to multipart without file");
                return createRequestBodyFromFile(null, payload);
            }
        }

        // Only create multipart form data if explicitly requested via payloadType
        if ("multipart/form-data".equalsIgnoreCase(payloadType)) {
            LOGGER.info("PayloadType is 'multipart/form-data' - creating multipart form data without file");
            return createRequestBodyFromFile(null, payload);
        }

        LOGGER.info("Creating JSON request body from 'value' field");
        return createJsonRequestBody(payload.get("value"));
    }

    /**
     * Checks if the payload contains file attachments.
     *
     * @param payload the request payload map
     * @return true if the payload contains a "file" field, false otherwise
     */
    private boolean hasFilePayload(Map<String, Object> payload) {
        return payload.get("file") != null;
    }

    /**
     * Downloads Krista files from the payload and converts them to Java file
     * objects.
     * <p>
     * Extracts Krista file objects from the payload and converts them to
     * {@link java.io.File}
     * objects using {@link KristaMediaClient}. I/O errors during conversion are
     * silently ignored.
     * </p>
     *
     * @param payload the request payload containing Krista file objects
     * @return a list of Java file objects
     */
    private List<java.io.File> downloadFilesFromPayload(Map<String, Object> payload) {
        List<java.io.File> ticketFiles = new ArrayList<>();
        List<app.krista.model.base.File> kristaFiles = getKristaFilesFromPayload(payload);

        for (app.krista.model.base.File currentFile : kristaFiles) {
            try {
                ticketFiles.add(kristaMediaClient.toJavaFile(currentFile));
            } catch (IOException ignored) {
            }
        }

        return ticketFiles;
    }

    /**
     * Extracts and deserializes Krista file objects from the payload.
     *
     * @param payload the request payload containing file data
     * @return a list of Krista file objects
     */
    private List<app.krista.model.base.File> getKristaFilesFromPayload(Map<String, Object> payload) {
        Type fileListType = new TypeToken<ArrayList<app.krista.model.base.File>>() {
        }.getType();
        @SuppressWarnings("unchecked")
        List<app.krista.model.base.File> files = (List<app.krista.model.base.File>) payload.get("file");
        String serializedFiles = GSON_JSON_MAPPER.toString(files);
        return GSON_JSON_MAPPER.fromString(serializedFiles, fileListType);
    }

    /**
     * Creates a JSON request body from the payload object.
     * <p>
     * The payload must be a String containing JSON data, or null for an empty body.
     * </p>
     *
     * @param payloadObj the payload object (must be a String or null)
     * @return a RequestBody with application/json content type
     * @throws IllegalArgumentException if the payload is not a String or null
     */
    private RequestBody createJsonRequestBody(Object payloadObj) {
        if (payloadObj instanceof String) {
            return RequestBody.create((String) payloadObj, JSON_MIME_TYPE);
        } else if (payloadObj == null) {
            return RequestBody.create("", JSON_MIME_TYPE);
        } else {
            throw new IllegalArgumentException(ErrorMessages.invalidPayloadType(payloadObj.getClass().getName()));
        }
    }

    /**
     * Validates the URL format.
     *
     * @param url the URL to validate
     * @return true if the URL is valid, false otherwise
     */
    private boolean isUrlValid(String url) {
        if (url == null || url.isEmpty() || url.isBlank())
            return false;
        try {
            java.net.URI.create(url).toURL();
            return true;
        } catch (Exception cause) {
            LOGGER.error("Invalid URL format: {}", url);
            return false;
        }
    }

    /**
     * Validates the HTTP method type.
     *
     * @param methodType the HTTP method to validate
     * @return true if the method is GET, POST, PUT, PATCH, or DELETE
     *         (case-insensitive), false otherwise
     */
    private boolean isMethodTypeValid(String methodType) {
        return HTTP_GET.equalsIgnoreCase(methodType) ||
                (HTTP_PUT.equalsIgnoreCase(methodType) ||
                        (HTTP_PATCH.equalsIgnoreCase(methodType) ||
                                (HTTP_POST.equalsIgnoreCase(methodType) ||
                                        (HTTP_DELETE.equalsIgnoreCase(methodType)))));
    }

}
