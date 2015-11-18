package no.nb.microservices.delivery.core.order.service;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.request.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.io.File;

public interface IOrderService {
    Order placeOrder(OrderRequest deliveryOrderRequest);

    File getOrder(String key);

    Page<Order> getOrders(Pageable pageable);

    @Async
    void processOrder(Order deliveryOrder);
}
