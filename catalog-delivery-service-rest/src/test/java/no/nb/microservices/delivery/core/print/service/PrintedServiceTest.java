package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 24.08.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class PrintedServiceTest {
    @InjectMocks
    PrintedService printedService;

    @Mock
    PrintGeneratorRepository printGeneratorRepository;

    @Mock
    CatalogDeliveryTextRepository catalogDeliveryTextRepository;

    @Mock
    PrintFormatFactory printFormatFactory;

    @Mock
    FormatBookService formatBookService;

    @Test
    public void getResourceTest() {

        PrintedFile printedFile = new PrintedFile();
        printedFile.setContent(new ByteArrayResource(new byte[]{1, 2, 3}));
        when(formatBookService.getResource(any(PrintedFileRequest.class))).thenReturn(printedFile);
        when(printFormatFactory.getPrintFormat("pdf")).thenReturn(formatBookService);

        PrintedFileRequest printedFileRequest = new PrintedFileRequest();
        printedFileRequest.setFormat("pdf");
        printedFileRequest.setResources(Arrays.asList(new PrintedResourceRequest("URN:NBN:no-nb_digibok_2008040300029", 1)));
        PrintedFile resource = printedService.getResource(printedFileRequest);

        assertEquals(3, resource.getFileSizeInBytes());
    }
}
