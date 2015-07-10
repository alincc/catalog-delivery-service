package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.order.ItemOrder;

/**
 * Created by andreasb on 10.07.15.
 */
public class OrderService implements IOrderService {
    @Override
    public void placeOrder(ItemOrder itemOrder) {
        // 1. Make async calls to get all text, video and audio resources and store them on disk.
        // 2. Wait for all calls to finish.
        // 3. Compress files into one file and store on disk.
        // 4. Generate a unique link with and store this in a database.
        // 5. Send email to customer with link.
    }
}
