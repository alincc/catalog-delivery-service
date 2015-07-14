package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.text.TextResource;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface ITextService {
    Future<TextResource> getTextResourceAsync(TextRequest textRequest);
}
