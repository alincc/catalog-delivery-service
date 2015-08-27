package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.service.IItemService;
import no.nb.microservices.delivery.service.ITextualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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

        TextualResourceRequest textualRequest = new TextualResourceRequest() {{
            setUrn(urn);
            setPages(pages);
            setQuality((highQuality) ? 6 : 5);
        }};

        TextualFileRequest fileRequest = new TextualFileRequest() {{
            setFormat("pdf");
            setText(true);
            setTextualResourceRequests(Arrays.asList(textualRequest));
        }};

        Future<TextualFile> textualResourceFuture = textualService.getResourceAsync(fileRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(textualRequest.getUrn());
        TextualFile textualResource = textualResourceFuture.get();
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".pdf");
        response.getOutputStream().write(textualResource.getContent().getByteArray());

        LOG.info("Printed material successfully downloaded: " + urn);
    }
}
