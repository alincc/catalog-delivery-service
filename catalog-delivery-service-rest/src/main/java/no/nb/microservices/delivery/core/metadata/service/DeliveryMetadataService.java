package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.core.metadata.repository.DeliveryMetadataRepository;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DeliveryOrder saveOrder(DeliveryOrder orderMetadata) {
        return deliveryMetadataRepository.saveOrder(orderMetadata);
    }

    @Override
    public DeliveryOrder getOrderByIdOrKey(String value) {
        return deliveryMetadataRepository.getOrderByIdOrKey(value);
    }

    @Override
    public List<DeliveryOrder> getOrders() {
        return deliveryMetadataRepository.getOrders();
    }
}
