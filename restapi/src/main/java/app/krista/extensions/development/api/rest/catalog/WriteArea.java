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
import app.krista.extensions.development.api.rest.impl.WriteAction;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.extensions.development.api.rest.util.ResponseUtil;
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
 * Catalog area for Write operations (POST requests).
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
public class WriteArea implements PreDestroy {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteArea.class);
    private static final long EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS = 30;

    private final WriteAction writeable;
    private final EventHandler eventHandler;
    private final ExecutorService executorService;

    @Inject
    public WriteArea(WriteAction writeable, EventHandler eventHandler) {
        this.writeable = writeable;
        this.eventHandler = eventHandler;
        // Create executor with daemon threads to allow JVM shutdown
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "WriteArea-EventHandler");
            thread.setDaemon(true);
            return thread;
        });
        LOGGER.debug("WriteArea initialized with ExecutorService");
    }

    /**
     * Cleanup method called by HK2 container when service is being destroyed.
     * Ensures proper shutdown of the ExecutorService to prevent thread leaks.
     */
    @Override
    public void preDestroy() {
        LOGGER.info("Shutting down WriteArea ExecutorService");
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
                LOGGER.info("WriteArea ExecutorService shut down successfully");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for ExecutorService shutdown", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_4b7f4c35-4632-428c-8337-5505676f08ca",
            name = "Post With Filter",
            description = "The request sends data to the server with specified filters.",
            area = "Write",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> postWithFilter(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> queryParameters) {
        return writeable.post(uRL, queryParameters, Collections.emptyList(), payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_fb8c4760-a743-4b4a-9367-7c6baaea654e",
            name = "Post With Filter and Header",
            description = "The request sends data to the server with specified filters and headers",
            area = "Write",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> postWithFilterAndHeader(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File}", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> queryParameters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        return writeable.post(uRL, queryParameters, headers, payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_c5b23f21-8df6-45a0-9659-ada675643efb",
            name = "Post",
            description = "The request sends data to the server.",
            area = "Write",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> post(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload) {
        return writeable.post(uRL, Collections.emptyList(), Collections.emptyList(), payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_ab390be1-263e-4110-8bd0-1f28123c48e9",
            name = "Post With Headers",
            description = "The request sends data to the server with specified headers.",
            area = "Write",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> postWithHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File}", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        return writeable.post(uRL, Collections.emptyList(), headers, payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_716afb97-d6d2-43d1-bb94-e4d27bb0ad6a",
            name = "Wait for Event Post Request",
            description = "Wait for Event Post Request with Header and Filter",
            area = "Write",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Text(name = "Task Id", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String waitForEventPostRequest(
            @Field.Text(name = "Url", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String url,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> queryParameters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> headers) {
        final String taskId = UUID.randomUUID().toString();
        executorService.submit(() -> {
            Map<String, Object> post = writeable.post(url, queryParameters, headers, payload);
            FreeForm freeForm = new FreeForm();
            freeForm.put("Data", "{Response Info:FreeForm, Response:[ Composite ]}", post);
            LOGGER.info("Event has been sent for this Task ID :{}", taskId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException cause) {
                throw new RuntimeException(cause);
            }
            eventHandler.handleEvent(taskId, freeForm);
        });
        return taskId;
    }

    @CatalogRequest(
            id = "localDomainRequest_49af755d-7b41-42a9-8ad7-2c074cf3a51d",
            name = "Wait for Event Post Response",
            description = "Accepts task id to get Post request response for Wait for event",
            area = "Write",
            type = CatalogRequest.Type.WAIT_FOR_EVENT)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> waitForEventPostResponse(
            @Field(name = "eventName", type = "Text") String eventName,
            @Field(name = "eventData", type = "FreeForm") FreeForm eventData,
            @Field.Text(name = "Task Id", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String taskId) {
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) eventData.get(DATA);
        FreeForm info = (FreeForm) data.get(RESPONSE_INFO);
        String message = (String) info.get(STATUS_MESSAGE);
        if (message.contains(ERROR_MESSAGE)) {
            LOGGER.error(message);
            return ResponseUtil.createErrorResponse(message, info);
        }
        if (eventName.equals(taskId)) {
            return ResponseUtil.createSuccessResponse(eventData);
        }
        return Map.of();
    }

    @CatalogRequest(id = "localDomainRequest_a6962ac2-aa4a-4e99-afe3-a6975ec212fe",
            name = "Get Post Request Response as File",
            description = "This request sends data to the server with specified filters and headers and give you the response as a file.",
            area = "Write", type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.File(name = "Response File", multipleFileUpload = false, required = false, attributes = {
            @Attribute(name = "visualWidth", value = "S")}, options = {})
    public Map<String, Object> getPostRequestResponseAsFile(
            @Field.Text(name = "URL", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String uRL,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> headers,
            @Field.Desc(name = "Filters", type = "[ { key: Text, value: Text } ]", required = false) List<Map<String, Object>> filters) {
        try {
            LOGGER.info("Executing getPostRequestResponseAsFile with URL: {}", uRL);
            return writeable.postResponseAsFile(uRL, filters, headers, payload);
        } catch (Exception cause) {
            LOGGER.error("Error while getting response as a file: {}", cause.getMessage());
            throw new IllegalArgumentException(
                    ErrorMessages.catalogRequestError("downloading response as a file", cause.getMessage()));
        }
    }
}