package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FormatTextService implements FormatService {

    private final CatalogDeliveryTextRepository catalogDeliveryTextRepository;

    @Autowired
    public FormatTextService(CatalogDeliveryTextRepository catalogDeliveryTextRepository) {
        this.catalogDeliveryTextRepository = catalogDeliveryTextRepository;
    }

    @Override
    public InputStream getResource(PrintedFile fileRequest) {
        PrintedResource printedResourceRequest = fileRequest.getResources().get(0);
        return catalogDeliveryTextRepository.getText(printedResourceRequest.getUrn(), printedResourceRequest.getPages(), printedResourceRequest.getPageSelection());
    }
}
