package no.nb.microservices.delivery.core.order.service;

import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 10.07.15.
 */
public interface IOrderService {
    void placeOrder(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException, IOException;
    File getOrder(String key);

}
