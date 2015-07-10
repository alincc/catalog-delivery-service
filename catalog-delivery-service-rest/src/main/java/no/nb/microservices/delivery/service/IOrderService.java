package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.order.ItemOrder;

/**
 * Created by andreasb on 10.07.15.
 */
public interface IOrderService {
    void placeOrder(ItemOrder itemOrder);
}
