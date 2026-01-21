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

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;

/**
 * Service class responsible for executing HTTP GET REST API calls.
 * <p>
 * The HTTP GET method requests a representation of the specified resource.
 * Requests using GET should only retrieve data and must not include a request payload.
 * This class provides various overloaded methods to support different combinations of
 * query parameters, headers, pagination, and file responses.
 * </p>
 *
 * @see ActionableImpl
 * @see Constants
 */
@Service
public class ReadAction {
    private final ActionableImpl actionable;
    private final AttributeStore attributeStore;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final KristaMediaClient kristaMediaClient;

    @Inject
    public ReadAction(AttributeStore attributeStore, RefreshTokenStore refreshTokenStore, Invoker invoker, KristaMediaClient kristaMediaClient) {
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.refreshTokenStore = refreshTokenStore;
        this.kristaMediaClient = kristaMediaClient;
        this.actionable = (!attributeStore.listValues().iterator().hasNext()) ? new ActionableImpl(null, refreshTokenStore, invoker, attributeStore, kristaMediaClient) : new ActionableImpl(GSON_JSON_MAPPER.fromString(attributeStore.listValues().iterator().next().toString(),
                RestApiAttributes.class), refreshTokenStore, invoker, attributeStore, kristaMediaClient);  // setting RestApiAttributes null when Authentication Tab is Empty and user trying to execute API calls through authentication header
    }

    /**
     * Executes a simple HTTP GET request to the specified URL without query parameters or headers.
     *
     * @param url the target URL for the GET request
     * @return a map containing the response information and data
     */
    public Map<String, Object> get(String url) {
        return get(url, Collections.emptyList());
    }

    /**
     * Executes an HTTP GET request with query parameters.
     *
     * @param url the target URL for the GET request
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @return a map containing the response information and data
     */
    public Map<String, Object> get(String url, List<Map<String, Object>> queryParameters) {
        return get(url, queryParameters, Collections.emptyList());
    }

    /**
     * Executes an HTTP GET request with query parameters and custom headers.
     *
     * @param url the target URL for the GET request
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @param headers a list of custom HTTP header key-value pairs
     * @return a map containing the response information and data
     * @throws RuntimeException if an I/O error occurs during the request execution
     */
    public Map<String, Object> get(String url, List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers) {
        try {
            return actionable.executeApiRequest(url, Constants.HTTP_GET, queryParameters, headers, Map.of());
        } catch (IOException cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, url, "executing GET request");
            throw new RuntimeException(result.getUserMessage(), cause);
        }
    }

    /**
     * Executes an HTTP GET request with client-side pagination support.
     * <p>
     * Note: This method loads the entire response into memory and applies pagination logic client-side.
     * For large datasets, consider using server-side pagination if supported by the API.
     * </p>
     *
     * @param url the target URL for the GET request
     * @param filters a list of query parameter key-value pairs to filter the results
     * @param headers a list of custom HTTP header key-value pairs
     * @param pageSize the number of records per page
     * @param pageIndex the zero-based page index to retrieve
     * @return a map containing paginated response data, total records, total pages, page size, and page index
     */
    public Map<String, Object> getCustomResponseWithPagination(String url, List<Map<String, Object>> filters, List<Map<String, Object>> headers, Double pageSize, Double pageIndex) {
        return actionable.executeCustomPaginatedApiRequest(url, Constants.HTTP_GET, filters, headers, pageSize, pageIndex);
    }

    /**
     * Executes an HTTP GET request and returns the response as a file.
     * <p>
     * The response body is converted to a file and uploaded to Krista's media server.
     * Useful for downloading files, reports, or binary data from REST APIs.
     * </p>
     *
     * @param url the target URL for the GET request
     * @param filters a list of query parameter key-value pairs to filter the results
     * @param headers a list of custom HTTP header key-value pairs
     * @return a map containing the response information and the downloaded file
     */
    public Map<String, Object> getResponseFile(String url, List<Map<String, Object>> filters, List<Map<String, Object>> headers) {
        return actionable.getApiResponseFile(url, Constants.HTTP_GET, filters, headers);
    }
}
