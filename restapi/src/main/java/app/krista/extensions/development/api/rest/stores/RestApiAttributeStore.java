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

package app.krista.extensions.development.api.rest.stores;

import app.krista.extension.util.InvokerAttributes;
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AuthPayload;
import app.krista.extensions.development.api.rest.impl.Constants;
import app.krista.extensions.util.KeyValueStore;
import app.krista.model.field.util.JavaTypes;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static app.krista.extensions.development.api.rest.impl.Constants.GSON_JSON_MAPPER;

@Service
public class RestApiAttributeStore {
    private final KeyValueStore store;

    @Inject
    public RestApiAttributeStore(KeyValueStore store) {
        this.store = store;
    }

    public RestApiAttributes load(String authContextId) {
        Map<String, Object> attributes = GSON_JSON_MAPPER.fromString((String) store.get(authContextId), JavaTypes.getTypedClass(Map.class, String.class, Object.class));
        return new RestApiAttributes(InvokerAttributes.getStringOrNull(attributes, Constants.USERNAME),
                InvokerAttributes.getStringOrNull(attributes, Constants.PASSWORD),
                InvokerAttributes.getStringOrNull(attributes, Constants.TOKEN),
                InvokerAttributes.getStringOrNull(attributes, Constants.TOKEN_TYPE),
                AuthPayload.AuthType.valueOf(InvokerAttributes.getStringOrEmpty(attributes, Constants.AUTH_TYPE)),
                InvokerAttributes.getStringOrNull(attributes, Constants.API_URL),
                InvokerAttributes.getStringOrNull(attributes, Constants.CLIENT_ID),
                InvokerAttributes.getStringOrNull(attributes, Constants.CLIENT_SECRET),
                InvokerAttributes.getStringOrNull(attributes, Constants.AUTH_URL),
                InvokerAttributes.getStringOrNull(attributes, Constants.ACCESS_TOKEN_URL),
                InvokerAttributes.getStringOrNull(attributes, Constants.STATE),
                InvokerAttributes.getStringOrNull(attributes, Constants.SCOPE));
    }

    public String save(RestApiAttributes restApiAttributes) {
        String authContextId = UUID.randomUUID().toString();
        return getKey(restApiAttributes, authContextId);
    }


    /**
     * Removes given authContext id from KeyValueStore
     *
     * @param authContextId authContext id
     */
    public void remove(String authContextId) {
        store.remove(authContextId);
    }

    public String saveCred(RestApiAttributes restApiAttributes, String key) {
        return getKey(restApiAttributes, key);
    }

    private String getKey(RestApiAttributes restApiAttributes, String key) {
        HashMap<String, Object> map = new HashMap<>();

        addAttributeIfNotNull(map, Constants.USERNAME, restApiAttributes.getUserName());
        addAttributeIfNotNull(map, Constants.TOKEN, restApiAttributes.getToken());
        addAttributeIfNotNull(map, Constants.TOKEN_TYPE, restApiAttributes.getTokenType());
        addAttributeIfNotNull(map, Constants.PASSWORD, restApiAttributes.getPassWord());
        addAttributeIfNotNull(map, Constants.AUTH_TYPE, restApiAttributes.getAuthType());
        addAttributeIfNotNull(map, Constants.API_URL, restApiAttributes.getApiUrl());
        addAttributeIfNotNull(map, Constants.CLIENT_ID, restApiAttributes.getClientId());
        addAttributeIfNotNull(map, Constants.CLIENT_SECRET, restApiAttributes.getClientSecret());
        addAttributeIfNotNull(map, Constants.AUTH_URL, restApiAttributes.getAuthUrl());
        addAttributeIfNotNull(map, Constants.ACCESS_TOKEN_URL, restApiAttributes.getAccessTokenUrl());
        addAttributeIfNotNull(map, Constants.STATE, restApiAttributes.getState());
        addAttributeIfNotNull(map, Constants.SCOPE, restApiAttributes.getScope());
        store.put(key, GSON_JSON_MAPPER.toString(map));
        return key;
    }

    private void addAttributeIfNotNull(HashMap<String, Object> map, String attributeName, Object attributeValue) {
        if (attributeValue != null) {
            map.put(attributeName, attributeValue);
        }
    }
}
