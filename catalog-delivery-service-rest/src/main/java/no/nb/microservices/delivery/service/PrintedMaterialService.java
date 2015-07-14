package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialFormat;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialRequest;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import no.nb.microservices.delivery.microservice.CatalogPdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class PrintedMaterialService implements IPrintedMaterialService {

    private final CatalogPdfGeneratorService catalogPdfGeneratorService;

    @Autowired
    public PrintedMaterialService(CatalogPdfGeneratorService catalogPdfGeneratorService) {
        this.catalogPdfGeneratorService = catalogPdfGeneratorService;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultPrintedMaterialResource")
    public Future<PrintedMaterialResource> getPrintedMaterialResourceAsync(PrintedMaterialRequest printedMaterialRequest) {
        return new AsyncResult<PrintedMaterialResource>() {
            @Override
            public PrintedMaterialResource invoke() {

                PrintedMaterialResource printedMaterialResource;

                if (PrintedMaterialFormat.PDF.equals(printedMaterialRequest.getFormat())) {
                    printedMaterialResource = new PrintedMaterialResource(printedMaterialRequest.getUrn(), printedMaterialRequest.getFormat(), getPrintedMaterialAsPdf(printedMaterialRequest));
                }
                else {
                    throw new IllegalArgumentException("Format is invalid in query");
                }

                return printedMaterialResource;
            }
        };
    }

    private ByteArrayResource getPrintedMaterialAsPdf(PrintedMaterialRequest printedMaterialRequest) {
        ByteArrayResource response = catalogPdfGeneratorService.generate(Arrays.asList(printedMaterialRequest.getUrn()), null, "", false, null, "", "");
        return response;
    }

    private PrintedMaterialResource getDefaultPrintedMaterialResource() {
        PrintedMaterialResource printedMaterialResource = new PrintedMaterialResource("urn", PrintedMaterialFormat.PDF, null);
        return printedMaterialResource;
    }
}
