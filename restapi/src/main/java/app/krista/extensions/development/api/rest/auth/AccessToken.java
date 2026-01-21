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


import com.google.gson.JsonObject;

public class AccessToken {

    public final String accessTokenValue;
    public final String scopes;
    public final Long expiresIn;
    public final String tokenType;
    public final String refreshToken;
    public final String state;

    public AccessToken(String accessTokenValue, String scopes, Long expiresIn, String tokenType, String refreshToken, String state) {
        this.accessTokenValue = accessTokenValue;
        this.scopes = scopes;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.state = state;
    }

    public static AccessToken create(JsonObject json) {
        String scopes = json.has("scopes") ? json.get("scopes").getAsString() : null;
        String state = json.has("state") ? json.get("state").getAsString() : null;
        String accessTokenValue = json.has("access_token") ? json.get("access_token").getAsString() : null;
        return new AccessToken(accessTokenValue,
                scopes,
                json.has("expires_in") ? json.get("expires_in").getAsLong() : null,
                json.has("token_type") ? json.get("token_type").getAsString() : null,
                json.has("refresh_token") ? json.get("refresh_token").getAsString() : null,
                state);
    }

    public String getAccessToken() {
        return accessTokenValue;
    }
}
