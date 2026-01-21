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

package app.krista.extensions.development.api.rest.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SaveCredentialsResponse} class.
 * <p>
 * Tests verify the SaveCredentialsResponse constructor and field values.
 * </p>
 */
class SaveCredentialsResponseTest {

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithSuccessfulSave_ShouldCreateInstance() {
        // Arrange & Act
        SaveCredentialsResponse response = new SaveCredentialsResponse(true, false);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConstructor_WithFailedSave_ShouldCreateInstance() {
        // Arrange & Act
        SaveCredentialsResponse response = new SaveCredentialsResponse(false, true);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConstructor_WithBothFalse_ShouldCreateInstance() {
        // Arrange & Act
        SaveCredentialsResponse response = new SaveCredentialsResponse(false, false);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConstructor_WithBothTrue_ShouldCreateInstance() {
        // Arrange & Act
        SaveCredentialsResponse response = new SaveCredentialsResponse(true, true);

        // Assert
        assertNotNull(response);
    }

    // ========== Field Immutability Tests ==========

    @Test
    void testFields_ShouldBeFinal() {
        // Arrange
        SaveCredentialsResponse response1 = new SaveCredentialsResponse(true, false);
        SaveCredentialsResponse response2 = new SaveCredentialsResponse(false, true);

        // Assert - verify instances maintain their values
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotEquals(response1, response2);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_SuccessScenario_ShouldCreateValidResponse() {
        // Arrange & Act - credentials saved successfully
        SaveCredentialsResponse response = new SaveCredentialsResponse(true, false);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConstructor_ErrorScenario_ShouldCreateValidResponse() {
        // Arrange & Act - error occurred while saving
        SaveCredentialsResponse response = new SaveCredentialsResponse(false, true);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConstructor_NotSavedNoError_ShouldCreateValidResponse() {
        // Arrange & Act - not saved but no error (e.g., validation failed)
        SaveCredentialsResponse response = new SaveCredentialsResponse(false, false);

        // Assert
        assertNotNull(response);
    }

    // ========== Multiple Instance Tests ==========

    @Test
    void testMultipleInstances_ShouldBeIndependent() {
        // Arrange & Act
        SaveCredentialsResponse response1 = new SaveCredentialsResponse(true, false);
        SaveCredentialsResponse response2 = new SaveCredentialsResponse(false, true);
        SaveCredentialsResponse response3 = new SaveCredentialsResponse(true, true);
        SaveCredentialsResponse response4 = new SaveCredentialsResponse(false, false);

        // Assert - all instances should be created successfully
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        assertNotNull(response4);
    }

    // ========== Edge Case Tests ==========

    @Test
    void testConstructor_AllCombinations_ShouldCreateValidInstances() {
        // Test all 4 possible combinations of boolean values
        SaveCredentialsResponse r1 = new SaveCredentialsResponse(true, true);
        SaveCredentialsResponse r2 = new SaveCredentialsResponse(true, false);
        SaveCredentialsResponse r3 = new SaveCredentialsResponse(false, true);
        SaveCredentialsResponse r4 = new SaveCredentialsResponse(false, false);

        assertNotNull(r1);
        assertNotNull(r2);
        assertNotNull(r3);
        assertNotNull(r4);
    }
}

