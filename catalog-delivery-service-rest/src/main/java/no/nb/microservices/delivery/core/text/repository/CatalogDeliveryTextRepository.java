package no.nb.microservices.delivery.core.text.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;

/**
 * Created by andreasb on 07.09.15.
 */
@FeignClient("catalog-delivery-text-service")
public interface CatalogDeliveryTextRepository {

    @RequestMapping(value = "/alto/{urn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    InputStream getAltos(@PathVariable("urn") String urn,
                         @RequestParam("pages") String pages,
                         @RequestParam("pageSelection") String pageSelection,
                         @RequestParam("packageFormat") String packageFormat);

    @RequestMapping(value = "/text/{urn}", method = RequestMethod.GET)
    InputStream getText(@PathVariable("urn") String urn,
                              @RequestParam("pages") String pages,
                              @RequestParam("pageSelection") String pageSelection);
}