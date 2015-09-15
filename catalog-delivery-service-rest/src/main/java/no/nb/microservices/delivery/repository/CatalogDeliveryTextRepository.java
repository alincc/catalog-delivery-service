package no.nb.microservices.delivery.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by andreasb on 07.09.15.
 */
@FeignClient("catalog-delivery-text-service")
public interface CatalogDeliveryTextRepository {

    @RequestMapping(value = "/alto/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ByteArrayResource getAltos(@PathVariable("id") String id, @RequestParam("pages") String pages, @RequestParam("pageSelection") String pageSelection);

    @RequestMapping(value = "/text/{urn}", method = RequestMethod.GET)
    ByteArrayResource getText(@PathVariable("urn") String urn, @RequestParam("pages") String pages, @RequestParam("pageSelection") String pageSelection);
}
