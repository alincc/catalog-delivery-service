package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.metadata.model.PrintedFile;
import no.nb.microservices.delivery.core.metadata.model.PrintedResource;
import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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
    public Future<CatalogFile> getResourceAsync(PrintedFile fileRequest, String packageFormat) {
//        return new AsyncResult<CatalogFile>() {
//            @Override
//            public CatalogFile invoke() {
//                return getResource(fileRequest, packageFormat);
//            }
//        };

        return new org.springframework.scheduling.annotation.AsyncResult<CatalogFile>(getResource(fileRequest, packageFormat));
    }

    @Override
    public CatalogFile getResource(PrintedFile fileRequest, String packageFormat) {
        InputStream inputStream = printFormatFactory.getPrintFormat(fileRequest.getFormat()).getResource(fileRequest, packageFormat);
        CatalogFile catalogFile = new CatalogFile(fileRequest.getFilename(), inputStream);
        return catalogFile;
    }

    private PrintedResource map(PrintedResourceRequest printedResourceRequest) {
        PrintedResource printedResource = new PrintedResource();
        printedResource.setPages(printedResourceRequest.getPages());
        printedResource.setQuality(printedResourceRequest.getQuality());
        printedResource.setUrn(printedResourceRequest.getUrn());

        return printedResource;
    }

    private CatalogFile getDefaultResources(PrintedFile fileRequest, String packageFormat) {
        CatalogFile catalogFile = new CatalogFile("empty." + packageFormat, new ByteArrayInputStream(new byte[1]));
        return catalogFile;
    }
}
