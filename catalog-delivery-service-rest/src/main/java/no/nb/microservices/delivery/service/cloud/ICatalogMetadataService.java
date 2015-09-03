package no.nb.microservices.delivery.service.cloud;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 25.08.15.
 */
public interface ICatalogMetadataService {
    Future<StructMap> getStructureAsync(String id);
    StructMap getStructure(String id);
}
