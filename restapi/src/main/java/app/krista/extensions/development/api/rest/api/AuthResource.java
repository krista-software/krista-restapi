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

package app.krista.extensions.development.api.rest.api;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.*;
import app.krista.extensions.development.api.rest.connectors.ActionableImplProviderFactory;
import app.krista.extensions.development.api.rest.impl.ActionableImpl;
import app.krista.extensions.development.api.rest.impl.AuthHelper;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.stores.RestApiAttributeStore;
import app.krista.extensions.development.api.rest.util.AuthUtils;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import app.krista.model.field.NamedValuedField;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;


@javax.ws.rs.Path("/auth")
public class AuthResource {
    public static final String PROVIDE_VALID_INPUT = "Please provide valid input.";
    public static final String TEST_CONNECTION_FAILED_PLEASE_CHECK_EXTENSION_LOGS_FOR_MORE_DETAILS = "Test connection failed, please check extension logs for more details.";
    public static final String TEST_CONNECTION_FAILED_WITH_ERROR_CODE = "Test connection failed with error code: ";
    public static final String AND_WITH_MESSAGE = " and with message: ";
    public static final String RETRY_FAILED = "Retry failed: ";
    private final AttributeStore attributeStore;
    private final RestApiAttributeStore restApiAttributeStore;
    private final ActionableImplProviderFactory actionableImplProviderFactory;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;
    private final AuthHelper authHelper;
    private final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    private final KristaMediaClient kristaMediaClient;

    @Inject
    public AuthResource(AttributeStore attributeStore, AuthHelper authHelper, RestApiAttributeStore restApiAttributeStore, ActionableImplProviderFactory actionableImplProviderFactory, RefreshTokenStore refreshTokenStore, Invoker invoker, KristaMediaClient kristaMediaClient) {
        this.attributeStore = attributeStore;
        this.authHelper = authHelper;
        this.restApiAttributeStore = restApiAttributeStore;
        this.actionableImplProviderFactory = actionableImplProviderFactory;
        this.refreshTokenStore = refreshTokenStore;
        this.invoker = invoker;
        this.kristaMediaClient = kristaMediaClient;
    }

    @GET
    @javax.ws.rs.Path("/docs/{subPath:.*}")
    public InputStream customTabs(@PathParam("subPath") String subPath) {
        String filePath = "ui/restapiauth/" + (subPath.isEmpty() ? "index.html" : subPath);
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    @POST
    @javax.ws.rs.Path("/saveCredentials")
    @Produces("text/plain")
    public String saveCredentials(AuthPayload authPayload) throws IOException {
        RestApiAttributes restApiAttributes = RestApiAttributes.create(authPayload);
        Map<String, Object> previousCred = authHelper.payloadHandle(authPayload, restApiAttributes);
        Map.Entry<String, Object> next = previousCred.entrySet().iterator().next();
        ActionableImpl actionable = new ActionableImpl(restApiAttributes, refreshTokenStore, invoker, attributeStore, null);
        SaveCredentialsResponse saveCredentialsResponse;
        try (Response response = actionable.testConnection()) {
            if (response.isSuccessful()) {
                saveCredentialsResponse = new SaveCredentialsResponse(true, false);
            } else {
                saveCredentialsResponse = new SaveCredentialsResponse(false, true);
                authHelper.updateAttributeStore(authPayload.getAuthType().name(), next);
            }
        } catch (IllegalArgumentException cause) {
            saveCredentialsResponse = new SaveCredentialsResponse(false, true);
            authHelper.updateAttributeStore(authPayload.getAuthType().name(), next);
            LOGGER.debug(cause.getMessage());
        }
        return GSON_JSON_MAPPER.toString(saveCredentialsResponse);
    }

    @GET
    @javax.ws.rs.Path("/getCredentials")
    @Produces("text/plain")
    public String getCredentials(@QueryParam("authType") String authType) throws IOException {
        String key = AuthUtils.getAuthTypeKey(attributeStore);
        if (key == null) {
            return "";
        }
        RestApiAttributes restApiAttributes = AuthUtils.getRestApiAttributes(attributeStore, key);
        if (restApiAttributes.getAuthType().name().equals(authType)) {
            return GSON_JSON_MAPPER.toString(restApiAttributes);
        }
        return "";
    }

    @GET
    @javax.ws.rs.Path("/getAuthKey")
    @Produces("text/plain")
    public String getAuthKey() throws IOException {
        String key = AuthUtils.getAuthTypeKey(attributeStore);
        return GSON_JSON_MAPPER.toString(Objects.requireNonNullElse(key, "Basic"));
    }

    @POST
    @javax.ws.rs.Path("/testConnection")
    @Produces("text/plain")
    public String testConnection(AuthPayload authPayload) throws IOException {
        ActionableImpl actionable;
        RestApiAttributes restApiAttributes = RestApiAttributes.create(authPayload);
        try {
            actionable = new ActionableImpl(restApiAttributes, refreshTokenStore, invoker, attributeStore, kristaMediaClient);
            if (AuthPayload.AuthType.OAuth.equals(authPayload.getAuthType())) {
                return authHelper.validateAndTestConnection(actionableImplProviderFactory.create(restApiAttributes), authPayload);
            }
        } catch (MustAuthorizeException cause) {
            String userId = (String) cause.getDetails().getFirst().getValue();
            Optional<NamedValuedField> authContextIdField = cause.getDetails().stream().filter(f -> Objects.equals(f.getName(), "authContextId")).findFirst();

            RestApiAttributes effectiveRestApiAttributes;
            String state;

            if (authContextIdField.isPresent()) {
                String authContextId = (String) authContextIdField.get().getValue();
                effectiveRestApiAttributes = restApiAttributeStore.load(authContextId);
                state = userId + "#" + authContextId;
            } else {
                effectiveRestApiAttributes = RestApiAttributes.create(authPayload);
                state = userId;
            }

            String routingUrl = invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE) + "/" + "rest/callback";
            OAuthClient oAuthClient = new OAuthClient(effectiveRestApiAttributes.getClientId(), effectiveRestApiAttributes.getClientSecret(), effectiveRestApiAttributes.getAuthUrl(), effectiveRestApiAttributes.getAccessTokenUrl(), effectiveRestApiAttributes.getScope(), routingUrl);
            String failedAuth = GSON_JSON_MAPPER.toString(new TestConnectionResponse(false, null, oAuthClient.getOAuthUrl(state)));
            return failedAuth;
        }
        return authHelper.testConnection(actionable);
    }
}
