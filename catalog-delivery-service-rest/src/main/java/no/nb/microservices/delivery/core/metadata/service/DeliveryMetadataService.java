package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.core.metadata.repository.OrderRepository;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeliveryMetadataService implements IDeliveryMetadataService {

    private OrderRepository orderRepository;

    @Autowired
    public DeliveryMetadataService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order updateOrder(Order orderMetadata) {
        return orderRepository.save(orderMetadata);
    }

    @Override
    public Order saveOrder(Order orderMetadata) {
        return orderRepository.save(orderMetadata);
    }

    @Override
    public Order getOrderByIdOrKey(String value) {
        if (value.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) { // Is GUID
            return orderRepository.findOne(value);
        } else { // Is key
            return orderRepository.findByKey(value);
        }
    }

    @Override
    public Order getOrderByState(State state) {
        return orderRepository.findByState(state.toString());
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
