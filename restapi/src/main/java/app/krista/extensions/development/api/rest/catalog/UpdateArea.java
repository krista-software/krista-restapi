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

package app.krista.extensions.development.api.rest.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.development.api.rest.impl.ModifyAction;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Domain(id = "catEntryDomain_d71d6d2e-f830-46bb-aa3d-9453b3de9efa",
        name = "API Integrations",
        ecosystemId = "catEntryEcosystem_954d3331-9431-48e5-bcf2-a51a5453b74f",
        ecosystemName = "Development",
        ecosystemVersion = "2a196e0e-a8d8-4d4e-b545-dce57865dc20")
public class UpdateArea {

    private final ModifyAction modifiable;

    @Inject
    public UpdateArea(ModifyAction modifiable) {
        this.modifiable = modifiable;
    }

    @CatalogRequest(
            id = "localDomainRequest_ea575add-a8ae-41a6-857a-e8a57013e1ff",
            name = "Modify",
            description = "The request creates a new resource or replaces a representation of the target resource with the request payload.",
            area = "Update",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> modify(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.PickOne(name = "Request Type", values = {"PUT", "PATCH"}) String requestType,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload) {
        return modifiable.put(uRL, requestType, Collections.emptyList(), Collections.emptyList(), payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_054c5e8d-d1b4-4360-8fab-6ce3d7cf20fe",
            name = "Modify Using Headers",
            description = "The request creates a new resource or replaces a representation of the target resource with the request payload and supported headers.",
            area = "Update",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> modifyUsingHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.PickOne(name = "Request Type", values = {"PUT", "PATCH"}) String requestType,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        return modifiable.put(uRL, requestType, Collections.emptyList(), headers, payload);
    }

    @CatalogRequest(
            id = "localDomainRequest_355aca14-607b-4d68-b7a6-4641a6da187a",
            name = "Modify Using Filters",
            description = "The request creates a new resource or replaces a representation of the target resource with the request payload with the filters",
            area = "Update",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> modifyUsingFilters(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.PickOne(name = "Request Type", values = {"PUT", "PATCH"}) String requestType,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> queryParameters) {
        Map<String, Object> responseMap = modifiable.put(uRL, requestType, queryParameters, Collections.emptyList(), payload);
        return Response.create(responseMap);
    }

    @CatalogRequest(
            id = "localDomainRequest_b445dac8-74cf-4a0e-82aa-20491838590f",
            name = "Modify Using Filters And Headers",
            description = "The request takes query parameters and headers and creates a new resource or replaces a representation of the target resource with the request payload.",
            area = "Update",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> modifyUsingFiltersAndHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.PickOne(name = "Request Type", values = {"PUT", "PATCH"}) String requestType,
            @Field.Desc(name = "Payload", type = "{ key: Text, value: Text, file: File }", required = true) Map<String, Object> payload,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> queryParameters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        return modifiable.put(uRL, requestType, queryParameters, headers, payload);
    }

}
