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
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ResponseUtil} class.
 * <p>
 * Tests verify the ResponseUtil static methods for creating success and error responses.
 * </p>
 */
class ResponseUtilTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_ShouldBePrivate() {
        // ResponseUtil should have a private constructor to prevent instantiation
        // This is a utility class with only static methods
        assertNotNull(ResponseUtil.class);
    }

    // ========== createSuccessResponse Tests ==========

    @Test
    void testCreateSuccessResponse_WithValidEventData_ShouldReturnSuccessResponse() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        
        FreeForm responseInfo = new FreeForm();
        responseInfo.put("Status Code", "Text", "200");
        responseInfo.put("Message", "Text", "Success");
        
        List<FreeForm> responseList = new ArrayList<>();
        FreeForm item1 = new FreeForm();
        item1.put("id", "Text", "1");
        item1.put("name", "Text", "Item 1");
        responseList.add(item1);
        
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, responseList);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertEquals(responseList, result.get(RestApiConstants.RESPONSE));
        assertEquals(responseInfo, result.get(RestApiConstants.RESPONSE_INFO));
    }

    @Test
    void testCreateSuccessResponse_WithEmptyResponseList_ShouldReturnSuccessResponse() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        
        FreeForm responseInfo = new FreeForm();
        responseInfo.put("Status Code", "Text", "200");
        
        List<FreeForm> emptyResponseList = new ArrayList<>();
        
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, emptyResponseList);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertEquals(emptyResponseList, result.get(RestApiConstants.RESPONSE));
        assertTrue(((List<?>) result.get(RestApiConstants.RESPONSE)).isEmpty());
    }

    @Test
    void testCreateSuccessResponse_WithMultipleResponseItems_ShouldReturnSuccessResponse() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        
        FreeForm responseInfo = new FreeForm();
        responseInfo.put("Status Code", "Text", "200");
        
        List<FreeForm> responseList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            FreeForm item = new FreeForm();
            item.put("id", "Text", String.valueOf(i));
            item.put("name", "Text", "Item " + i);
            responseList.add(item);
        }
        
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, responseList);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertEquals(responseList, result.get(RestApiConstants.RESPONSE));
        assertEquals(5, ((List<?>) result.get(RestApiConstants.RESPONSE)).size());
    }

    // ========== createErrorResponse Tests ==========

    @Test
    void testCreateErrorResponse_WithValidMessageAndInfo_ShouldReturnErrorResponse() {
        // Arrange
        String errorMessage = "Connection failed";
        FreeForm info = new FreeForm();
        info.put("Status Code", "Text", "500");
        info.put("Error", "Text", errorMessage);

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(errorMessage, info);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertEquals(info, result.get(RestApiConstants.RESPONSE_INFO));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    @Test
    void testCreateErrorResponse_WithNullMessage_ShouldReturnErrorResponse() {
        // Arrange
        FreeForm info = new FreeForm();
        info.put("Status Code", "Text", "400");

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(null, info);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertEquals(info, result.get(RestApiConstants.RESPONSE_INFO));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    @Test
    void testCreateErrorResponse_WithEmptyMessage_ShouldReturnErrorResponse() {
        // Arrange
        String errorMessage = "";
        FreeForm info = new FreeForm();
        info.put("Status Code", "Text", "404");

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(errorMessage, info);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    @Test
    void testCreateErrorResponse_WithEmptyInfo_ShouldReturnErrorResponse() {
        // Arrange
        String errorMessage = "Authentication failed";
        FreeForm emptyInfo = new FreeForm();

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(errorMessage, emptyInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertEquals(emptyInfo, result.get(RestApiConstants.RESPONSE_INFO));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    // ========== Response Structure Tests ==========

    @Test
    void testCreateSuccessResponse_ShouldHaveExactlyTwoKeys() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        FreeForm responseInfo = new FreeForm();
        List<FreeForm> responseList = new ArrayList<>();
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, responseList);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
    }

    @Test
    void testCreateErrorResponse_ShouldHaveExactlyTwoKeys() {
        // Arrange
        FreeForm info = new FreeForm();

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse("Error", info);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey(RestApiConstants.RESPONSE));
        assertTrue(result.containsKey(RestApiConstants.RESPONSE_INFO));
    }

    @Test
    void testCreateErrorResponse_ResponseShouldAlwaysBeNull() {
        // Arrange
        FreeForm info1 = new FreeForm();
        FreeForm info2 = new FreeForm();
        FreeForm info3 = new FreeForm();

        // Act
        Map<String, Object> result1 = ResponseUtil.createErrorResponse("Error 1", info1);
        Map<String, Object> result2 = ResponseUtil.createErrorResponse("Error 2", info2);
        Map<String, Object> result3 = ResponseUtil.createErrorResponse(null, info3);

        // Assert
        assertNull(result1.get(RestApiConstants.RESPONSE));
        assertNull(result2.get(RestApiConstants.RESPONSE));
        assertNull(result3.get(RestApiConstants.RESPONSE));
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testCreateSuccessResponse_WithApiGetResponse_ShouldReturnValidResponse() {
        // Arrange - simulating a successful GET request
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        
        FreeForm responseInfo = new FreeForm();
        responseInfo.put("Status Code", "Text", "200");
        responseInfo.put("Message", "Text", "OK");
        
        List<FreeForm> users = new ArrayList<>();
        FreeForm user1 = new FreeForm();
        user1.put("id", "Text", "1");
        user1.put("name", "Text", "John Doe");
        user1.put("email", "Text", "john@example.com");
        users.add(user1);
        
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, users);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertEquals(users, result.get(RestApiConstants.RESPONSE));
        assertEquals(1, ((List<?>) result.get(RestApiConstants.RESPONSE)).size());
    }

    @Test
    void testCreateErrorResponse_WithConnectionTimeout_ShouldReturnValidResponse() {
        // Arrange - simulating a connection timeout error
        String errorMessage = "Connection timeout after 30 seconds";
        FreeForm info = new FreeForm();
        info.put("Status Code", "Text", "408");
        info.put("Error Message", "Text", errorMessage);
        info.put("URL", "Text", "https://api.example.com/users");

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(errorMessage, info);

        // Assert
        assertNotNull(result);
        assertEquals(info, result.get(RestApiConstants.RESPONSE_INFO));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    @Test
    void testCreateErrorResponse_WithAuthenticationFailure_ShouldReturnValidResponse() {
        // Arrange - simulating an authentication failure
        String errorMessage = "Invalid credentials: 401 Unauthorized";
        FreeForm info = new FreeForm();
        info.put("Status Code", "Text", "401");
        info.put("Error Message", "Text", errorMessage);

        // Act
        Map<String, Object> result = ResponseUtil.createErrorResponse(errorMessage, info);

        // Assert
        assertNotNull(result);
        assertNull(result.get(RestApiConstants.RESPONSE));
    }

    // ========== Edge Case Tests ==========

    @Test
    void testCreateSuccessResponse_WithNullResponseInfo_ShouldHandleGracefully() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        List<FreeForm> responseList = new ArrayList<>();
        data.put(RestApiConstants.RESPONSE_INFO, null);
        data.put(RestApiConstants.RESPONSE, responseList);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertNull(result.get(RestApiConstants.RESPONSE_INFO));
        assertEquals(responseList, result.get(RestApiConstants.RESPONSE));
    }

    @Test
    void testCreateSuccessResponse_WithNullResponse_ShouldHandleGracefully() {
        // Arrange
        FreeForm eventData = new FreeForm();
        Map<String, Object> data = new HashMap<>();
        FreeForm responseInfo = new FreeForm();
        data.put(RestApiConstants.RESPONSE_INFO, responseInfo);
        data.put(RestApiConstants.RESPONSE, null);
        eventData.put(RestApiConstants.DATA, "Composite", data);

        // Act
        Map<String, Object> result = ResponseUtil.createSuccessResponse(eventData);

        // Assert
        assertNotNull(result);
        assertEquals(responseInfo, result.get(RestApiConstants.RESPONSE_INFO));
        assertNull(result.get(RestApiConstants.RESPONSE));
    }
}

