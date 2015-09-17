package no.nb.microservices.delivery.core.metadata.repository;

import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
@FeignClient("catalog-delivery-metadata-service")
public interface DeliveryMetadataRepository {

    @RequestMapping(value = "/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    DeliveryOrder saveOrder(DeliveryOrder orderMetadata);

    @RequestMapping(value = "/orders/{value}", method = RequestMethod.GET)
    DeliveryOrder getOrderByIdOrKey(@PathVariable("value") String value);

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    List<DeliveryOrder> getOrders();
}