package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialFormat;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialRequest;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class PrintedMaterialService implements IPrintedMaterialService {

    private final PdfGeneratorRepository pdfGeneratorRepository;

    @Autowired
    public PrintedMaterialService(PdfGeneratorRepository pdfGeneratorRepository) {
        this.pdfGeneratorRepository = pdfGeneratorRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultPrintedMaterialResources")
    public Future<List<PrintedMaterialResource>> getPrintedMaterialResourcesAsync(PrintedMaterialRequest printedMaterialRequest) {
        return new AsyncResult<List<PrintedMaterialResource>>() {
            @Override
            public List<PrintedMaterialResource> invoke() {

                List<PrintedMaterialResource> printedMaterialResources = new ArrayList<>();

                if (PrintedMaterialFormat.PDF.equals(printedMaterialRequest.getFormat())) {
                    PrintedMaterialResource printedMaterialResource = new PrintedMaterialResource(printedMaterialRequest.getUrn(), printedMaterialRequest.getFormat(), getPrintedMaterialAsPdf(printedMaterialRequest));
                    printedMaterialResources.add(printedMaterialResource);
                }
                else {
                    throw new IllegalArgumentException("Format is invalid in query");
                }

                return printedMaterialResources;
            }
        };
    }

    private ByteArrayResource getPrintedMaterialAsPdf(PrintedMaterialRequest printedMaterialRequest) {
        ByteArrayResource response = pdfGeneratorRepository.generate(Arrays.asList(printedMaterialRequest.getUrn()), null, "", false, null, "", "");
        return response;
    }

    private PrintedMaterialResource getDefaultPrintedMaterialResources() {
        PrintedMaterialResource printedMaterialResource = new PrintedMaterialResource("urn", PrintedMaterialFormat.PDF, null);
        return printedMaterialResource;
    }
}
