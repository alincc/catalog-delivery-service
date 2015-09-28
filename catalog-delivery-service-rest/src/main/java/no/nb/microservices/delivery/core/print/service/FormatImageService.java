package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 23.09.15.
 */
@Service
public class FormatImageService implements FormatService {

    private final PrintGeneratorRepository printGeneratorRepository;

    @Autowired
    public FormatImageService(PrintGeneratorRepository printGeneratorRepository) {
        this.printGeneratorRepository = printGeneratorRepository;
    }

    @Override
    public PrintedFile getResource(PrintedFileRequest fileRequest) {
        PrintedFile printedFile = new PrintedFile();

        List<PrintedResourceRequest> requests = fileRequest.getResources();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().filter(q -> q.getPages() != null).map(q -> q.getPages()).collect(Collectors.toList());
        List<String> pageSelections = requests.stream().filter(q -> q.getPageSelection() != null).map(q -> q.getPageSelection()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());

        ByteArrayResource response = printGeneratorRepository.generate(urns, pages, pageSelections, null, quality, "filename", fileRequest.getFormat());
        printedFile.setFileExtension(fileRequest.getPackageFormat());
        printedFile.setContentFormat(fileRequest.getFormat());
        printedFile.setFileSizeInBytes(response.contentLength());
        printedFile.setContent(response);

        return printedFile;
    }
}
