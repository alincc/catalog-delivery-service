package no.nb.microservices.delivery.core.scheduler;

import no.nb.microservices.delivery.core.metadata.service.IDeliveryMetadataService;
import no.nb.microservices.delivery.core.order.service.IOrderService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderScheduler {

    private final IDeliveryMetadataService deliveryMetadataService;
    private final IOrderService orderService;

    @Autowired
    public OrderScheduler(IDeliveryMetadataService deliveryMetadataService, IOrderService orderService) {
        this.deliveryMetadataService = deliveryMetadataService;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 5000)
    public void process() {
        Order order = deliveryMetadataService.getOrderByState(State.OPEN);

        if (order != null) {
            order.setState(State.PROCESSING);
            deliveryMetadataService.updateOrder(order);
            orderService.processOrder(order);
        }
    }
}
