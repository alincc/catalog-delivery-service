package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.metadata.model.PrintedFile;

import java.io.InputStream;

public interface FormatService {
    InputStream getResource(PrintedFile fileRequest, String packageFormat);
}
