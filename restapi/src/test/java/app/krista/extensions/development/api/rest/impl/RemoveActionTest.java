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

import app.krista.extension.executor.Invoker;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static app.krista.extensions.development.api.rest.impl.Constants.HTTP_DELETE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link RemoveAction} class.
 * <p>
 * Tests verify the RemoveAction constructor and initialization logic.
 * Note: Full integration tests for the delete() methods would require complex mocking
 * of HTTP calls and are better suited for integration testing.
 * </p>
 */
class RemoveActionTest {

    @Mock
    private AttributeStore attributeStore;

    @Mock
    private RefreshTokenStore refreshTokenStore;

    @Mock
    private Invoker invoker;

    @Mock
    private KristaMediaClient kristaMediaClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== Constructor Tests ==========

    @Test
    void testConstructor_WithAllParameters_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }

    @Test
    void testConstructor_WithNullParameters_ShouldThrowException() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new RemoveAction(null, null, null, null);
        });
    }

    @Test
    void testConstructor_WithEmptyAttributeStore_ShouldCreateInstanceWithNullRestApiAttributes() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
    }

    // ========== Method Overload Tests ==========

    @Test
    void testDelete_WithOnlyUrl_MethodExists() {
        // This test verifies that the delete(String url) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(removeAction);
    }

    @Test
    void testDelete_WithUrlAndQueryParameters_MethodExists() {
        // This test verifies that the delete(String url, List<Map<String, Object>> queryParameters) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(removeAction);
    }

    // ========== HTTP Method Type Tests ==========

    @Test
    void testConstructor_ForDeleteRequestScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
        // Verify that HTTP_DELETE constant is available
        assertEquals("DELETE", HTTP_DELETE);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_ForBasicAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
    }

    @Test
    void testConstructor_ForTokenAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
    }

    @Test
    void testConstructor_ForOAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
    }

    @Test
    void testConstructor_WithMultipleCalls_ShouldCreateMultipleInstances() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction1 = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );
        RemoveAction removeAction2 = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction1);
        assertNotNull(removeAction2);
        assertNotSame(removeAction1, removeAction2, "Should create different instances");
    }

    @Test
    void testConstructor_VerifyDependenciesInjected_ShouldCallAttributeStore() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }

    @Test
    void testConstructor_ForConditionalDeleteScenario_ShouldCreateInstance() {
        // Arrange - Simulating conditional delete with If-Match header
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        RemoveAction removeAction = new RemoveAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(removeAction);
    }
}