package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.metadata.model.TextualResource;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;
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
    public Future<TextualFile> getResourcesAsync(TextualFileRequest fileRequest) {
        return new AsyncResult<TextualFile>() {
            @Override
            public TextualFile invoke() {
                return getResource(fileRequest);
            }
        };
    }

    public TextualFile getResource(TextualFileRequest fileRequest) {
        TextualFile textualFile = new TextualFile();
        textualFile.setFilename((fileRequest.getFilename() != null) ? fileRequest.getFilename() : "Collection");
        textualFile.setTextualResources(fileRequest.getTextualResourceRequests().stream().map(q -> map(q)).collect(Collectors.toList()));

        List<TextualResourceRequest> requests = fileRequest.getTextualResourceRequests();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().map(q -> q.getPages()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());

        if (fileRequest.getFormat().equalsIgnoreCase("pdf")) {
            ByteArrayResource response = pdfGeneratorRepository.generate(urns, pages, "book", fileRequest.isText(), quality, "", "");
            textualFile.setContent(response);
            textualFile.setFileSizeInBytes(response.contentLength());
            textualFile.setExtension("pdf");
            textualFile.setHasText(fileRequest.isText());
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

        return textualFile;
    }

    private TextualResource map(TextualResourceRequest textualResourceRequest) {
        TextualResource textualResource = new TextualResource();
        textualResource.setPages(textualResourceRequest.getPages());
        textualResource.setQuality(textualResourceRequest.getQuality());
        textualResource.setUrn(textualResourceRequest.getUrn());

        return textualResource;
    }

    private TextualFile getDefaultResources(TextualFileRequest fileRequest) {
        TextualFile deliveryResource = new TextualFile();
        return deliveryResource;
    }
}
