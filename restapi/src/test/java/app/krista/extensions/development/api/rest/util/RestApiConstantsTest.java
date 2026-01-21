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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RestApiConstants} class.
 * <p>
 * Tests verify that all constants are properly defined and have expected values.
 * </p>
 */
class RestApiConstantsTest {

    // ========== Constant Value Tests ==========

    @Test
    void testDataConstant_ShouldHaveCorrectValue() {
        assertEquals("Data", RestApiConstants.DATA);
    }

    @Test
    void testResponseInfoConstant_ShouldHaveCorrectValue() {
        assertEquals("Response Info", RestApiConstants.RESPONSE_INFO);
    }

    @Test
    void testErrorMessageConstant_ShouldHaveCorrectValue() {
        assertEquals("Error Message", RestApiConstants.ERROR_MESSAGE);
    }

    @Test
    void testResponseConstant_ShouldHaveCorrectValue() {
        assertEquals("Response", RestApiConstants.RESPONSE);
    }

    @Test
    void testStatusMessageConstant_ShouldHaveCorrectValue() {
        assertEquals("Status and Message", RestApiConstants.STATUS_MESSAGE);
    }

    // ========== Null Validation Tests ==========

    @Test
    void testAllConstants_ShouldNotBeNull() {
        assertNotNull(RestApiConstants.DATA, "DATA should not be null");
        assertNotNull(RestApiConstants.RESPONSE_INFO, "RESPONSE_INFO should not be null");
        assertNotNull(RestApiConstants.ERROR_MESSAGE, "ERROR_MESSAGE should not be null");
        assertNotNull(RestApiConstants.RESPONSE, "RESPONSE should not be null");
        assertNotNull(RestApiConstants.STATUS_MESSAGE, "STATUS_MESSAGE should not be null");
    }

    // ========== Empty String Validation Tests ==========

    @Test
    void testAllConstants_ShouldNotBeEmpty() {
        assertFalse(RestApiConstants.DATA.isEmpty(), "DATA should not be empty");
        assertFalse(RestApiConstants.RESPONSE_INFO.isEmpty(), "RESPONSE_INFO should not be empty");
        assertFalse(RestApiConstants.ERROR_MESSAGE.isEmpty(), "ERROR_MESSAGE should not be empty");
        assertFalse(RestApiConstants.RESPONSE.isEmpty(), "RESPONSE should not be empty");
        assertFalse(RestApiConstants.STATUS_MESSAGE.isEmpty(), "STATUS_MESSAGE should not be empty");
    }

    // ========== Uniqueness Tests ==========

    @Test
    void testAllConstants_ShouldBeDistinct() {
        // Verify all constants are unique
        assertNotEquals(RestApiConstants.DATA, RestApiConstants.RESPONSE_INFO);
        assertNotEquals(RestApiConstants.DATA, RestApiConstants.ERROR_MESSAGE);
        assertNotEquals(RestApiConstants.DATA, RestApiConstants.RESPONSE);
        assertNotEquals(RestApiConstants.DATA, RestApiConstants.STATUS_MESSAGE);
        assertNotEquals(RestApiConstants.RESPONSE_INFO, RestApiConstants.ERROR_MESSAGE);
        assertNotEquals(RestApiConstants.RESPONSE_INFO, RestApiConstants.RESPONSE);
        assertNotEquals(RestApiConstants.RESPONSE_INFO, RestApiConstants.STATUS_MESSAGE);
        assertNotEquals(RestApiConstants.ERROR_MESSAGE, RestApiConstants.RESPONSE);
        assertNotEquals(RestApiConstants.ERROR_MESSAGE, RestApiConstants.STATUS_MESSAGE);
        assertNotEquals(RestApiConstants.RESPONSE, RestApiConstants.STATUS_MESSAGE);
    }

    // ========== String Length Tests ==========

    @Test
    void testAllConstants_ShouldHaveReasonableLength() {
        assertTrue(RestApiConstants.DATA.length() > 0 && RestApiConstants.DATA.length() < 50,
                "DATA should have reasonable length");
        assertTrue(RestApiConstants.RESPONSE_INFO.length() > 0 && RestApiConstants.RESPONSE_INFO.length() < 50,
                "RESPONSE_INFO should have reasonable length");
        assertTrue(RestApiConstants.ERROR_MESSAGE.length() > 0 && RestApiConstants.ERROR_MESSAGE.length() < 50,
                "ERROR_MESSAGE should have reasonable length");
        assertTrue(RestApiConstants.RESPONSE.length() > 0 && RestApiConstants.RESPONSE.length() < 50,
                "RESPONSE should have reasonable length");
        assertTrue(RestApiConstants.STATUS_MESSAGE.length() > 0 && RestApiConstants.STATUS_MESSAGE.length() < 50,
                "STATUS_MESSAGE should have reasonable length");
    }

    // ========== Immutability Tests ==========

    @Test
    void testConstants_ShouldBeImmutable() {
        // Verify constants maintain their values
        String originalData = RestApiConstants.DATA;
        String originalResponseInfo = RestApiConstants.RESPONSE_INFO;
        String originalErrorMessage = RestApiConstants.ERROR_MESSAGE;
        String originalResponse = RestApiConstants.RESPONSE;
        String originalStatusMessage = RestApiConstants.STATUS_MESSAGE;

        // Access constants multiple times
        assertEquals(originalData, RestApiConstants.DATA);
        assertEquals(originalResponseInfo, RestApiConstants.RESPONSE_INFO);
        assertEquals(originalErrorMessage, RestApiConstants.ERROR_MESSAGE);
        assertEquals(originalResponse, RestApiConstants.RESPONSE);
        assertEquals(originalStatusMessage, RestApiConstants.STATUS_MESSAGE);
    }

    // ========== Case Sensitivity Tests ==========

    @Test
    void testDataConstant_ShouldStartWithUpperCase() {
        assertTrue(Character.isUpperCase(RestApiConstants.DATA.charAt(0)),
                "DATA should start with uppercase letter");
    }

    @Test
    void testResponseInfoConstant_ShouldStartWithUpperCase() {
        assertTrue(Character.isUpperCase(RestApiConstants.RESPONSE_INFO.charAt(0)),
                "RESPONSE_INFO should start with uppercase letter");
    }

    @Test
    void testErrorMessageConstant_ShouldStartWithUpperCase() {
        assertTrue(Character.isUpperCase(RestApiConstants.ERROR_MESSAGE.charAt(0)),
                "ERROR_MESSAGE should start with uppercase letter");
    }

    @Test
    void testResponseConstant_ShouldStartWithUpperCase() {
        assertTrue(Character.isUpperCase(RestApiConstants.RESPONSE.charAt(0)),
                "RESPONSE should start with uppercase letter");
    }

    @Test
    void testStatusMessageConstant_ShouldStartWithUpperCase() {
        assertTrue(Character.isUpperCase(RestApiConstants.STATUS_MESSAGE.charAt(0)),
                "STATUS_MESSAGE should start with uppercase letter");
    }

    // ========== Content Validation Tests ==========

    @Test
    void testDataConstant_ShouldNotContainSpecialCharacters() {
        assertFalse(RestApiConstants.DATA.matches(".*[!@#$%^&*()_+=\\[\\]{}|;:'\",.<>?/\\\\].*"),
                "DATA should not contain special characters");
    }

    @Test
    void testResponseConstant_ShouldNotContainSpecialCharacters() {
        assertFalse(RestApiConstants.RESPONSE.matches(".*[!@#$%^&*()_+=\\[\\]{}|;:'\",.<>?/\\\\].*"),
                "RESPONSE should not contain special characters");
    }

    // ========== Whitespace Tests ==========

    @Test
    void testDataConstant_ShouldNotHaveLeadingOrTrailingWhitespace() {
        assertEquals(RestApiConstants.DATA.trim(), RestApiConstants.DATA,
                "DATA should not have leading or trailing whitespace");
    }

    @Test
    void testResponseInfoConstant_ShouldNotHaveLeadingOrTrailingWhitespace() {
        assertEquals(RestApiConstants.RESPONSE_INFO.trim(), RestApiConstants.RESPONSE_INFO,
                "RESPONSE_INFO should not have leading or trailing whitespace");
    }

    @Test
    void testErrorMessageConstant_ShouldNotHaveLeadingOrTrailingWhitespace() {
        assertEquals(RestApiConstants.ERROR_MESSAGE.trim(), RestApiConstants.ERROR_MESSAGE,
                "ERROR_MESSAGE should not have leading or trailing whitespace");
    }

    @Test
    void testResponseConstant_ShouldNotHaveLeadingOrTrailingWhitespace() {
        assertEquals(RestApiConstants.RESPONSE.trim(), RestApiConstants.RESPONSE,
                "RESPONSE should not have leading or trailing whitespace");
    }

    @Test
    void testStatusMessageConstant_ShouldNotHaveLeadingOrTrailingWhitespace() {
        assertEquals(RestApiConstants.STATUS_MESSAGE.trim(), RestApiConstants.STATUS_MESSAGE,
                "STATUS_MESSAGE should not have leading or trailing whitespace");
    }

    // ========== Usage Scenario Tests ==========

    @Test
    void testConstants_CanBeUsedAsMapKeys() {
        // Verify constants can be used as map keys
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put(RestApiConstants.DATA, "test data");
        map.put(RestApiConstants.RESPONSE_INFO, "test response info");
        map.put(RestApiConstants.ERROR_MESSAGE, "test error message");
        map.put(RestApiConstants.RESPONSE, "test response");
        map.put(RestApiConstants.STATUS_MESSAGE, "test status message");

        assertEquals("test data", map.get(RestApiConstants.DATA));
        assertEquals("test response info", map.get(RestApiConstants.RESPONSE_INFO));
        assertEquals("test error message", map.get(RestApiConstants.ERROR_MESSAGE));
        assertEquals("test response", map.get(RestApiConstants.RESPONSE));
        assertEquals("test status message", map.get(RestApiConstants.STATUS_MESSAGE));
    }

    @Test
    void testConstants_CanBeComparedWithEquals() {
        // Verify constants can be compared using equals
        assertTrue(RestApiConstants.DATA.equals("Data"));
        assertTrue(RestApiConstants.RESPONSE_INFO.equals("Response Info"));
        assertTrue(RestApiConstants.ERROR_MESSAGE.equals("Error Message"));
        assertTrue(RestApiConstants.RESPONSE.equals("Response"));
        assertTrue(RestApiConstants.STATUS_MESSAGE.equals("Status and Message"));
    }
}

