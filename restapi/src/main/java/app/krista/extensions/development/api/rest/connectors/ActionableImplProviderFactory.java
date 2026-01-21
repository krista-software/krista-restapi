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

package app.krista.extensions.development.api.rest.connectors;

import app.krista.extension.executor.Invoker;
import app.krista.extensions.development.api.rest.RestApiAttributes;
import app.krista.extensions.development.api.rest.auth.AttributeStore;
import app.krista.extensions.development.api.rest.stores.RefreshTokenStore;
import app.krista.extensions.development.api.rest.stores.RestApiAttributeStore;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;

@Service
public class ActionableImplProviderFactory {

    private final RefreshTokenStore refreshTokenStore;
    private final AuthorizationContext authorizationContext;
    private final RequestContext requestContext;
    private final RestApiAttributeStore restApiAttributeStore;
    private final Invoker invoker;
    private final AttributeStore attributeStore;
    private final KristaMediaClient kristaMediaClient;

    @Inject
    public ActionableImplProviderFactory(RefreshTokenStore refreshTokenStore, RestApiAttributeStore restApiAttributeStore
            , RequestContext requestContext, AuthorizationContext authorizationContext, Invoker invoker, AttributeStore attributeStore, KristaMediaClient kristaMediaClient) {
        this.refreshTokenStore = refreshTokenStore;
        this.restApiAttributeStore = restApiAttributeStore;
        this.requestContext = requestContext;
        this.authorizationContext = authorizationContext;
        this.invoker = invoker;
        this.attributeStore = attributeStore;
        this.kristaMediaClient = kristaMediaClient;
    }

    public ActionableImplProvider create(RestApiAttributes restApiAttributes) throws IOException {
        String authContextId = restApiAttributeStore.save(restApiAttributes);
        return new ActionableImplProvider(restApiAttributes, refreshTokenStore, authorizationContext, requestContext, authContextId, invoker, attributeStore, kristaMediaClient);
    }

}
