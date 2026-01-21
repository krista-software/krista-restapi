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
 * Service class responsible for executing HTTP DELETE REST API calls.
 * <p>
 * The HTTP DELETE request method deletes the specified resource from the server.
 * This class provides various overloaded methods to support different combinations of
 * query parameters and headers for conditional or filtered delete operations.
 * </p>
 *
 * @see ActionableImpl
 * @see Constants
 */
@Service
public class RemoveAction {
    private final RefreshTokenStore refreshTokenStore;
    private final ActionableImpl actionable;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;


    @Inject
    public RemoveAction(AttributeStore attributeStore, RefreshTokenStore refreshTokenStore, Invoker invoker, KristaMediaClient kristaMediaClient) {
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.refreshTokenStore = refreshTokenStore;
        this.kristaMediaClient = kristaMediaClient;
        this.actionable = (!attributeStore.listValues().iterator().hasNext()) ? new ActionableImpl(null, refreshTokenStore, invoker, attributeStore, kristaMediaClient) : new ActionableImpl(GSON_JSON_MAPPER.fromString(attributeStore.listValues().iterator().next().toString(),
                RestApiAttributes.class), refreshTokenStore, invoker, attributeStore, kristaMediaClient); // setting RestApiAttributes null when Authentication Tab is Empty and user trying to execute API calls through authentication header
    }

    /**
     * Executes a simple HTTP DELETE request to the specified URL without query parameters or headers.
     *
     * @param url the target URL for the DELETE request
     * @return a map containing the response information and data
     */
    public Map<String, Object> delete(String url) {
        return delete(url, Collections.emptyList());
    }

    /**
     * Executes an HTTP DELETE request with query parameters.
     * <p>
     * Query parameters can be used for conditional or filtered delete operations,
     * such as deleting resources that match specific criteria.
     * </p>
     *
     * @param url the target URL for the DELETE request
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @return a map containing the response information and data
     */
    public Map<String, Object> delete(String url, List<Map<String, Object>> queryParameters) {
        return delete(url, queryParameters, Collections.emptyList());
    }

    /**
     * Executes an HTTP DELETE request with query parameters and custom headers.
     * <p>
     * Headers can be used for conditional delete operations, such as If-Match for optimistic locking,
     * or for authentication and authorization purposes.
     * </p>
     *
     * @param url the target URL for the DELETE request
     * @param queryParameters a list of query parameter key-value pairs to append to the URL
     * @param headers a list of custom HTTP header key-value pairs
     * @return a map containing the response information and data
     * @throws RuntimeException if an I/O error occurs during the request execution
     */
    public Map<String, Object> delete(String url, List<Map<String, Object>> queryParameters, List<Map<String, Object>> headers) {
        try {
            return actionable.executeApiRequest(url, Constants.HTTP_DELETE, queryParameters, headers, Map.of());
        } catch (IOException cause) {
            ApiExceptionHandler.Result result = ApiExceptionHandler.handleAndLog(cause, url, "executing DELETE request");
            throw new RuntimeException(result.getUserMessage(), cause);
        }
    }
}
