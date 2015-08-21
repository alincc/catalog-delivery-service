package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface ITextualService {
    Future<TextualFile> getResourcesAsync(TextualFileRequest fileRequest);
}
