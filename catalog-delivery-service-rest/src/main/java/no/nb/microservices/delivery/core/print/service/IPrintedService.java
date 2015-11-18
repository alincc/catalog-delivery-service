package no.nb.microservices.delivery.core.print.service;

import no.nb.commons.io.compression.model.CompressibleFile;
import no.nb.microservices.delivery.model.metadata.PrintedFile;

import java.util.concurrent.Future;

public interface IPrintedService {
    Future<CompressibleFile> getResourceAsync(PrintedFile fileRequest);

    CompressibleFile getResource(PrintedFile fileRequest);
}
