package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.model.text.TextFormat;
import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.text.TextResource;
import no.nb.microservices.delivery.service.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class DirectTextController {

    private static final Logger LOG = LoggerFactory.getLogger(DirectTextController.class);

    private TextService textService;

    @Autowired
    public DirectTextController(TextService textService) {
        this.textService = textService;
    }

    @RequestMapping(value = "/download/text/{urn}", method = RequestMethod.GET)
    public void downloadTextResource(@PathVariable String urn,
                     @RequestParam(value = "pages", defaultValue = "all") String pages,
                     @RequestParam(value = "highQuality", defaultValue = "false") boolean highQuality,
                     HttpServletResponse response) throws IOException {
        TextRequest textRequest = new TextRequest() {{
            setUrn(urn);
            setFormat(TextFormat.PDF);
            setPages(pages);
            setQuality((highQuality) ? 8 : 4);
            setText(false);
        }};

        TextResource textResource = textService.getTextResource(textRequest);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + urn + ".pdf"); // TODO: Get metadata about the object and return title insted of urn as filename.
        response.getOutputStream().write(textResource.getContent().getByteArray());
    }
}
