package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

/**
 * Created by andreasb on 23.09.15.
 */
@Service
public class FormatAltoService implements FormatService {

    private final CatalogDeliveryTextRepository catalogDeliveryTextRepository;

    @Autowired
    public FormatAltoService(CatalogDeliveryTextRepository catalogDeliveryTextRepository) {
        this.catalogDeliveryTextRepository = catalogDeliveryTextRepository;
    }

    @Override
    public PrintedFile getResource(PrintedFileRequest fileRequest) {
        PrintedFile printedFile = new PrintedFile();
        PrintedResourceRequest printedResourceRequest = fileRequest.getResources().get(0);
        ByteArrayResource response = catalogDeliveryTextRepository.getAltos(printedResourceRequest.getUrn(), printedResourceRequest.getPages(), printedResourceRequest.getPageSelection(), fileRequest.getPackageFormat());
        printedFile.setFileExtension(fileRequest.getPackageFormat());
        printedFile.setFileSizeInBytes(response.contentLength());
        printedFile.setContentFormat(fileRequest.getFormat());
        printedFile.setContent(response);

        return printedFile;
    }
}
