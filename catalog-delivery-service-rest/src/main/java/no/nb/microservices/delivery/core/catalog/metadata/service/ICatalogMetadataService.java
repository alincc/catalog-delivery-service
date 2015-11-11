package no.nb.microservices.delivery.core.catalog.metadata.service;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;

import java.util.concurrent.Future;

public interface ICatalogMetadataService {
    Future<StructMap> getStructureAsync(String id);

    StructMap getStructure(String id);
}
