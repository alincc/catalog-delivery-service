package no.nb.microservices.delivery.core.catalog.metadata.repository;

import no.nb.microservices.catalogmetadata.model.struct.StructMap;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by andreasb on 25.08.15.
 */
@FeignClient
public interface CatalogMetadataRepository {
    @RequestMapping(value = "{id}/struct", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    ResponseEntity<StructMap> getStructure(@PathVariable String id);
}
