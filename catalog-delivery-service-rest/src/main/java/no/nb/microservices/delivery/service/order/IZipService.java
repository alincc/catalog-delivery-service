package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.model.generic.DeliveryResource;

import java.io.File;
import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
public interface IZipService {
    File zipIt(String outputZipPath, List<DeliveryResource> deliveryResources);
}
