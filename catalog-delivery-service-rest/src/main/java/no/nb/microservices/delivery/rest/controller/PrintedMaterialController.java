package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialFormat;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialRequest;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import no.nb.microservices.delivery.service.ItemService;
import no.nb.microservices.delivery.service.PrintedMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class PrintedMaterialController {

    private static final Logger LOG = LoggerFactory.getLogger(PrintedMaterialController.class);

    private PrintedMaterialService printedMaterialService;
    private ItemService itemService;

    @Autowired
    public PrintedMaterialController(PrintedMaterialService printedMaterialService, ItemService itemService) {
        this.printedMaterialService = printedMaterialService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/printedMaterial/{urn}", method = RequestMethod.GET)
    public void downloadPrintedMaterialResource(@PathVariable String urn,
                                     @RequestParam(value = "pages", defaultValue = "all") String pages,
                                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                     HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {
        PrintedMaterialRequest printedMaterialRequest = new PrintedMaterialRequest() {{
            setUrn(urn);
            setFormat(PrintedMaterialFormat.PDF);
            setPages(pages);
            setQuality((highQuality) ? 8 : 4);
            setText(false);
        }};

        Future<PrintedMaterialResource> printedMaterialResourceFuture = printedMaterialService.getPrintedMaterialResourceAsync(printedMaterialRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(printedMaterialRequest.getUrn());
        PrintedMaterialResource printedMaterialResource = printedMaterialResourceFuture.get();
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".pdf");
        response.getOutputStream().write(printedMaterialResource.getContent().getByteArray());

        LOG.info("Printed material successfully downloaded: " + urn);
    }
}
