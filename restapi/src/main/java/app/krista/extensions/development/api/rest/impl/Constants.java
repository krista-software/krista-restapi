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

import app.krista.model.field.util.GsonJsonMapper;
import okhttp3.MediaType;

import java.util.Map;

public class Constants {

    //GSON Mapper
    public static final GsonJsonMapper GSON_JSON_MAPPER = GsonJsonMapper.create();

    public static final app.krista.model.field.Field textField = new app.krista.model.field.Field("com.krista.fields.Text", Map.of(), Map.of());
    public static final String INVALID_AUTH_TYPE = "Invalid auth type";
    public static final String EMAIL = "Email";
    public static final String CLIENT_ID = "Client Id";
    public static final String CLIENT_SECRET = "Client Secret";
    public static final String AUTH_URL = "Auth Url";
    public static final String STATE = "State";
    public static final String SCOPE = "Scope";
    public static final String API_URL = "Api Url";
    public static final String AUTH_TYPE = "Auth Type";
    public static final String ACCESS_TOKEN_URL = "Access Token Url";
    public static final String USERNAME = "Username";
    public static final String TOKEN = "token";
    public static final String TOKEN_TYPE = "Token Type";
    public static final String PASSWORD = "password";
    //Media Type
    public static MediaType JSON_MIME_TYPE = MediaType.parse("application/json");
    //Method Type
    public static String HTTP_GET = "GET";
    public static String HTTP_PUT = "PUT";
    public static String HTTP_PATCH = "PATCH";
    public static String HTTP_POST = "POST";
    public static String HTTP_DELETE = "DELETE";
    //Response Key
    public static String STATUS_CODE = "status";
    public static String MESSAGE = "message";
    public static String DATA = "data";


}
