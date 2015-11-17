package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDeliveryMetadataService {

    Order updateOrder(Order orderMetadata);

    Order saveOrder(Order orderMetadata);

    Order getOrderByIdOrKey(String value);

    Order getOrderByState(State state);

    Page<Order> getOrders(Pageable pageable);
}
