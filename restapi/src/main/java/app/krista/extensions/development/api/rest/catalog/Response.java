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

import app.krista.extensions.development.api.rest.impl.Constants;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class Response {

    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class);

    public static FreeForm create(Map<String, Object> responseMap) {
        FreeForm responseFreeform = new FreeForm();
        if (responseMap == null || responseMap.isEmpty()) {
            responseFreeform.put(Constants.STATUS_CODE, "Text", "Unknown");
            responseFreeform.put(Constants.MESSAGE, "Text", "Response is null or empty");
        } else {
            responseMap.forEach((key, value) -> responseFreeform.put(key, "Text", String.valueOf(value)));
        }
        return responseFreeform;
    }

    public static FreeForm create(Exception cause) {
        LOGGER.info("Error while reading data with filters. " + cause.getMessage(), cause);
        String stackTrace = getStackTraceString(cause);
        return Response.create(Map.of(Constants.STATUS_CODE, "Unknown", Constants.DATA, stackTrace, Constants.MESSAGE, cause.getMessage()));
    }

    private static String getStackTraceString(Exception cause) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        cause.printStackTrace(pw);
        String stackTrace = sw.toString();
        return stackTrace;
    }
}
