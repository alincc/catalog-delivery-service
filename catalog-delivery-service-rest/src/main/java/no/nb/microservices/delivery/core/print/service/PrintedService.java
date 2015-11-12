package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

@Service
public class PrintedService implements IPrintedService {

    private static final Logger LOG = LoggerFactory.getLogger(PrintedService.class);

    private final PrintFormatFactory printFormatFactory;

    @Autowired
    public PrintedService(PrintFormatFactory printFormatFactory) {
        this.printFormatFactory = printFormatFactory;
    }

    @Override
    @Async
    public Future<CatalogFile> getResourceAsync(PrintedFile fileRequest) {
        CatalogFile catalogFile = getResource(fileRequest);
        AsyncResult<CatalogFile> asyncResult = new AsyncResult<CatalogFile>(catalogFile);
        if (catalogFile == null) {
            asyncResult.cancel(true);
        }

        return asyncResult;
    }

    @Override
    public CatalogFile getResource(PrintedFile fileRequest) {
        try {
            InputStream inputStream = printFormatFactory.getPrintFormat(fileRequest.getFormat()).getResource(fileRequest);
            return new CatalogFile(fileRequest.getFilename(), inputStream);
        } catch (IOException ioe) {
            LOG.error("Failed to get printed file", ioe);
            return null;
        }
    }
}
