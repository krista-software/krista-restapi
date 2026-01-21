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

package app.krista.extensions.development.api.rest.util;

import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AttributeStore;

import java.io.IOException;

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;

public class AuthUtils {

    public static String getAuthTypeKey(AttributeStore attributeStore) throws IOException {
        if (attributeStore.get("Token") != null) {
            return "Token";
        } else if (attributeStore.get("Basic") != null) {
            return "Basic";
        } else if (attributeStore.get("OAuth") != null) {
            return "OAuth";
        }
        return null;
    }

    public static RestApiAttributes getRestApiAttributes(AttributeStore attributeStore, String key) throws IOException {
        return GSON_JSON_MAPPER.fromString(
                GSON_JSON_MAPPER.toString(attributeStore.get(key)), RestApiAttributes.class
        );
    }
}
