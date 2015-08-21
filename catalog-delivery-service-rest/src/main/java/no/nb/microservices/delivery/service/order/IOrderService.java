package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 10.07.15.
 */
public interface IOrderService {
    void placeOrder(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException;
    public File getOrder(String key);

}
