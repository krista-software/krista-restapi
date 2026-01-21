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
import app.krista.extensions.development.api.rest.impl.RemoveAction;
import app.krista.extensions.development.api.rest.util.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Domain(id = "catEntryDomain_d71d6d2e-f830-46bb-aa3d-9453b3de9efa",
        name = "API Integrations",
        ecosystemId = "catEntryEcosystem_954d3331-9431-48e5-bcf2-a51a5453b74f",
        ecosystemName = "Development",
        ecosystemVersion = "2a196e0e-a8d8-4d4e-b545-dce57865dc20")
public class DeleteArea {

    private final RemoveAction removable;
    private static final Logger logger = LoggerFactory.getLogger(DeleteArea.class);

    @Inject
    public DeleteArea(RemoveAction removable) {
        this.removable = removable;
    }

    @CatalogRequest(
            id = "localDomainRequest_b202a8e6-245a-4c57-a3fd-cbaf6675c7dd",
            name = "Remove With Filters",
            description = "The request method deletes the specified resource with specified filters",
            area = "Delete",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> removeWithFilters(
            @Field(name = "URL", type = "Text") String uRL,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]") List<Map<String, Object>> queryParameters) {
        try {
            return removable.delete(uRL, queryParameters);
        } catch (Exception cause) {
            logger.error("Error while executing delete request with filter. :{}", cause.getMessage());
            throw new IllegalArgumentException("Error while executing delete request with filter." + cause.getMessage());
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_df4639ce-8fc3-4e21-91ff-344a0bcb2d5a",
            name = "Remove With Filters And Headers",
            description = "The request method deletes the specified resource with specified filters and headers",
            area = "Delete",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> removeWithFiltersAndHeaders(
            @Field(name = "URL", type = "Text") String uRL,
            @Field.Desc(name = "Query Parameters", type = "[ { key: Text, value: Text } ]") List<Map<String, Object>> queryParameters,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]") List<Map<String, Object>> headers) {
        try {
            return removable.delete(uRL, queryParameters, headers);
        } catch (Exception cause) {
            logger.error("Error while executing delete request with filters and headers: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("deleting resource with filters and headers", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_ac1abcb6-fb50-4eaa-b515-15de6262e69a",
            name = "Remove With Headers",
            description = "The request method deletes the specified resource with specified headers",
            area = "Delete",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false, attributes = {}, options = {})
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> removeWithHeaders(
            @Field(name = "URL", type = "Text", required = true, attributes = {}, options = {}) String uRL,
            @Field.Desc(name = "Headers", type = "[ { key: Text, value: Text } ]", required = true) List<Map<String, Object>> headers) {
        try {
            return removable.delete(uRL, Collections.emptyList(), headers);
        } catch (Exception cause) {
            logger.error("Error while executing delete request with headers: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("deleting resource with headers", cause.getMessage()));
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_97b01203-ebb9-40b0-9671-5996fc9a55fd",
            name = "Remove",
            description = "The request deletes the specified resource.",
            area = "Delete",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response Info", type = "FreeForm", required = false)
    @Field.Desc(name = "Response", type = "[ Composite ]", required = false)
    public Map<String, Object> remove(
            @Field(name = "URL", type = "Text") String uRL) {
        try {
            return removable.delete(uRL);
        } catch (Exception cause) {
            logger.error("Error while executing delete request: {}", cause.getMessage());
            throw new IllegalArgumentException(ErrorMessages.catalogRequestError("deleting resource", cause.getMessage()));
        }
    }

}
