package no.nb.microservices.delivery.repository;

import no.nb.microservices.delivery.metadata.model.ItemMetadata;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
@FeignClient("catalog-delivery-metadata-service")
public interface DeliveryMetadataRepository {

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    ResponseEntity<OrderMetadata> saveOrder(OrderMetadata orderMetadata);

    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET)
    OrderMetadata getOrderById(@PathVariable("orderId") String orderId);

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    List<OrderMetadata> getOrders();

}