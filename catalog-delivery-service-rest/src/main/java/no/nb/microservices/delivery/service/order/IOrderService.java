package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.model.order.ItemOrder;

import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 10.07.15.
 */
public interface IOrderService {
    void placeOrder(ItemOrder itemOrder) throws InterruptedException, ExecutionException;
}
