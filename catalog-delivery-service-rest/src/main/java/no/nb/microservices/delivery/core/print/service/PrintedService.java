package no.nb.microservices.delivery.core.print.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.metadata.model.PrintedResource;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 09.07.15.
 */
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
    @HystrixCommand(fallbackMethod = "getDefaultResources",
        commandProperties = {
                @HystrixProperty(name="execution.timeout.enabled", value="600000")
        })
    public Future<PrintedFile> getResourceAsync(PrintedFileRequest fileRequest) {
        return new AsyncResult<PrintedFile>() {
            @Override
            public PrintedFile invoke() {
                return getResource(fileRequest);
            }
        };
    }

    @Override
    public PrintedFile getResource(PrintedFileRequest fileRequest) {
        PrintedFile printedFile = printFormatFactory.getPrintFormat(fileRequest.getFormat()).getResource(fileRequest);
        printedFile.setFilename(fileRequest.getResources().get(0).getUrn());
        printedFile.setResources(fileRequest.getResources().stream().map(q -> map(q)).collect(Collectors.toList()));
        return printedFile;
    }

    private PrintedResource map(PrintedResourceRequest printedResourceRequest) {
        PrintedResource printedResource = new PrintedResource();
        printedResource.setPages(printedResourceRequest.getPages());
        printedResource.setQuality(printedResourceRequest.getQuality());
        printedResource.setUrn(printedResourceRequest.getUrn());

        return printedResource;
    }

    private PrintedFile getDefaultResources(PrintedFileRequest fileRequest) {
        PrintedFile deliveryResource = new PrintedFile();
        return deliveryResource;
    }
}
