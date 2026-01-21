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
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;

/**
 * Service class responsible for executing HTTP POST REST API calls.
 * <p>
 * The HTTP POST method sends data to the server to create new resources.
 * The type of the request body is indicated by the Content-Type header.
 * This class provides various overloaded methods to support different
 * combinations of
 * query parameters, headers, and request payloads including file uploads.
 * </p>
 *
 * @see ActionableImpl
 * @see Constants
 */
@Service
public class WriteAction {
    private static final Logger logger = LoggerFactory.getLogger(WriteAction.class);

    private final ActionableImpl actionable;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;

    @Inject
    public WriteAction(AttributeStore attributeStore, RefreshTokenStore refreshTokenStore, Invoker invoker,
            KristaMediaClient kristaMediaClient) {
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.refreshTokenStore = refreshTokenStore;
        this.kristaMediaClient = kristaMediaClient;
        this.actionable = (!attributeStore.listValues().iterator().hasNext())
                ? new ActionableImpl(null, refreshTokenStore, invoker, attributeStore, kristaMediaClient)
                : new ActionableImpl(
                        GSON_JSON_MAPPER.fromString(attributeStore.listValues().iterator().next().toString(),
                                RestApiAttributes.class),
                        refreshTokenStore, invoker, attributeStore, kristaMediaClient); // setting RestApiAttributes
                                                                                        // null when Authentication Tab
                                                                                        // is Empty and user trying to
                                                                                        // execute API calls through
                                                                                        // authentication header
    }

    /**
     * Executes a simple HTTP POST request to the specified URL without query
     * parameters, headers, or payload.
     *
     * @param url the target URL for the POST request
     * @return a map containing the response information and data
     */
    public Map<String, Object> post(String url) {
        return post(url, Collections.emptyList());
    }

    /**
     * Executes an HTTP POST request with query parameters.
     *
     * @param url             the target URL for the POST request
     * @param queryParameters a list of query parameter key-value pairs to append to
     *                        the URL
     * @return a map containing the response information and data
     */
    public Map<String, Object> post(String url, List<Map<String, Object>> queryParameters) {
        return post(url, queryParameters, Collections.emptyList());
    }

    /**
     * Executes an HTTP POST request with query parameters and custom headers.
     *
     * @param url             the target URL for the POST request
     * @param queryParameters a list of query parameter key-value pairs to append to
     *                        the URL
     * @param headers         a list of custom HTTP header key-value pairs
     * @return a map containing the response information and data
     */
    public Map<String, Object> post(String url, List<Map<String, Object>> queryParameters,
            List<Map<String, Object>> headers) {
        return post(url, queryParameters, headers, Map.of());
    }

    /**
     * Executes an HTTP POST request with query parameters, custom headers, and a
     * request payload.
     * <p>
     * The payload can contain JSON data or file attachments. Files are
     * automatically detected
     * and sent as multipart/form-data, while other data is sent as
     * application/json.
     * </p>
     *
     * @param url             the target URL for the POST request
     * @param queryParameters a list of query parameter key-value pairs to append to
     *                        the URL
     * @param headers         a list of custom HTTP header key-value pairs
     * @param payload         a map containing the request body data, which may
     *                        include "value" (JSON string) or "file" (file
     *                        attachments)
     * @return a map containing the response information and data
     * @throws RuntimeException if an I/O error occurs during the request execution
     */
    public Map<String, Object> post(String url, List<Map<String, Object>> queryParameters,
            List<Map<String, Object>> headers, Map<String, Object> payload) {
        try {
            return actionable.executeApiRequest(url, Constants.HTTP_POST, queryParameters, headers, payload);
        } catch (IOException cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, url, "executing POST request");
            throw new RuntimeException(result.getUserMessage(), cause);
        }
    }

    public Map<String, Object> postResponseAsFile(String url, List<Map<String, Object>> queryParameters,
            List<Map<String, Object>> headers, Map<String, Object> payload) {
        if (payload != null) {
            for (Map.Entry<String, Object> entry : payload.entrySet()) {
                logger.info("Payload entry - Key: '{}', Value: '{}', Value Type: {}", entry.getKey(), entry.getValue(),
                        entry.getValue() != null ? entry.getValue().getClass().getName() : "null");
            }
        }
        return actionable.getApiResponseFile(url, Constants.HTTP_POST, queryParameters, headers, payload);
    }
}
