package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by andreasb on 15.07.15.
 */
public interface IDeliveryMetadataService {

    OrderMetadata saveOrder(OrderMetadata orderMetadata);

    OrderMetadata getOrderById(String orderId);

    List<OrderMetadata> getOrders();
}
