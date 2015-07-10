package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.text.TextFormat;
import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.text.TextResource;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by andreasb on 09.07.15.
 */
@Service
public class TextService implements ITextService {

    private final PdfGeneratorRepository pdfGeneratorRepository;

    @Autowired
    public TextService(PdfGeneratorRepository pdfGeneratorRepository) {
        this.pdfGeneratorRepository = pdfGeneratorRepository;
    }

    @Override
    public TextResource getTextResource(TextRequest textRequest) {

        TextResource textResource;

        if (TextFormat.PDF.equals(textRequest.getFormat())) {
            textResource = new TextResource(textRequest.getUrn(), textRequest.getFormat(), getTextAsPdf(textRequest));
        }
        else {
            throw new IllegalArgumentException("Format is invalid in query");
        }

        return textResource;
    }

    private ByteArrayResource getTextAsPdf(TextRequest textRequest) {
        ByteArrayResource response = pdfGeneratorRepository.generate(Arrays.asList(textRequest.getUrn()), null, "", false, null, "", "");
        return response;
    }
}
