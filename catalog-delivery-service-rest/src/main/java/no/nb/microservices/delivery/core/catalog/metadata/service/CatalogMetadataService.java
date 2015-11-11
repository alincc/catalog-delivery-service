package no.nb.microservices.delivery.core.catalog.metadata.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import no.nb.microservices.delivery.core.catalog.metadata.repository.CatalogMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

public class CatalogMetadataService implements ICatalogMetadataService {

    @Autowired
    CatalogMetadataRepository catalogMetadataRepository;

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultStructure")
    public Future<StructMap> getStructureAsync(String id) {
        return new AsyncResult<StructMap>() {
            @Override
            public StructMap invoke() {
                return getStructure(id);
            }
        };
    }

    @Override
    public StructMap getStructure(String id) {
        return catalogMetadataRepository.getStructure(id).getBody();
    }

}
