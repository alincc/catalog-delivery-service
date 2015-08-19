package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.model.generic.DeliveryResource;
import no.nb.microservices.delivery.model.generic.FileRequest;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.service.IItemService;
import no.nb.microservices.delivery.service.ITextualService;
import no.nb.microservices.delivery.service.ItemService;
import no.nb.microservices.delivery.service.TextualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class TextualController {

    private static final Logger LOG = LoggerFactory.getLogger(TextualController.class);

    private final ITextualService textualService;
    private final IItemService itemService;

    @Autowired
    public TextualController(ITextualService textualService, IItemService itemService) {
        this.textualService = textualService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/textual/{urn}", method = RequestMethod.GET)
    public void downloadTextualResource(@PathVariable String urn,
                                     @RequestParam(value = "pages", defaultValue = "") String pages,
                                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                     HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {

        TextualRequest textualRequest = new TextualRequest() {{
            setUrn(urn);
            setPages(pages);
            setQuality((highQuality) ? 6 : 5);
        }};

        FileRequest fileRequest = new FileRequest() {{
            setFormat("pdf");
            setText(true);
            setTextualRequests(Arrays.asList(textualRequest));
        }};

        Future<DeliveryResource> textualResourceFuture = textualService.getResourcesAsync(fileRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(textualRequest.getUrn());
        DeliveryResource textualResource = textualResourceFuture.get();
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".pdf");
        response.getOutputStream().write(textualResource.getContent().getByteArray());

        LOG.info("Printed material successfully downloaded: " + urn);
    }
}
