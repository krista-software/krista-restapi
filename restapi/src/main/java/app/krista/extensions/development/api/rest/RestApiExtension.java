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

import app.krista.extension.impl.anno.Extension;
import app.krista.extension.impl.anno.InvokerRequest;
import app.krista.extension.impl.anno.Java;
import app.krista.extension.impl.anno.StaticResource;

import java.util.Map;

@Extension(version = "2.0.16", name = "Rest API", jaxrsId = RestApiExtension.JAXRS_ID)
@Java(version = Java.Version.JAVA_21)
@StaticResource(path = "docs", file = "docs")
public class RestApiExtension {
    protected static final String JAXRS_ID = "rest";

    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> getCustomTabs() {
        return Map.of("Authentication", "rest/auth/docs/", "Documentation", "static/docs");
    }

}
