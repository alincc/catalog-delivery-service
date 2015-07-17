package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.textual.TextualResource;
import no.nb.microservices.delivery.service.ItemService;
import no.nb.microservices.delivery.service.TextualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class TextualController {

    private static final Logger LOG = LoggerFactory.getLogger(TextualController.class);

    private TextualService textualService;
    private ItemService itemService;

    @Autowired
    public TextualController(TextualService textualService, ItemService itemService) {
        this.textualService = textualService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/textual/{urn}", method = RequestMethod.GET)
    public void downloadTextualResource(@PathVariable String urn,
                                     @RequestParam(value = "pages", defaultValue = "all") String pages,
                                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                     HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {
        TextualRequest textualRequest = new TextualRequest() {{
            setUrn(urn);
            setFormat(TextualFormat.PDF);
            setPages(pages);
            setQuality((highQuality) ? 8 : 4);
            setText(false);
        }};

        Future<List<TextualResource>> textualResourceFuture = textualService.getResourcesAsync(textualRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(textualRequest.getUrn());
        TextualResource textualResource = textualResourceFuture.get().get(0);
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".pdf");
        response.getOutputStream().write(textualResource.getContent().getByteArray());

        LOG.info("Printed material successfully downloaded: " + urn);
    }
}
