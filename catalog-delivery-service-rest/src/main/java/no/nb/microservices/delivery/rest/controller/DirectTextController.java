package no.nb.microservices.delivery.rest.controller;

import com.netflix.client.http.HttpResponse;
import no.nb.microservices.delivery.model.text.TextFormat;
import no.nb.microservices.delivery.model.text.TextQuery;
import no.nb.microservices.delivery.model.text.TextResource;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import no.nb.microservices.delivery.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class DirectTextController {

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
        TextQuery textQuery = new TextQuery() {{
            setUrn(urn);
            setFormat(TextFormat.PDF);
            setPages(pages);
            setQuality((highQuality) ? 8 : 4);
            setText(false);
        }};

        TextResource textResource = textService.getTextResource(textQuery);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + urn + ".pdf");
        response.getOutputStream().write(textResource.getContent().getByteArray());
    }
}
