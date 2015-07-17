package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.textual.TextualResource;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface ITextualService {
    Future<List<TextualResource>> getResourcesAsync(TextualRequest textualRequest);
}
