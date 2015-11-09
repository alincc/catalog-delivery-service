package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.core.metadata.model.Order;
import no.nb.microservices.delivery.core.metadata.model.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDeliveryMetadataService {

    Order updateOrder(Order orderMetadata);

    Order saveOrder(Order orderMetadata);

    Order getOrderByIdOrKey(String value);

    List<Order> getOrdersByState(State state);

    Page<Order> getOrders(Pageable pageable);
}
