package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.generic.DeliveryResource;
import no.nb.microservices.delivery.model.generic.FileRequest;
import no.nb.microservices.delivery.model.textual.TextualRequest;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface ITextualService {
    Future<DeliveryResource> getResourcesAsync(FileRequest fileRequest);
}
