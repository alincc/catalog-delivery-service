package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.factory.PrintFormatFactory;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import no.nb.microservices.delivery.model.request.PrintFormat;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

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
    public void getResourceTest() throws IOException {

        InputStream printedFile = new ByteArrayInputStream(new byte[]{1, 2, 3});

        when(formatBookService.getResource(any(PrintedFile.class))).thenReturn(printedFile);
        when(printFormatFactory.getPrintFormat(eq(PrintFormat.PDF))).thenReturn(formatBookService);

        PrintedFile printedFileRequest = new PrintedFile();
        printedFileRequest.setFormat(PrintFormat.PDF);
        printedFileRequest.setResources(Arrays.asList(new PrintedResource("URN:NBN:no-nb_digibok_2008040300029")));
        CatalogFile resource = printedService.getResource(printedFileRequest);

        assertEquals(3, IOUtils.toByteArray(resource.openInputStream()).length);
    }
}
