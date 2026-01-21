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

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AccessToken;
import app.krista.extensions.development.api.rest.auth.OAuthClient;
import app.krista.extensions.development.api.rest.connectors.ActionableImplProvider;
import app.krista.extensions.development.api.rest.connectors.ActionableImplProviderFactory;
import app.krista.extensions.development.api.rest.impl.Constants;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.stores.RestApiAttributeStore;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Objects;

import static app.krista.extensions.development.api.rest.impl.HTTPRequest.BEARER;

@Path("/")
public class AuthCallBackResource {
    private final RestApiAttributeStore restApiAttributeStore;
    private final ActionableImplProviderFactory actionableImplProviderFactory;
    private final AuthorizationContext context;
    private final AuthorizationListener authorizationListener;
    private final RefreshTokenStore refreshTokenStore;
    private final Invoker invoker;


    @Inject
    public AuthCallBackResource(RestApiAttributeStore restApiAttributeStore, ActionableImplProviderFactory actionableImplProviderFactory,
                                AuthorizationContext context, AuthorizationListener authorizationListener,
                                RefreshTokenStore refreshTokenStore, Invoker invoker) {
        this.restApiAttributeStore = restApiAttributeStore;
        this.actionableImplProviderFactory = actionableImplProviderFactory;
        this.context = context;
        this.authorizationListener = authorizationListener;
        this.refreshTokenStore = refreshTokenStore;
        this.invoker = invoker;
    }

    @GET
    @Path("callback")
    public String getCallBack(@QueryParam("code") String code, @QueryParam("state") String state) {
        Objects.requireNonNull(code);
        String[] parts = state.split("#");
        if (parts[0].isBlank() || parts.length > 2) {
            throw new BadRequestException("Invalid state parameters!");
        }
        String key = parts[0];
        String authContextId = parts.length == 2 ? parts[1] : null;
        try {
            ActionableImplProvider clientProvider;
            RestApiAttributes effectiveRestApiAttributes = restApiAttributeStore.load(authContextId);
            clientProvider = actionableImplProviderFactory.create(effectiveRestApiAttributes);
            String routingUrl = invoker.getRoutingInfo().getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE) + "/" + "rest/callback";
            OAuthClient oAuthClient = new OAuthClient(clientProvider.getRestApiAttributes().getClientId(),
                    clientProvider.getRestApiAttributes().getClientSecret(), clientProvider.getRestApiAttributes().getAuthUrl(),
                    clientProvider.getRestApiAttributes().getAccessTokenUrl(), clientProvider.getRestApiAttributes().getScope(), routingUrl);
            AccessToken authTokenResponse = oAuthClient.getAccessToken(code);
            if (authTokenResponse.refreshToken == null) {
                refreshTokenStore.put(key, authTokenResponse.getAccessToken());
            } else {
                refreshTokenStore.put(key, authTokenResponse.refreshToken);
            }
            if (context.isAuthenticated()) {
                authorizationListener.authorized();
                return "User authenticated successfully.";
            }
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request.Builder requestBuilder = new Request.Builder()
                    .url(effectiveRestApiAttributes.getApiUrl())
                    .method(Constants.HTTP_GET, null);
            requestBuilder.addHeader("Authorization", BEARER + authTokenResponse.getAccessToken());
            final Request request = requestBuilder.build();
            final Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return "User authenticated successfully. Save Changes.";
            } else {
                return "User authenticated successfully. Test connection failed. Please check API Url.";
            }
        } catch (Exception cause) {
            System.err.println("Error occurred during authorization.");
            throw new IllegalStateException("Error occurred during authorization", cause);
        } finally {
            if (authContextId != null) {
                restApiAttributeStore.remove(authContextId);
            }
        }
    }

}
