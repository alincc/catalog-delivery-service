package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.model.metadata.PrintedFile;

import java.io.IOException;
import java.io.InputStream;

public interface FormatService {
    InputStream getResource(PrintedFile fileRequest) throws IOException;
}
