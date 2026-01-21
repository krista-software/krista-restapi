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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ActionableImpl} class.
 * <p>
 * Tests focus on the static extractPaginatedResults method which handles
 * client-side pagination logic.
 * </p>
 */
class ActionableImplNewTest {

    // ========== Extract Paginated Results Tests ==========

    @Test
    void testExtractPaginatedResults_WithValidParameters_ShouldReturnCorrectSubset() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(100);
        Double pageSize = 10.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(10, result.size(), "Should return 10 results for page size 10");
    }

    @Test
    void testExtractPaginatedResults_WithSecondPage_ShouldReturnCorrectSubset() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(100);
        Double pageSize = 10.0;
        Double pageIndex = 1.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(10, result.size(), "Should return 10 results for page size 10");
    }

    @Test
    void testExtractPaginatedResults_WithLastPage_ShouldReturnRemainingResults() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(95);
        Double pageSize = 10.0;
        Double pageIndex = 9.0; // Last page

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(5, result.size(), "Should return 5 remaining results on last page");
    }

    @Test
    void testExtractPaginatedResults_WithPageIndexOutOfBounds_ShouldReturnEmptyList() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(50);
        Double pageSize = 10.0;
        Double pageIndex = 10.0; // Beyond available data

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return empty list for out of bounds page index");
    }

    @Test
    void testExtractPaginatedResults_WithNegativePageIndex_ShouldReturnEmptyList() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(50);
        Double pageSize = 10.0;
        Double pageIndex = -1.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return empty list for negative page index");
    }

    @Test
    void testExtractPaginatedResults_WithZeroPageSize_ShouldHandleGracefully() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(50);
        Double pageSize = 0.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return empty list for zero page size");
    }

    @Test
    void testExtractPaginatedResults_WithEmptyList_ShouldReturnEmptyList() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = Collections.emptyList();
        Double pageSize = 10.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return empty list for empty input");
    }

    @Test
    void testExtractPaginatedResults_WithSingleItem_ShouldReturnSingleItem() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(1);
        Double pageSize = 10.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should return single item");
    }

    @Test
    void testExtractPaginatedResults_WithPageSizeLargerThanTotal_ShouldReturnAllResults() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(5);
        Double pageSize = 10.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(5, result.size(), "Should return all 5 results");
    }

    @Test
    void testExtractPaginatedResults_WithLargeDataset_ShouldHandleEfficiently() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(10000);
        Double pageSize = 50.0;
        Double pageIndex = 100.0;

        // Act
        long startTime = System.currentTimeMillis();
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);
        long endTime = System.currentTimeMillis();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(50, result.size(), "Should return 50 results");
        assertTrue((endTime - startTime) < 100, "Should complete in less than 100ms");
    }

    @Test
    void testExtractPaginatedResults_WithDecimalPageSize_ShouldHandleCorrectly() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(100);
        Double pageSize = 10.5; // Decimal page size
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(10, result.size(), "Should handle decimal page size");
    }

    @Test
    void testExtractPaginatedResults_WithDecimalPageIndex_ShouldHandleCorrectly() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(100);
        Double pageSize = 10.0;
        Double pageIndex = 0.5; // Decimal page index

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Should handle decimal page index");
    }

    @Test
    void testExtractPaginatedResults_WithVeryLargePageSize_ShouldReturnAllResults() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(50);
        Double pageSize = 1000.0;
        Double pageIndex = 0.0;

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(50, result.size(), "Should return all results when page size exceeds total");
    }

    @Test
    void testExtractPaginatedResults_WithExactPageBoundary_ShouldReturnCorrectResults() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(100);
        Double pageSize = 10.0;
        Double pageIndex = 9.0; // Last complete page

        // Act
        ArrayList<LinkedHashMap<Object, Object>> result = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, pageIndex);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(10, result.size(), "Should return full page at boundary");
    }

    @Test
    void testExtractPaginatedResults_WithMultiplePages_ShouldMaintainOrder() {
        // Arrange
        List<LinkedHashMap<Object, Object>> allResults = createSampleResults(30);
        Double pageSize = 10.0;

        // Act - Get all three pages
        ArrayList<LinkedHashMap<Object, Object>> page1 = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, 0.0);
        ArrayList<LinkedHashMap<Object, Object>> page2 = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, 1.0);
        ArrayList<LinkedHashMap<Object, Object>> page3 = 
            ActionableImpl.extractPaginatedResults(allResults, pageSize, 2.0);

        // Assert
        assertEquals(10, page1.size(), "Page 1 should have 10 results");
        assertEquals(10, page2.size(), "Page 2 should have 10 results");
        assertEquals(10, page3.size(), "Page 3 should have 10 results");
        
        // Verify order is maintained
        assertNotEquals(page1.get(0), page2.get(0), "Pages should contain different results");
        assertNotEquals(page2.get(0), page3.get(0), "Pages should contain different results");
    }

    // ========== Helper Methods ==========

    private List<LinkedHashMap<Object, Object>> createSampleResults(int count) {
        List<LinkedHashMap<Object, Object>> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LinkedHashMap<Object, Object> item = new LinkedHashMap<>();
            item.put("id", i);
            item.put("name", "Item " + i);
            item.put("value", "Value " + i);
            results.add(item);
        }
        return results;
    }
}

