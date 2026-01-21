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
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.auth.AuthPayload;
import app.krista.extensions.development.api.rest.auth.OAuthClient;
import app.krista.extensions.development.api.rest.auth.TestConnectionResponse;
import app.krista.extensions.development.api.rest.connectors.ActionableImplProvider;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static app.krista.extensions.development.api.rest.api.AuthResource.*;
import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;
@Service
public class AuthHelper {

    private final AttributeStore attributeStore;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final Logger LOGGER = LoggerFactory.getLogger(AuthHelper.class);

    @Inject
    public AuthHelper(AttributeStore attributeStore, RefreshTokenStore refreshTokenStore, Invoker invoker) {
        this.attributeStore = attributeStore;
        this.refreshTokenStore = refreshTokenStore;
        this.invoker = invoker;
    }

    public Map<String, Object> payloadHandle(AuthPayload authPayload, RestApiAttributes restApiAttributes) throws IOException {
        AuthPayload.AuthType currentAuthType = authPayload.getAuthType();
        Object previousCred = null;
        String previousCredType = null;
        for (AuthPayload.AuthType authType : AuthPayload.AuthType.values()) {
            if (!authType.equals(currentAuthType)) {
                String authTypeName = authType.name();
                if (attributeStore.get(authTypeName) != null){
                    previousCredType = authTypeName;
                    previousCred = attributeStore.get(previousCredType);
                }
                attributeStore.remove(authTypeName);
            }
        }
        attributeStore.put(currentAuthType.name(), GSON_JSON_MAPPER.toString(restApiAttributes));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(previousCredType, previousCred);
        return resultMap;
    }

    public void updateAttributeStore(String authType, Map.Entry<String, Object> entry) throws IOException {
        attributeStore.remove(authType);
        if (entry.getKey() != null) {
            attributeStore.put(entry.getKey(), GSON_JSON_MAPPER.toString(entry.getValue()));
        }
    }

    public String validateAndTestConnection(ActionableImplProvider actionableImplProvider, AuthPayload authPayload) {
        ActionableImpl actionable;
        TestConnectionResponse testConnectionResponse;
        Response response;
        try {
            actionable = actionableImplProvider.getRestClientForAdmin();
            if (actionable == null) {
                testConnectionResponse = new TestConnectionResponse(false, PROVIDE_VALID_INPUT, null);
                return GSON_JSON_MAPPER.toString(testConnectionResponse);
            }
            response = actionable.testConnection();

            if (response.isSuccessful()) {
                testConnectionResponse = new TestConnectionResponse(true, null, null);

            } else {
                LOGGER.error("Failed to connect. {}", response.message());
                throw new IOException("Failed to connect: " + response.message());
            }
        } catch (IOException cause) {
            LOGGER.error("Failed to connect. {}", cause.getMessage());
            if (Objects.equals(authPayload.getAccessTokenUrl(), "https://oauth2.googleapis.com/token")){
                String routingUrl = invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE) + "/" + "rest/callback";
                OAuthClient oAuthClient = new OAuthClient(authPayload.getClientId(), authPayload.getClientSecret(), authPayload.getAuthUrl(), authPayload.getAccessTokenUrl(), authPayload.getScope(), routingUrl);
                oAuthClient.revokeAccessToken(refreshTokenStore.get(authPayload.getClientId()));
            }
            refreshTokenStore.remove(authPayload.getClientId());
            // Retry getting the client
            try {
                actionable = actionableImplProvider.getRestClientForAdmin();
                if (actionable != null) {
                    response = actionable.testConnection();
                    if (response.isSuccessful()) {
                        testConnectionResponse = new TestConnectionResponse(true, null, null);
                    } else {
                        throw new IOException(RETRY_FAILED + response.message());
                    }
                } else {
                    testConnectionResponse = new TestConnectionResponse(false, PROVIDE_VALID_INPUT, null);
                    return GSON_JSON_MAPPER.toString(testConnectionResponse);
                }
                // Assuming getRestClientForAdmin() stores the new refresh token in refreshTokenStore
            } catch (IOException retryException) {
                LOGGER.error("Failed to connect. {}", retryException.getMessage());
                testConnectionResponse = new TestConnectionResponse(false, retryException.getMessage(), null);
            }
        }
        return GSON_JSON_MAPPER.toString(testConnectionResponse);
    }

    @NotNull
    public String testConnection(ActionableImpl actionable) {
        TestConnectionResponse testConnectionResponse;
        try (Response response = actionable.testConnection()) {
            if (response.isSuccessful()) {
                testConnectionResponse = new TestConnectionResponse(true, null, null);
            } else {
                LOGGER.error("Failed to connect. {}", response.message());
                testConnectionResponse = new TestConnectionResponse(false,
                        TEST_CONNECTION_FAILED_PLEASE_CHECK_EXTENSION_LOGS_FOR_MORE_DETAILS, null);
            }
        } catch (Exception cause) {
            LOGGER.error("Failed to connect. {}", cause.getMessage());
            testConnectionResponse = new TestConnectionResponse(false, TEST_CONNECTION_FAILED_PLEASE_CHECK_EXTENSION_LOGS_FOR_MORE_DETAILS, null);
        }
        return GSON_JSON_MAPPER.toString(testConnectionResponse);
    }
}
