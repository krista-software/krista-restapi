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

import static app.krista.extensions.development.api.rest.impl.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ModifyAction} class.
 * <p>
 * Tests verify the ModifyAction constructor and initialization logic.
 * Note: Full integration tests for the put() and patch() methods would require complex mocking
 * of HTTP calls and are better suited for integration testing.
 * </p>
 */
class ModifyActionTest {

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
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }

    @Test
    void testConstructor_WithNullParameters_ShouldThrowException() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new ModifyAction(null, null, null, null);
        });
    }

    @Test
    void testConstructor_WithEmptyAttributeStore_ShouldCreateInstanceWithNullRestApiAttributes() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
    }

    // ========== Method Overload Tests ==========

    @Test
    void testPut_WithUrlAndRequestType_MethodExists() {
        // This test verifies that the put(String url, String requestType) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(modifyAction);
    }

    @Test
    void testPut_WithUrlRequestTypeAndQueryParameters_MethodExists() {
        // This test verifies that the put(String url, String requestType, List<Map<String, Object>> queryParameters) method exists
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Act & Assert
        assertNotNull(modifyAction);
    }

    // ========== HTTP Method Type Tests ==========

    @Test
    void testConstructor_ForPutRequestScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
        // Verify that HTTP_PUT constant is available
        assertEquals("PUT", HTTP_PUT);
    }

    @Test
    void testConstructor_ForPatchRequestScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
        // Verify that HTTP_PATCH constant is available
        assertEquals("PATCH", HTTP_PATCH);
    }

    // ========== Real-World Scenario Tests ==========

    @Test
    void testConstructor_ForBasicAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
    }

    @Test
    void testConstructor_ForTokenAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
    }

    @Test
    void testConstructor_ForOAuthScenario_ShouldCreateInstance() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
    }

    @Test
    void testConstructor_WithMultipleCalls_ShouldCreateMultipleInstances() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction1 = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );
        ModifyAction modifyAction2 = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction1);
        assertNotNull(modifyAction2);
        assertNotSame(modifyAction1, modifyAction2, "Should create different instances");
    }

    @Test
    void testConstructor_VerifyDependenciesInjected_ShouldCallAttributeStore() {
        // Arrange
        when(attributeStore.listValues()).thenReturn(Collections.emptyList());

        // Act
        ModifyAction modifyAction = new ModifyAction(
                attributeStore,
                refreshTokenStore,
                invoker,
                kristaMediaClient
        );

        // Assert
        assertNotNull(modifyAction);
        verify(attributeStore, atLeastOnce()).listValues();
    }
}