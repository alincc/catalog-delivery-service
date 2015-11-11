package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.core.metadata.repository.OrderRepository;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryMetadataService implements IDeliveryMetadataService {

    private OrderRepository orderRepository;

    @Autowired
    public DeliveryMetadataService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order updateOrder(Order orderMetadata) {
        Order savedOrder = orderRepository.save(orderMetadata);
        return savedOrder;
    }

    @Override
    public Order saveOrder(Order orderMetadata) {
        Order savedOrder = orderRepository.save(orderMetadata);
        return savedOrder;
    }

    @Override
    public Order getOrderByIdOrKey(String value) {
        if (value.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) { // Is GUID
            return orderRepository.findOne(value);
        }
        else { // Is key
            return orderRepository.findByKey(value);
        }
    }

    @Override
    public List<Order> getOrdersByState(State state) {
        return orderRepository.findByState(state.toString());
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        Page<Order> orderMetadatas = orderRepository.findAll(pageable);
        return orderMetadatas;
    }
}
