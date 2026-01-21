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

package app.krista.extensions.development.api.rest.connectors;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.auth.OAuthClient;
import app.krista.extensions.development.api.rest.impl.ActionableImpl;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.field.NamedValuedField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionableImplProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionableImplProvider.class);
    private final OAuthClient oAuthClient;
    private final RefreshTokenStore refreshTokenStore;
    private final AuthorizationContext authorizationContext;
    private final RequestContext requestContext;
    private final String authContextId;
    private final RestApiAttributes restApiAttributes;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;


    @Inject
    public ActionableImplProvider(RestApiAttributes restApiAttributes, RefreshTokenStore refreshTokenStore,
                                  AuthorizationContext authorizationContext, RequestContext requestContext, Invoker invoker, AttributeStore attributeStore ,KristaMediaClient kristaMediaClient) {
        this(restApiAttributes, refreshTokenStore, authorizationContext, requestContext, null, invoker, attributeStore,kristaMediaClient);
    }

    public ActionableImplProvider(RestApiAttributes restApiAttributes, RefreshTokenStore refreshTokenStore,
                                  AuthorizationContext authorizationContext, RequestContext requestContext,
                                  String authContextId, Invoker invoker, AttributeStore attributeStore,KristaMediaClient kristaMediaClient) {
        this.restApiAttributes = restApiAttributes;
        this.attributeStore = attributeStore;
        String routingUrl = invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE) + "/" + "rest/callback";
        this.oAuthClient = new OAuthClient(restApiAttributes.getClientId(), restApiAttributes.getClientSecret(),
                restApiAttributes.getAuthUrl(), restApiAttributes.getAccessTokenUrl(), restApiAttributes.getScope(), routingUrl);
        this.refreshTokenStore = refreshTokenStore;
        this.authorizationContext = authorizationContext;
        this.requestContext = requestContext;
        this.authContextId = authContextId;
        this.invoker = invoker;
        this.kristaMediaClient = kristaMediaClient;
    }

    public RestApiAttributes getRestApiAttributes() {
        return restApiAttributes;
    }

    public ActionableImpl getRestClient() {
        return getRestClient(!requestContext.invokeAsUser());
    }

    public ActionableImpl getRestClientForAdmin() {
        return getRestClient(true);
    }

    public ActionableImpl getRestClient(boolean useEmail) {
        String userId = getUserId(useEmail);
        String refreshToken;
        try {
            refreshToken = refreshTokenStore.get(userId);
        } catch (Exception cause) {
            LOGGER.error("Failed to retrieve refresh token for user {}: {}", userId, cause.getMessage());
            return null;
        }
        if (refreshToken == null) {
            LOGGER.warn("User {} is not authorized. Refresh token not found.", userId);
            throw createMustAuthorizationException(userId, false);
        }
        return getGraphServiceClient(userId, refreshToken);
    }

    private String getUserId(boolean calledFromValidateAttributes) {
        String userId = calledFromValidateAttributes || !requestContext.invokeAsUser()
                ? restApiAttributes.getClientId()
                : authorizationContext.getAuthorizedAccount().getAccountId();
        if (userId == null) {
            LOGGER.error("Failed to get account. User ID is null.");
            throw new IllegalStateException("Failed to get account");
        }
        return userId;
    }

    private MustAuthorizeException createMustAuthorizationException(String userId, boolean reAuthentication) {
        if (reAuthentication) {
            LOGGER.warn("Authentication error for user {}. Sending for re-authentication.", userId);
        } else {
            LOGGER.warn("Authentication error for user {}. Sending for authentication.", userId);
        }
        List<NamedValuedField> details = new ArrayList<>();
        NamedValuedField userIdField = new NamedValuedField("userId", "Text", userId, new HashMap<>(), new HashMap<>());
        details.add(userIdField);
        if (authContextId != null) {
            NamedValuedField contextIdField = new NamedValuedField("authContextId", "Text", authContextId, new HashMap<>(), new HashMap<>());
            details.add(contextIdField);
        }
        return new MustAuthorizeException(reAuthentication ? "Refresh Token Expired. Please reauthorize yourself" :
                "You are not authorized. Please authorize yourself", details);
    }

    private ActionableImpl getGraphServiceClient(String userId, String refreshToken) {
        refreshTokenStore.put(userId, refreshToken);
        return new ActionableImpl(restApiAttributes, refreshTokenStore, invoker, attributeStore, kristaMediaClient);
    }
}
