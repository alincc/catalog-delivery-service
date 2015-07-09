package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.text.TextQuery;
import no.nb.microservices.delivery.model.text.TextResource;

/**
 * Created by andreasb on 09.07.15.
 */
public interface ITextService {
    TextResource getTextResource(TextQuery textQuery);
}
