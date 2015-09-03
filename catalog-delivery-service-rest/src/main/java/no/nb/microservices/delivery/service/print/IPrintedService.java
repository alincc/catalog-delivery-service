package no.nb.microservices.delivery.service.print;

import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface IPrintedService {
    Future<PrintedFile> getResourceAsync(PrintedFileRequest fileRequest);
    PrintedFile getResource(PrintedFileRequest fileRequest);
}
