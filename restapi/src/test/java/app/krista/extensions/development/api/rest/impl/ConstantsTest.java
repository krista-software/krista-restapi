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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Constants} class.
 * <p>
 * Tests verify that all constants are properly defined and have expected values.
 * </p>
 */
class ConstantsTest {

    // ========== Field Name Constants Tests ==========

    @Test
    void testEmailConstant_ShouldHaveCorrectValue() {
        assertEquals("Email", Constants.EMAIL);
    }

    @Test
    void testClientIdConstant_ShouldHaveCorrectValue() {
        assertEquals("Client Id", Constants.CLIENT_ID);
    }

    @Test
    void testClientSecretConstant_ShouldHaveCorrectValue() {
        assertEquals("Client Secret", Constants.CLIENT_SECRET);
    }

    @Test
    void testAuthUrlConstant_ShouldHaveCorrectValue() {
        assertEquals("Auth Url", Constants.AUTH_URL);
    }

    @Test
    void testStateConstant_ShouldHaveCorrectValue() {
        assertEquals("State", Constants.STATE);
    }

    @Test
    void testScopeConstant_ShouldHaveCorrectValue() {
        assertEquals("Scope", Constants.SCOPE);
    }

    @Test
    void testApiUrlConstant_ShouldHaveCorrectValue() {
        assertEquals("Api Url", Constants.API_URL);
    }

    @Test
    void testAuthTypeConstant_ShouldHaveCorrectValue() {
        assertEquals("Auth Type", Constants.AUTH_TYPE);
    }

    @Test
    void testAccessTokenUrlConstant_ShouldHaveCorrectValue() {
        assertEquals("Access Token Url", Constants.ACCESS_TOKEN_URL);
    }

    @Test
    void testUsernameConstant_ShouldHaveCorrectValue() {
        assertEquals("Username", Constants.USERNAME);
    }

    @Test
    void testTokenConstant_ShouldHaveCorrectValue() {
        assertEquals("token", Constants.TOKEN);
    }

    @Test
    void testTokenTypeConstant_ShouldHaveCorrectValue() {
        assertEquals("Token Type", Constants.TOKEN_TYPE);
    }

    @Test
    void testPasswordConstant_ShouldHaveCorrectValue() {
        assertEquals("password", Constants.PASSWORD);
    }

    @Test
    void testInvalidAuthTypeConstant_ShouldHaveCorrectValue() {
        assertEquals("Invalid auth type", Constants.INVALID_AUTH_TYPE);
    }

    // ========== HTTP Method Constants Tests ==========

    @Test
    void testHttpGetConstant_ShouldHaveCorrectValue() {
        assertEquals("GET", Constants.HTTP_GET);
    }

    @Test
    void testHttpPutConstant_ShouldHaveCorrectValue() {
        assertEquals("PUT", Constants.HTTP_PUT);
    }

    @Test
    void testHttpPatchConstant_ShouldHaveCorrectValue() {
        assertEquals("PATCH", Constants.HTTP_PATCH);
    }

    @Test
    void testHttpPostConstant_ShouldHaveCorrectValue() {
        assertEquals("POST", Constants.HTTP_POST);
    }

    @Test
    void testHttpDeleteConstant_ShouldHaveCorrectValue() {
        assertEquals("DELETE", Constants.HTTP_DELETE);
    }

    // ========== Response Key Constants Tests ==========

    @Test
    void testStatusCodeConstant_ShouldHaveCorrectValue() {
        assertEquals("status", Constants.STATUS_CODE);
    }

    @Test
    void testMessageConstant_ShouldHaveCorrectValue() {
        assertEquals("message", Constants.MESSAGE);
    }

    @Test
    void testDataConstant_ShouldHaveCorrectValue() {
        assertEquals("data", Constants.DATA);
    }

    // ========== Media Type Tests ==========

    @Test
    void testJsonMimeType_ShouldNotBeNull() {
        assertNotNull(Constants.JSON_MIME_TYPE, "JSON MIME type should not be null");
    }

    @Test
    void testJsonMimeType_ShouldHaveCorrectValue() {
        assertEquals("application/json", Constants.JSON_MIME_TYPE.toString());
    }

    // ========== GSON Mapper Tests ==========

    @Test
    void testGsonJsonMapper_ShouldNotBeNull() {
        assertNotNull(Constants.GSON_JSON_MAPPER, "GSON JSON mapper should not be null");
    }

    @Test
    void testGsonJsonMapper_ShouldBeUsable() {
        // Test that the mapper can serialize and deserialize
        String json = Constants.GSON_JSON_MAPPER.toString("test");
        assertNotNull(json, "Serialized JSON should not be null");
    }

    // ========== Text Field Tests ==========

    @Test
    void testTextField_ShouldNotBeNull() {
        assertNotNull(Constants.textField, "Text field should not be null");
    }

    // ========== HTTP Method Validation Tests ==========

    @Test
    void testAllHttpMethods_ShouldBeUpperCase() {
        assertTrue(Constants.HTTP_GET.equals(Constants.HTTP_GET.toUpperCase()), "HTTP_GET should be uppercase");
        assertTrue(Constants.HTTP_PUT.equals(Constants.HTTP_PUT.toUpperCase()), "HTTP_PUT should be uppercase");
        assertTrue(Constants.HTTP_PATCH.equals(Constants.HTTP_PATCH.toUpperCase()), "HTTP_PATCH should be uppercase");
        assertTrue(Constants.HTTP_POST.equals(Constants.HTTP_POST.toUpperCase()), "HTTP_POST should be uppercase");
        assertTrue(Constants.HTTP_DELETE.equals(Constants.HTTP_DELETE.toUpperCase()), "HTTP_DELETE should be uppercase");
    }

    @Test
    void testAllHttpMethods_ShouldNotBeEmpty() {
        assertFalse(Constants.HTTP_GET.isEmpty(), "HTTP_GET should not be empty");
        assertFalse(Constants.HTTP_PUT.isEmpty(), "HTTP_PUT should not be empty");
        assertFalse(Constants.HTTP_PATCH.isEmpty(), "HTTP_PATCH should not be empty");
        assertFalse(Constants.HTTP_POST.isEmpty(), "HTTP_POST should not be empty");
        assertFalse(Constants.HTTP_DELETE.isEmpty(), "HTTP_DELETE should not be empty");
    }

    // ========== Field Name Validation Tests ==========

    @Test
    void testAllFieldNames_ShouldNotBeNull() {
        assertNotNull(Constants.EMAIL, "EMAIL should not be null");
        assertNotNull(Constants.CLIENT_ID, "CLIENT_ID should not be null");
        assertNotNull(Constants.CLIENT_SECRET, "CLIENT_SECRET should not be null");
        assertNotNull(Constants.AUTH_URL, "AUTH_URL should not be null");
        assertNotNull(Constants.STATE, "STATE should not be null");
        assertNotNull(Constants.SCOPE, "SCOPE should not be null");
        assertNotNull(Constants.API_URL, "API_URL should not be null");
        assertNotNull(Constants.AUTH_TYPE, "AUTH_TYPE should not be null");
        assertNotNull(Constants.ACCESS_TOKEN_URL, "ACCESS_TOKEN_URL should not be null");
        assertNotNull(Constants.USERNAME, "USERNAME should not be null");
        assertNotNull(Constants.TOKEN, "TOKEN should not be null");
        assertNotNull(Constants.TOKEN_TYPE, "TOKEN_TYPE should not be null");
        assertNotNull(Constants.PASSWORD, "PASSWORD should not be null");
    }

    @Test
    void testAllFieldNames_ShouldNotBeEmpty() {
        assertFalse(Constants.EMAIL.isEmpty(), "EMAIL should not be empty");
        assertFalse(Constants.CLIENT_ID.isEmpty(), "CLIENT_ID should not be empty");
        assertFalse(Constants.CLIENT_SECRET.isEmpty(), "CLIENT_SECRET should not be empty");
        assertFalse(Constants.AUTH_URL.isEmpty(), "AUTH_URL should not be empty");
        assertFalse(Constants.STATE.isEmpty(), "STATE should not be empty");
        assertFalse(Constants.SCOPE.isEmpty(), "SCOPE should not be empty");
        assertFalse(Constants.API_URL.isEmpty(), "API_URL should not be empty");
        assertFalse(Constants.AUTH_TYPE.isEmpty(), "AUTH_TYPE should not be empty");
        assertFalse(Constants.ACCESS_TOKEN_URL.isEmpty(), "ACCESS_TOKEN_URL should not be empty");
        assertFalse(Constants.USERNAME.isEmpty(), "USERNAME should not be empty");
        assertFalse(Constants.TOKEN.isEmpty(), "TOKEN should not be empty");
        assertFalse(Constants.TOKEN_TYPE.isEmpty(), "TOKEN_TYPE should not be empty");
        assertFalse(Constants.PASSWORD.isEmpty(), "PASSWORD should not be empty");
    }

    // ========== Response Key Validation Tests ==========

    @Test
    void testAllResponseKeys_ShouldNotBeNull() {
        assertNotNull(Constants.STATUS_CODE, "STATUS_CODE should not be null");
        assertNotNull(Constants.MESSAGE, "MESSAGE should not be null");
        assertNotNull(Constants.DATA, "DATA should not be null");
    }

    @Test
    void testAllResponseKeys_ShouldNotBeEmpty() {
        assertFalse(Constants.STATUS_CODE.isEmpty(), "STATUS_CODE should not be empty");
        assertFalse(Constants.MESSAGE.isEmpty(), "MESSAGE should not be empty");
        assertFalse(Constants.DATA.isEmpty(), "DATA should not be empty");
    }

    // ========== Consistency Tests ==========

    @Test
    void testHttpMethods_ShouldBeDistinct() {
        // Verify all HTTP methods are unique
        assertNotEquals(Constants.HTTP_GET, Constants.HTTP_POST);
        assertNotEquals(Constants.HTTP_GET, Constants.HTTP_PUT);
        assertNotEquals(Constants.HTTP_GET, Constants.HTTP_PATCH);
        assertNotEquals(Constants.HTTP_GET, Constants.HTTP_DELETE);
        assertNotEquals(Constants.HTTP_POST, Constants.HTTP_PUT);
        assertNotEquals(Constants.HTTP_POST, Constants.HTTP_PATCH);
        assertNotEquals(Constants.HTTP_POST, Constants.HTTP_DELETE);
        assertNotEquals(Constants.HTTP_PUT, Constants.HTTP_PATCH);
        assertNotEquals(Constants.HTTP_PUT, Constants.HTTP_DELETE);
        assertNotEquals(Constants.HTTP_PATCH, Constants.HTTP_DELETE);
    }

    @Test
    void testResponseKeys_ShouldBeDistinct() {
        // Verify all response keys are unique
        assertNotEquals(Constants.STATUS_CODE, Constants.MESSAGE);
        assertNotEquals(Constants.STATUS_CODE, Constants.DATA);
        assertNotEquals(Constants.MESSAGE, Constants.DATA);
    }
}

