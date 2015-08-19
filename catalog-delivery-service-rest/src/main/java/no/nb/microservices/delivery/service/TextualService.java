package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.model.generic.DeliveryResource;
import no.nb.microservices.delivery.model.generic.FileRequest;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @HystrixCommand(fallbackMethod = "getDefaultResources",
        commandProperties = {
                @HystrixProperty(name="execution.timeout.enabled", value="600000")
        })
    public Future<DeliveryResource> getResourcesAsync(FileRequest fileRequest) {
        return new AsyncResult<DeliveryResource>() {
            @Override
            public DeliveryResource invoke() {

                DeliveryResource textualResources;
                List<TextualRequest> requests = fileRequest.getTextualRequests();
                List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
                List<String> pages = requests.stream().map(q -> q.getPages()).collect(Collectors.toList());
                List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());

                if (fileRequest.getFormat().equalsIgnoreCase("pdf")) {
                    ByteArrayResource response = pdfGeneratorRepository.generate(urns, pages, "book", fileRequest.isText(), quality, "", "");
                    textualResources = new DeliveryResource(
                            urns,
                            (fileRequest.getFilename() != null) ? fileRequest.getFilename() : "Collection",
                            "pdf",
                            response);
                }
                else if (TextualFormat.EPUB.equals(fileRequest.getFormat())) {
                    throw new NotImplementedException("Format not implemented");
                }
                else if (fileRequest.isImages()) {
                    throw new NotImplementedException("Format not implemented");
                }
                else {
                    throw new IllegalArgumentException("Format is invalid in query");
                }

                return textualResources;
            }
        };
    }

    private DeliveryResource getDefaultResources(FileRequest fileRequest) {
        DeliveryResource deliveryResource = new DeliveryResource(Arrays.asList("URN:NBN:no-nb_digibok_2010082422018"), "default", "pdf", null);
        return deliveryResource;
    }
}
