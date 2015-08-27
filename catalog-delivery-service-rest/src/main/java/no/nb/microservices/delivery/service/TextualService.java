package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.metadata.model.TextualResource;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.repository.TextualGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class TextualService implements ITextualService {

    private final TextualGeneratorRepository textualGeneratorRepository;

    @Autowired
    public TextualService(TextualGeneratorRepository textualGeneratorRepository) {
        this.textualGeneratorRepository = textualGeneratorRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultResources",
        commandProperties = {
                @HystrixProperty(name="execution.timeout.enabled", value="600000")
        })
    public Future<TextualFile> getResourceAsync(TextualFileRequest fileRequest) {
        return new AsyncResult<TextualFile>() {
            @Override
            public TextualFile invoke() {
                return getResource(fileRequest);
            }
        };
    }

    @Override
    public TextualFile getResource(TextualFileRequest fileRequest) {
        TextualFile textualFile = new TextualFile();
        textualFile.setTextualResources(fileRequest.getResources().stream().map(q -> map(q)).collect(Collectors.toList()));

        List<TextualResourceRequest> requests = fileRequest.getResources();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().map(q -> q.getPages()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());

        List<String> graphicFormats = Arrays.asList("pdf", "jpg", "tif", "jp2");
        List<String> textFormats = Arrays.asList("alto", "txt");

        if (graphicFormats.contains(fileRequest.getFormat())) {
            ByteArrayResource response = textualGeneratorRepository.generate(urns, pages, fileRequest.getPageSelection(), fileRequest.isText(), quality, fileRequest.getFilename(), fileRequest.getFormat());
            textualFile.setFilename(fileRequest.getFilename() + "." + (fileRequest.isImages() ? "zip" : fileRequest.getFormat()));
            textualFile.setFormat(fileRequest.getFormat());
            textualFile.setFileSizeInBytes(response.contentLength());
            textualFile.setContent(response);
        }
        else  if (textFormats.contains(fileRequest.getFormat())) {

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
