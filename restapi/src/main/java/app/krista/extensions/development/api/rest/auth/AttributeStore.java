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

import app.krista.extensions.util.KeyValueStore;
import com.google.gson.JsonObject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;

@Service
public final class AttributeStore {

    private final KeyValueStore keyValueStore;

    @Inject
    public AttributeStore(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    public void put(String key, Object refToken) throws IOException {
        keyValueStore.put(key, refToken);
    }

    public Object get(String key) throws IOException {
        return keyValueStore.get(key, JsonObject.class);
    }

    public void remove(String key) throws IOException {
        keyValueStore.remove(key);
    }

    public Iterable<Object> listValues() {
        return keyValueStore.listValues();
    }

}
