package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.core.item.service.IItemService;
import no.nb.microservices.delivery.core.print.service.IPrintedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
@RequestMapping("/delivery")
public class PrintsController {

    private static final Logger LOG = LoggerFactory.getLogger(PrintsController.class);

    private final IPrintedService printedService;
    private final IItemService itemService;

    @Autowired
    public PrintsController(IPrintedService printedService, IItemService itemService) {
        this.printedService = printedService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/prints/{urn}", method = RequestMethod.GET)
    public void downloadTextualResource(@PathVariable String urn,
                                     @RequestParam(value = "pages", defaultValue = "") String pages,
                                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                     @RequestParam(value = "format", defaultValue = "pdf") String format,
                                     HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {

        // TODO: Implement direct download

//        PrintedResourceRequest textualRequest = new PrintedResourceRequest(urn, (highQuality) ? 6 : 5, pages);
//        PrintedFileRequest fileRequest = new PrintedFileRequest(format, Arrays.asList(textualRequest));
//
//        Future<OutputStream> printedFileFuture = printedService.getResourceAsync(fileRequest);
//        OutputStream printedFile = printedFileFuture.get();
//        String outputFilename = urn + "." + printedFile.getFileExtension();
//        String contentType = Files.probeContentType(new File(outputFilename).toPath());
//        response.setContentType(contentType);
//        response.setHeader("Content-Disposition", "attachment; filename=" + outputFilename);
//        response.getOutputStream().write(printedFile.getContent().getByteArray());
    }
}
