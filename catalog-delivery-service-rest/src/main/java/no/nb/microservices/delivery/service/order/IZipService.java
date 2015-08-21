package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.metadata.model.DeliveryFile;

import java.io.File;
import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
public interface IZipService {
    File zipIt(String outputZipPath, List<DeliveryFile> deliveryFiles);
}
