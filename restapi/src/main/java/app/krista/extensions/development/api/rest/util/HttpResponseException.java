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

import java.io.IOException;
import java.io.Serial;

/**
 * Exception thrown when an HTTP request receives an error response from the server.
 * <p>
 * This exception is used to distinguish between network-level errors (connection refused,
 * timeout, etc.) and HTTP-level errors (4xx, 5xx responses). This allows for proper
 * error categorization and user-friendly error messages.
 * </p>
 *
 * @see ApiExceptionHandler
 */
public class HttpResponseException extends IOException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String statusMessage;
    private final String responseBody;

    /**
     * Constructs a new HttpResponseException with the specified details.
     *
     * @param statusCode    the HTTP status code (e.g., 404, 500)
     * @param statusMessage the HTTP status message (e.g., "Not Found", "Internal Server Error")
     * @param responseBody  the response body from the server (may contain error details)
     * @param message       the detail message for this exception
     */
    public HttpResponseException(int statusCode, String statusMessage, String responseBody, String message) {
        super(message);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseBody = responseBody;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return the status code (e.g., 404, 500)
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the HTTP status message.
     *
     * @return the status message (e.g., "Not Found", "Internal Server Error")
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Returns the response body from the server.
     *
     * @return the response body, may be null or empty
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Checks if this is a client error (4xx status code).
     *
     * @return true if status code is between 400 and 499
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Checks if this is a server error (5xx status code).
     *
     * @return true if status code is between 500 and 599
     */
    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * Checks if this is an authentication error (401 Unauthorized).
     *
     * @return true if status code is 401
     */
    public boolean isUnauthorized() {
        return statusCode == 401;
    }

    /**
     * Checks if this is a forbidden error (403 Forbidden).
     *
     * @return true if status code is 403
     */
    public boolean isForbidden() {
        return statusCode == 403;
    }

    /**
     * Checks if this is a not found error (404 Not Found).
     *
     * @return true if status code is 404
     */
    public boolean isNotFound() {
        return statusCode == 404;
    }
}

