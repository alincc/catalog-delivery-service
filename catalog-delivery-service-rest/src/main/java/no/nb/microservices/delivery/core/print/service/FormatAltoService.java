package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.metadata.model.PrintedFile;
import no.nb.microservices.delivery.core.metadata.model.PrintedResource;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FormatAltoService implements FormatService {

    private final CatalogDeliveryTextRepository catalogDeliveryTextRepository;

    @Autowired
    public FormatAltoService(CatalogDeliveryTextRepository catalogDeliveryTextRepository) {
        this.catalogDeliveryTextRepository = catalogDeliveryTextRepository;
    }

    @Override
    public InputStream getResource(PrintedFile fileRequest, String packageFormat) {
        PrintedResource printedResourceRequest = fileRequest.getResources().get(0);
        InputStream response = catalogDeliveryTextRepository.getAltos(printedResourceRequest.getUrn(), printedResourceRequest.getPages(), printedResourceRequest.getPageSelection(), packageFormat);
        return response;
    }
}
