package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.rest.assembler.PrintedFileBuilder;
import no.nb.microservices.delivery.core.print.service.IPrintedService;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.request.PrintFormat;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/delivery")
public class PrintsController {

    private static final Logger LOG = LoggerFactory.getLogger(PrintsController.class);

    private final IPrintedService printedService;

    @Autowired
    public PrintsController(IPrintedService printedService) {
        this.printedService = printedService;
    }

    @RequestMapping(value = "/download/prints/{urn}", method = RequestMethod.GET)
    public void downloadTextualResource(@PathVariable String urn,
                                        @RequestParam(value = "pages", defaultValue = "") String pages,
                                        @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                        @RequestParam(value = "format", defaultValue = "PDF") PrintFormat format,
                                        HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {

        PrintedFile request = new PrintedFileBuilder()
                .withFormat(format)
                .withQuality(highQuality)
                .addResource(urn, pages)
                .build();

        Future<CatalogFile> printedFileFuture = printedService.getResourceAsync(request);
        CatalogFile printedFile = printedFileFuture.get();
        String contentType = Files.probeContentType(new File(printedFile.getFilename()).toPath());
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=" + printedFile.getFilename());
        IOUtils.copy(printedFile.openInputStream(), response.getOutputStream());

        LOG.info("File downloaded with urn: " + urn + ", pages: " + pages + ", format: " + format);
    }
}
