package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.delivery.repository.DeliveryMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by andreasb on 15.07.15.
 */
@Service
public class DeliveryMetadataService implements IDeliveryMetadataService {

    private DeliveryMetadataRepository deliveryMetadataRepository;

    @Autowired
    public DeliveryMetadataService(DeliveryMetadataRepository deliveryMetadataRepository) {
        this.deliveryMetadataRepository = deliveryMetadataRepository;
    }


    @Override
    public OrderMetadata saveOrder(OrderMetadata orderMetadata) {
        ResponseEntity<OrderMetadata> responseEntity = deliveryMetadataRepository.saveOrder(orderMetadata);
        return responseEntity.getBody();
    }

    @Override
    public OrderMetadata getOrderById(String orderId) {
        return deliveryMetadataRepository.getOrderById(orderId);
    }

    @Override
    public List<OrderMetadata> getOrders() {
        return deliveryMetadataRepository.getOrders();
    }
}
