package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;

/**
 * Created by andreasb on 23.09.15.
 */
public interface FormatService {
    PrintedFile getResource(PrintedFileRequest fileRequest);
}
