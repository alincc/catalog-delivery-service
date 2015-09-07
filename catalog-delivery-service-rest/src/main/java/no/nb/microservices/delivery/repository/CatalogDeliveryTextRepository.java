package no.nb.microservices.delivery.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by andreasb on 07.09.15.
 */
@FeignClient("catalog-delivery-text-service")
public interface CatalogDeliveryTextRepository {

    @RequestMapping(value = "/alto/{urn}", method = RequestMethod.GET)
    ByteArrayResource getAltos(@PathVariable String urn, @RequestParam String pages, @RequestParam String pageSelection);
}
