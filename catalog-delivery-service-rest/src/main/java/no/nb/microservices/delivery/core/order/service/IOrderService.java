package no.nb.microservices.delivery.core.order.service;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.request.OrderRequest;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IOrderService {
    Order placeOrder(OrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException, IOException;

    File getOrder(String key);

    @Async
    void processOrder(Order deliveryOrder);
}
