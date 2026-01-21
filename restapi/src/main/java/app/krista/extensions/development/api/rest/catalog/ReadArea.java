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

package app.krista.extensions.development.api.rest.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.development.api.rest.impl.ReadAction;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.extensions.development.api.rest.util.ResponseUtil;
import app.krista.extensions.development.api.rest.util.RestApiConstants;
import app.krista.extensions.util.EventHandler;
import app.krista.model.base.FreeForm;
import org.glassfish.hk2.api.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static app.krista.extensions.development.api.rest.util.RestApiConstants.*;

/**
 * Catalog area for Read operations (GET requests).
 * <p>
 * This class implements PreDestroy to ensure proper cleanup of the ExecutorService
 * when the service is being destroyed by the HK2 container.
 * </p>
 */
@Domain(id = "catEntryDomain_d71d6d2e-f830-46bb-aa3d-9453b3de9efa",
        name = "API Integrations",
        ecosystemId = "catEntryEcosystem_954d3331-9431-48e5-bcf2-a51a5453b74f",
        ecosystemName = "Development",
        ecosystemVersion = "2a196e0e-a8d8-4d4e-b545-dce57865dc20")
public class ReadArea implements PreDestroy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadArea.class);
    private static final long EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS = 30;

    private final ReadAction readable;
    private final EventHandler eventHandler;
    private final ExecutorService executorService;

    @Inject
    public ReadArea(ReadAction readable, EventHandler eventHandler) {
        this.readable = readable;
        this.eventHandler = eventHandler;
        // Create executor with daemon threads to allow JVM shutdown
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "ReadArea-EventHandler");
            thread.setDaemon(true);
            return thread;
        });
        LOGGER.debug("ReadArea initialized with ExecutorService");
    }

    /**
     * Cleanup method called by HK2 container when service is being destroyed.
     * Ensures proper shutdown of the ExecutorService to prevent thread leaks.
     */
    @Override
    public void preDestroy() {
        LOGGER.info("Shutting down ReadArea ExecutorService");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                LOGGER.warn("ExecutorService did not terminate gracefully within {} seconds, forcing shutdown",
                           EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS);
                executorService.shutdownNow();

                // Wait a bit for tasks to respond to being cancelled
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOGGER.error("ExecutorService did not terminate after forced shutdown");
                }
            } else {
                LOGGER.info("ReadArea ExecutorService shut down successfully");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for ExecutorService shutdown", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_8d176519-04c5-4c18-9b3b-3b7e4b76221f",
            name = "GetWithFilters",
            description = "The request that reads data from the specified resource with given filter parameters",
            area = "Read",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> getWithFilters(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "QueryParameters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> queryParameters) {
        try {
            return readable.get(uRL, queryParameters, Collections.emptyList());
        } catch (Exception cause) {
            LOGGER.error("Error while reading data with filters: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("reading data with filters", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_5bac6ba1-e63a-4f9b-9b56-451cdf7ae93c",
            name = "Get With Headers",
            description = "The request read data from specified resources with given headers",
            area = "Read",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> getWithHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        try {
            return readable.get(uRL, Collections.emptyList(), headers);
        } catch (Exception cause) {
            LOGGER.error("Error while reading data with headers: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("reading data with headers", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_d41e1993-8f65-4d0a-ae27-2f98c43654f9",
            name = "Get With Filters and Headers",
            description = "The request reads data from the specified resource with given filters and headers",
            area = "Read",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> getWithFiltersAndHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Filters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> filters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        try {
            return readable.get(uRL, filters, headers);
        } catch (Exception cause) {
            LOGGER.error("Error while reading data with filters and headers: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("reading data with filters and headers", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_6c4a4c66-df41-4d0a-8764-00c36e5a8a53",
            name = "Get",
            description = "The request is responsible for reading data from the specified resource",
            area = "Read",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> get(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL) {
        try {
            return readable.get(uRL, Collections.emptyList(), Collections.emptyList());
        } catch (Exception cause) {
            LOGGER.error("Error while reading data: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("reading data", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_231f9b04-0931-456d-acf0-2dfa9af8e12a",
            name = "Get with pagination",
            description = "If the records are too much to handle you can use the Krista pagination by just giving Page Size and Page Index.",
            area = "Read",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field(name = "Total Records", type = "Number", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    @Field(name = "Total Pages", type = "Number", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    @Field(name = "Page Size", type = "Number", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    @Field(name = "Page Index", type = "Number", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public Map<String, Object> getWithPagination(
            @Field.Text(name = "Url", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String url,
            @Field.Desc(name = "Filters", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> filters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> headers,
            @Field(name = "Page Size", type = "Number", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) Double pageSize,
            @Field(name = "Page Index", type = "Number", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) Double pageIndex) {
        try {
            return readable.getCustomResponseWithPagination(url, filters, headers, pageSize, pageIndex);
        } catch (Exception cause) {
            LOGGER.error("Error while reading data with pagination: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("reading data with pagination", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_57376c36-39a9-4bf1-a157-3daa61b2c602",
            name = "Get Response as a file",
            description = "Get Response as a file",
            area = "Read",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.File(name = "Response File", multipleFileUpload = false, required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public Map<String, Object> getResponseAsAFile(
            @Field.Text(name = "Url", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String url,
            @Field.Desc(name = "Filters", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> filters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> headers) {
        try {
            return readable.getResponseFile(url, filters, headers);
        } catch (Exception cause) {
            LOGGER.error("Error while getting response as a file: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("downloading response as a file", cause.getMessage()));
        }

    }

    @CatalogRequest(
            id = "localDomainRequest_4e2e730b-1521-41e0-9d2d-e231b49c487c",
            name = "Wait for Event Get Response",
            description = "Accepts task id to get request response for Wait for event",
            area = "Read",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> waitForEventGetResponse(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData,
            @Field.Text(name = "Task Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String taskId) {
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) eventData.get(DATA);
        FreeForm info = (FreeForm) data.get(RESPONSE_INFO);
        String message = (String) info.get(STATUS_MESSAGE);
        if (message.contains(RestApiConstants.ERROR_MESSAGE)) {
            LOGGER.error(message);
            return ResponseUtil.createErrorResponse(message, info);
        }
        if (eventName.equals(taskId)) {
            return ResponseUtil.createSuccessResponse(eventData);
        }
        return Map.of();
    }


    @CatalogRequest(
            id = "localDomainRequest_aba05d68-6154-48d6-bacf-f042dbd1645b",
            name = "Wait for Event Get Request",
            description = "Wait for Event Get Request with Header and Filter",
            area = "Read",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Text(name = "Task Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String waitForEventGetRequest(
            @Field.Text(name = "Url", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String url,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> queryParameters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> headers) {
        final String taskId = UUID.randomUUID().toString();
        executorService.submit(() -> {
            Map<String, Object> post = readable.get(url, queryParameters, headers);
            FreeForm freeForm = new FreeForm();
            freeForm.put("Data", "{Response Info:FreeForm, Response:[ Composite ]}", post);
            eventHandler.handleEvent(taskId, freeForm);
        });
        return taskId;
    }

}
