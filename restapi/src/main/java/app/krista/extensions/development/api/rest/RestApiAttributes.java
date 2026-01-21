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

package app.krista.extensions.development.api.rest;

import app.krista.extensions.development.api.rest.auth.AuthPayload;
import com.google.gson.annotations.SerializedName;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public final class RestApiAttributes {
    @SerializedName("Username")
    private final String username;

    @SerializedName("password")
    private final String password;

    @SerializedName("Api Url")
    private final String apiUrl;

    @SerializedName("token")
    private final String token;

    @SerializedName("Token Type")
    private final String tokenType;

    @SerializedName("Auth Type")
    private final AuthPayload.AuthType authType;

    @SerializedName("Client Id")
    private final String clientId;

    @SerializedName("Client Secret")
    private final String clientSecret;

    @SerializedName("Auth Url")
    private final String authUrl;

    @SerializedName("Access Token Url")
    private final String accessTokenUrl;

    @SerializedName("State")
    private final String state;

    @SerializedName("Scope")
    private final String scope;

    @Inject
    public RestApiAttributes(String username, String password, String token, String tokenType, AuthPayload.AuthType authType, String apiUrl,
                             String clientId, String clientSecret, String authUrl, String accessTokenUrl,
                             String state, String scope) {
        this.authType = authType;
        this.apiUrl = apiUrl;
        this.username = username;
        this.password = password;
        this.token = token;
        this.tokenType = tokenType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authUrl = authUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.state = state;
        this.scope = scope;
    }

    public static RestApiAttributes create(AuthPayload payload) {
        return new RestApiAttributes(payload.getUserName(), payload.getPassword(), payload.getToken(), payload.getTokenType(), payload.getAuthType(), payload.getApiUrl(), payload.getClientId(),
                payload.getClientSecret(), payload.getAuthUrl(), payload.getAccessTokenUrl(),
                payload.getState(), payload.getScope());
    }

    public String getUserName() {
        return username;
    }

    public String getPassWord() {
        return password;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public AuthPayload.AuthType getAuthType() {
        return authType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getState() {
        return state;
    }

    public String getScope() {
        return scope;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

}
