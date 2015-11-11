package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

@Service
public class PrintedService implements IPrintedService {

    private final PrintGeneratorRepository printGeneratorRepository;
    private final CatalogDeliveryTextRepository catalogDeliveryTextRepository;
    private final PrintFormatFactory printFormatFactory;

    @Autowired
    public PrintedService(PrintGeneratorRepository printGeneratorRepository, CatalogDeliveryTextRepository catalogDeliveryTextRepository, PrintFormatFactory printFormatFactory) {
        this.printGeneratorRepository = printGeneratorRepository;
        this.catalogDeliveryTextRepository = catalogDeliveryTextRepository;
        this.printFormatFactory = printFormatFactory;
    }

    @Override
    @Async
//    @HystrixCommand(fallbackMethod = "getDefaultResources",
//        commandProperties = {
//                @HystrixProperty(name="execution.timeout.enabled", value="600000")
//        })
    public Future<CatalogFile> getResourceAsync(PrintedFile fileRequest) {
//        return new AsyncResult<CatalogFile>() {
//            @Override
//            public CatalogFile invoke() {
//                return getResource(fileRequest, packageFormat);
//            }
//        };

        CatalogFile catalogFile = getResource(fileRequest);
        org.springframework.scheduling.annotation.AsyncResult<CatalogFile> as = new org.springframework.scheduling.annotation.AsyncResult<CatalogFile>(catalogFile);

        if (catalogFile == null) {
            as.cancel(true);
        }

        return as;
    }

    @Override
    public CatalogFile getResource(PrintedFile fileRequest) {
        try {
            InputStream inputStream = printFormatFactory.getPrintFormat(fileRequest.getFormat()).getResource(fileRequest);
            CatalogFile catalogFile = new CatalogFile(fileRequest.getFilename(), inputStream);
            return catalogFile;
        }
        catch (IOException ioe) {
            return null;
        }
    }

    private CatalogFile getDefaultResources(PrintedFile fileRequest) {
        CatalogFile catalogFile = new CatalogFile("empty." + fileRequest.getFormat().toString(), new ByteArrayInputStream(new byte[1]));
        return catalogFile;
    }
}
