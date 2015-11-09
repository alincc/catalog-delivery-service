package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.metadata.model.PrintedFile;
import no.nb.microservices.delivery.core.metadata.model.PrintedResource;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatBookService implements FormatService {

    private final PrintGeneratorRepository printGeneratorRepository;

    @Autowired
    public FormatBookService(PrintGeneratorRepository printGeneratorRepository) {
        this.printGeneratorRepository = printGeneratorRepository;
    }

    @Override
    public InputStream getResource(PrintedFile fileRequest, String packageFormat) {
        List<PrintedResource> requests = fileRequest.getResources();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().filter(q -> q.getPages() != null).map(q -> q.getPages()).collect(Collectors.toList());
        List<String> pageSelections = requests.stream().filter(q -> q.getPageSelection() != null).map(q -> q.getPageSelection()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> q.getQuality() + "").collect(Collectors.toList());

        InputStream response = printGeneratorRepository.generate(urns, pages, pageSelections, fileRequest.hasText(), quality, "filename", fileRequest.getFormat());
        return response;
    }
}
