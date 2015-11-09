package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.metadata.model.PrintedFile;
import no.nb.microservices.delivery.core.order.model.CatalogFile;

import java.util.concurrent.Future;

public interface IPrintedService {
    Future<CatalogFile> getResourceAsync(PrintedFile fileRequest, String packageFormat);
    CatalogFile getResource(PrintedFile fileRequest, String packageFormat);
}
