package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.textual.TextualResource;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class TextualService implements ITextualService {

    private final PdfGeneratorRepository pdfGeneratorRepository;

    @Autowired
    public TextualService(PdfGeneratorRepository pdfGeneratorRepository) {
        this.pdfGeneratorRepository = pdfGeneratorRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultResources")
    public Future<List<TextualResource>> getResourcesAsync(TextualRequest textualRequest) {
        return new AsyncResult<List<TextualResource>>() {
            @Override
            public List<TextualResource> invoke() {

                List<TextualResource> textualResources = new ArrayList<>();

                if (TextualFormat.PDF.equals(textualRequest.getFormat())) {
                    TextualResource textualResource = new TextualResource(textualRequest.getUrn(), textualRequest.getFormat(), getTextualAsPdf(textualRequest));
                    textualResources.add(textualResource);
                }
                else if (TextualFormat.EPUB.equals(textualRequest.getFormat())) {
                    throw new NotImplementedException("Format not implemented");
                }
                else if (textualRequest.isImages()) {
                    throw new NotImplementedException("Format not implemented");
                }
                else {
                    throw new IllegalArgumentException("Format is invalid in query");
                }

                return textualResources;
            }
        };
    }

    private ByteArrayResource getTextualAsPdf(TextualRequest textualRequest) {
        ByteArrayResource response = pdfGeneratorRepository.generate(Arrays.asList(textualRequest.getUrn()), null, "", false, null, "", "");
        return response;
    }

    private TextualResource getDefaultResources() {
        TextualResource textualResource = new TextualResource("urn", TextualFormat.PDF, null);
        return textualResource;
    }
}
