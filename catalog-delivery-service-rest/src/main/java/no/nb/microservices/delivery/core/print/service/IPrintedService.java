package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.model.metadata.PrintedFile;

import java.util.concurrent.Future;

public interface IPrintedService {
    Future<CatalogFile> getResourceAsync(PrintedFile fileRequest);

    CatalogFile getResource(PrintedFile fileRequest);
}
