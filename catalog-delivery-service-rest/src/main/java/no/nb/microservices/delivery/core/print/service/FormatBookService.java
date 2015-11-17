package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.print.repository.IPrintGeneratorRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatBookService implements FormatService {

    private final IPrintGeneratorRepository printGeneratorRepository;

    @Autowired
    public FormatBookService(IPrintGeneratorRepository printGeneratorRepository) {
        this.printGeneratorRepository = printGeneratorRepository;
    }

    @Override
    public InputStream getResource(PrintedFile fileRequest) throws IOException {
        List<PrintedResource> requests = fileRequest.getResources();
        List<String> urns = requests.stream().map(q -> q.getUrn()).collect(Collectors.toList());
        List<String> pages = requests.stream().filter(q -> q.getPages() != null).map(q -> q.getPages()).collect(Collectors.toList());
        List<String> pageSelections = requests.stream().filter(q -> q.getPageSelection() != null).map(q -> q.getPageSelection()).collect(Collectors.toList());
        List<String> quality = requests.stream().map(q -> fileRequest.getQuality() + "").collect(Collectors.toList());

        return printGeneratorRepository.generate(urns, pages, pageSelections, fileRequest.isText(), quality, "filename", fileRequest.getFormat().toString());
    }
}
