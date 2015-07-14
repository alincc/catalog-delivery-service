package no.nb.microservices.delivery.microservice;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by andreasb on 14.07.15.
 */
@FeignClient("catalog-delivery-metadata-service")
public interface CatalogDeliveryMetadataService {

}
