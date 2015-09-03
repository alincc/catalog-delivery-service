package no.nb.microservices.delivery.service.cloud;

import no.nb.microservices.delivery.metadata.model.DeliveryOrder;

import java.util.List;

/**
 * Created by andreasb on 15.07.15.
 */
public interface IDeliveryMetadataService {

    DeliveryOrder saveOrder(DeliveryOrder orderMetadata);

    DeliveryOrder getOrderByIdOrKey(String value);

    List<DeliveryOrder> getOrders();
}
