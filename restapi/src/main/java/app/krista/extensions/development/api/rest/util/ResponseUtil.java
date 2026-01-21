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

import app.krista.model.base.FreeForm;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.krista.extensions.development.api.rest.util.RestApiConstants.*;

public class ResponseUtil {


    private ResponseUtil() {
    }


    public static @NotNull Map<String, Object> createSuccessResponse(FreeForm eventData) {
        Map<String, Object> data;
        FreeForm info;
        data = (Map<String, Object>) eventData.get(DATA);
        info = (FreeForm) data.get(RESPONSE_INFO);
        List<FreeForm> response = (List<FreeForm>) data.get(RESPONSE);
        Map<String, Object> responseDetails = new HashMap<>();
        responseDetails.put(RESPONSE, response);
        responseDetails.put(RESPONSE_INFO, info);
        return responseDetails;
    }

    public static @NotNull Map<String, Object> createErrorResponse(String message, FreeForm info) {
        Map<String, Object> response = new HashMap<>();
        response.put(RESPONSE_INFO, info);
        response.put(RESPONSE, null);
        return response;
    }
}
