package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.model.text.TextFormat;
import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.text.TextResource;
import no.nb.microservices.delivery.service.ItemService;
import no.nb.microservices.delivery.service.TextService;
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
public class DirectTextController {

    private static final Logger LOG = LoggerFactory.getLogger(DirectTextController.class);

    private TextService textService;
    private ItemService itemService;

    @Autowired
    public DirectTextController(TextService textService, ItemService itemService) {
        this.textService = textService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/text/{urn}", method = RequestMethod.GET)
    public void downloadTextResource(@PathVariable String urn,
                                     @RequestParam(value = "pages", defaultValue = "all") String pages,
                                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                                     HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {
        TextRequest textRequest = new TextRequest() {{
            setUrn(urn);
            setFormat(TextFormat.PDF);
            setPages(pages);
            setQuality((highQuality) ? 8 : 4);
            setText(false);
        }};

        Future<TextResource> textResourceFuture = textService.getTextResourceAsync(textRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(textRequest.getUrn());
        TextResource textResource = textResourceFuture.get();
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".pdf");
        response.getOutputStream().write(textResource.getContent().getByteArray());
    }
}
