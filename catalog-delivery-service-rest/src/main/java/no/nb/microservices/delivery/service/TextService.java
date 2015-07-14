package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.model.text.TextFormat;
import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.text.TextResource;
import no.nb.microservices.delivery.microservice.CatalogPdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class TextService implements ITextService {

    private final CatalogPdfGeneratorService catalogPdfGeneratorService;

    @Autowired
    public TextService(CatalogPdfGeneratorService catalogPdfGeneratorService) {
        this.catalogPdfGeneratorService = catalogPdfGeneratorService;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultTextResource")
    public Future<TextResource> getTextResourceAsync(TextRequest textRequest) {
        return new AsyncResult<TextResource>() {
            @Override
            public TextResource invoke() {

                TextResource textResource;

                if (TextFormat.PDF.equals(textRequest.getFormat())) {
                    textResource = new TextResource(textRequest.getUrn(), textRequest.getFormat(), getTextAsPdf(textRequest));
                }
                else {
                    throw new IllegalArgumentException("Format is invalid in query");
                }

                return textResource;
            }
        };
    }

    private ByteArrayResource getTextAsPdf(TextRequest textRequest) {
        ByteArrayResource response = catalogPdfGeneratorService.generate(Arrays.asList(textRequest.getUrn()), null, "", false, null, "", "");
        return response;
    }

    private TextResource getDefaultTextResource() {
        TextResource textResource = new TextResource("urn", TextFormat.PDF, null);
        return textResource;
    }
}
