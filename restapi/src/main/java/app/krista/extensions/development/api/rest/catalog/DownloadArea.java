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

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.development.api.rest.impl.DownloadAction;
import app.krista.extensions.development.api.rest.util.KristaMediaClient;
import app.krista.model.base.File;

import javax.inject.Inject;

@Domain(id = "catEntryDomain_d71d6d2e-f830-46bb-aa3d-9453b3de9efa",
        name = "API Integrations",
        ecosystemId = "catEntryEcosystem_954d3331-9431-48e5-bcf2-a51a5453b74f",
        ecosystemName = "Development",
        ecosystemVersion = "49833887-6dc1-493b-9c23-133250fc36a2")
public class DownloadArea {

    private final KristaMediaClient kristaMediaClient;

    @Inject
    public DownloadArea(KristaMediaClient kristaMediaClient) {
        this.kristaMediaClient = kristaMediaClient;
    }

    @CatalogRequest(
            id = "localDomainRequest_286c9a9b-bc2d-49ee-a991-2fa6456f44a0",
            name = "Get the file using download URL",
            description = "Get the file using the download URL",
            area = "Download",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.File(name = "File" , multipleFileUpload = false, required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public File getTheFileUsingDownloadURL(
            @Field.Text(name = "Download URL", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String downloadURL) {
        DownloadAction downloadAction = new DownloadAction(kristaMediaClient);
        return downloadAction.downloadFile(downloadURL);
    }

}
