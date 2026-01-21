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

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.development.api.rest.impl.Constants.*;

/**
 * Service class responsible for executing HTTP PUT and PATCH REST API calls.
 * <p>
 * The HTTP PUT request method creates a new resource or replaces the entire
 * representation of the target resource with the request payload.
 * The HTTP PATCH request method applies partial modifications to a resource.
 * This class provides various overloaded methods to support different combinations of
 * query parameters, headers, and request payloads.
 * </p>
 *
 * @see ActionableImpl
 * @see Constants
 */
@Service
public class ModifyAction {
    private final ActionableImpl actionable;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;

    @Inject
    public ModifyAction(AttributeStore attributeStore, RefreshTokenStore refreshTokenStore, Invoker invoker, KristaMediaClient kristaMediaClient) {
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.refreshTokenStore = refreshTokenStore;
        this.kristaMediaClient = kristaMediaClient;
        this.actionable = (!attributeStore.listValues().iterator().hasNext()) ? new ActionableImpl(null, refreshTokenStore, invoker, attributeStore, kristaMediaClient) : new ActionableImpl(GSON_JSON_MAPPER.fromString(attributeStore.listValues().iterator().next().toString(),
                RestApiAttributes.class), refreshTokenStore, invoker, attributeStore, kristaMediaClient); // setting RestApiAttributes null when Authentication Tab is Empty and user trying to execute API calls through authentication header
    }

    /**
     * Executes an HTTP PUT or PATCH request to the specified URL without query parameters, headers, or payload.
     *
     * @param url the target URL for the request
     * @param requestType the HTTP method type, either "PUT" or "PATCH"
     * @return a map containing the response information and data
     */
    public Map<String, Object> put(String url, String requestType) {
        return put(url, requestType, Collections.emptyList());
    }

    /**
     * Executes an HTTP PUT or PATCH request with query parameters.
     *
     * @param url the target URL for the request
     * @param requestType the HTTP method type, either "PUT" or "PATCH"
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @return a map containing the response information and data
     */
    public Map<String, Object> put(String url, String requestType, List<Map<String, Object>> queryParameters) {
        return put(url, requestType, queryParameters, Collections.emptyList());
    }

    /**
     * Executes an HTTP PUT or PATCH request with query parameters and custom headers.
     *
     * @param url the target URL for the request
     * @param requestType the HTTP method type, either "PUT" or "PATCH"
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @param headers a list of custom HTTP header key-value pairs
     * @return a map containing the response information and data
     */
    public Map<String, Object> put(String url, String requestType, List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers) {
        return put(url, requestType, queryParameters, headers, Map.of());
    }

    /**
     * Executes an HTTP PUT or PATCH request with query parameters, custom headers, and a request payload.
     * <p>
     * PUT replaces the entire resource with the provided payload, while PATCH applies partial modifications.
     * The method type is determined by the requestType parameter.
     * </p>
     *
     * @param url the target URL for the request
     * @param requestType the HTTP method type, either "PUT" or "PATCH" (case-insensitive)
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @param headers a list of custom HTTP header key-value pairs
     * @param payload a map containing the request body data
     * @return a map containing the response information and data
     * @throws RuntimeException if an I/O error occurs during the request execution
     */
    public Map<String, Object> put(String url, String requestType, List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers, Map<String, Object> payload) {
        String httpMethod = requestType.equalsIgnoreCase(HTTP_PATCH) ? HTTP_PATCH : HTTP_PUT;
        try {
            return actionable.executeApiRequest(url, httpMethod, queryParameters, headers, payload);
        } catch (IOException cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, url, "executing " + httpMethod + " request");
            throw new RuntimeException(result.getUserMessage(), cause);
        }
    }

}
