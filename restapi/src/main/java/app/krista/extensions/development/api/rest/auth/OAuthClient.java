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

package app.krista.extensions.development.api.rest.auth;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import app.krista.model.field.util.GsonJsonMapper;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OAuthClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthClient.class);

    public static final String GOOGLEAPIS_COM_REVOKE = "https://oauth2.googleapis.com/revoke";
    private final WebTarget webTargetForAuthUrl;
    private final WebTarget webTargetForAccessTokenUrl;
    private final String clientId;
    private final String routingInfo;
    private final String scope;
    private final String clientSecret;


    public OAuthClient(String clientId, String clientSecret, String authUrl, String accessTokenUrl, String scope, String routingInfo) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.routingInfo = routingInfo;
        this.scope = (scope != null && !scope.isEmpty()) ? scope : "";
        webTargetForAuthUrl = ClientBuilder.newClient()
                .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
                .target(authUrl);
        webTargetForAccessTokenUrl = ClientBuilder.newClient()
                .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
                .target(accessTokenUrl);
    }

    public static JsonObject handleResponse(Response response) {
        String responseString = response.readEntity(String.class);
        GsonJsonMapper gsonJsonMapper = GsonJsonMapper.create();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return gsonJsonMapper.fromString(responseString, JsonObject.class);
        } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            String errorMessage = ErrorMessages.oauthAuthorizationFailed(response.getStatusInfo().getReasonPhrase());
            LOGGER.error("OAuth authorization failed: {}", errorMessage);
            throw new MustAuthorizeException(errorMessage);
        } else {
            String errorMessage = ErrorMessages.oauthAuthorizationFailed(responseString);
            LOGGER.error("OAuth authorization failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public String getOAuthUrl(String state) {
        WebTarget authorize = webTargetForAuthUrl;
        authorize = authorize.queryParam("response_type", "code");
        authorize = authorize.queryParam("client_id", clientId);
        authorize = authorize.queryParam("redirect_uri", routingInfo);
        if (!this.scope.isEmpty()) {
            authorize = authorize.queryParam("scope", scope);
        }
        authorize = authorize.queryParam("state", state);
        authorize = authorize.queryParam("access_type", "offline");
        authorize = authorize.queryParam("approval_prompt", "force");
        return authorize.getUri().toString();
    }

    public AccessToken getAccessToken(String code) {
        return getAccessToken(new Form()
                .param("grant_type", "authorization_code")
                .param("client_id", clientId)
                .param("scopes", scope)
                .param("client_secret", clientSecret)
                .param("redirect_uri", routingInfo)
                .param("code", code));
    }

    public void revokeAccessToken(String token) {
        String revokeUrl = GOOGLEAPIS_COM_REVOKE;
        Response response = ClientBuilder.newClient()
                .target(revokeUrl)
                .queryParam("token", token)
                .request()
                .post(Entity.text("")); // Empty entity for POST request

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            LOGGER.info("Token successfully revoked");
        } else {
            String responseBody = response.readEntity(String.class);
            LOGGER.error("Failed to revoke the token. Status code: {}. Response: {}", response.getStatus(), responseBody);
        }
    }

    public AccessToken refreshAccessToken(String refresh_token) {
        return getAccessToken(new Form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", refresh_token));
    }

    private AccessToken getAccessToken(Form body) {
        String authHeader = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        Response response = webTargetForAccessTokenUrl
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + authHeader)
                .post(Entity.form(body));
        return AccessToken.create(handleResponse(response));
    }


}
