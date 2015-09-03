package no.nb.microservices.delivery.service.print;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.metadata.model.PrintedResource;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import no.nb.microservices.delivery.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.service.print.IPrintedService;
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
public class PrintedService implements IPrintedService {

    private final PrintGeneratorRepository printGeneratorRepository;

    @Autowired
    public PrintedService(PrintGeneratorRepository printGeneratorRepository) {
        this.printGeneratorRepository = printGeneratorRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultResources",
        commandProperties = {
                @HystrixProperty(name="execution.timeout.enabled", value="600000")
        })
    public Future<PrintedFile> getResourceAsync(PrintedFileRequest fileRequest) {
        return new AsyncResult<PrintedFile>() {
            @Override
            public PrintedFile invoke() {
                return getResource(fileRequest);
            }
        };
    }

    @Override
    public PrintedFile getResource(PrintedFileRequest fileRequest) {
        PrintedFile printedFile = new PrintedFile();
        printedFile.setResources(fileRequest.getResources().stream().map(q -> map(q)).collect(Collectors.toList()));

        List<PrintedResourceRequest> requests = fileRequest.getResources();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().filter(q -> q.getPages() != null).map(q -> q.getPages()).collect(Collectors.toList());
        List<String> pageSelections = requests.stream().filter(q -> q.getPageSelection() != null).map(q -> q.getPageSelection()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());
        List<Boolean> addTexts = requests.stream().map(q -> q.isText()).collect(Collectors.toList());

        List<String> graphicFormats = Arrays.asList("pdf", "jpg", "tif", "jp2");
        List<String> textFormats = Arrays.asList("alto", "txt");

        if (graphicFormats.contains(fileRequest.getFormat())) {
            ByteArrayResource response = printGeneratorRepository.generate(urns, pages, pageSelections, addTexts, quality, fileRequest.getFilename(), fileRequest.getFormat());
            printedFile.setFilename(fileRequest.getFilename() + "." + (fileRequest.getResources().size() > 1 ? "zip " : fileRequest.getFormat()));
            printedFile.setFormat(fileRequest.getFormat());
            printedFile.setFileSizeInBytes(response.contentLength());
            printedFile.setContent(response);
        }
        else  if (textFormats.contains(fileRequest.getFormat())) {

        }

        return printedFile;
    }

    private PrintedResource map(PrintedResourceRequest printedResourceRequest) {
        PrintedResource printedResource = new PrintedResource();
        printedResource.setPages(printedResourceRequest.getPages());
        printedResource.setQuality(printedResourceRequest.getQuality());
        printedResource.setUrn(printedResourceRequest.getUrn());

        return printedResource;
    }

    private PrintedFile getDefaultResources(PrintedFileRequest fileRequest) {
        PrintedFile deliveryResource = new PrintedFile();
        return deliveryResource;
    }
}
