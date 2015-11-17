package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import no.nb.microservices.delivery.model.request.PrintFormat;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormatAltoServiceTest {

    @InjectMocks
    FormatAltoService formatAltoService;

    @Mock
    CatalogDeliveryTextRepository catalogDeliveryTextRepository;

    @Test
    public void getResourceAltoTest() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "";
        String pageSelection = "id";
        String format = "alto";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_all.zip");
        InputStream inputStream = resource.getInputStream();

        when(catalogDeliveryTextRepository.getAltos(eq(urn), eq(pages), eq(pageSelection), eq(format))).thenReturn(inputStream);

        PrintedResource printedResourceRequest = new PrintedResource(urn, pages, pageSelection);
        PrintedFile printedFileRequest = new PrintedFile(Arrays.asList(printedResourceRequest), PrintFormat.ALTO);
        InputStream textualFile = formatAltoService.getResource(printedFileRequest);

        assertEquals(22338, IOUtils.toByteArray(textualFile).length);
    }

    @Test
    public void getResourceAltoTestWithPages() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "1-3";
        String pageSelection = "id";
        String format = "alto";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_1-3.zip");
        InputStream inputStream = resource.getInputStream();

        when(catalogDeliveryTextRepository.getAltos(urn, pages, pageSelection, format)).thenReturn(inputStream);

        PrintedResource printedResourceRequest = new PrintedResource(urn, pages, pageSelection);
        PrintedFile printedFileRequest = new PrintedFile(Arrays.asList(printedResourceRequest), PrintFormat.ALTO);
        InputStream textualFile = formatAltoService.getResource(printedFileRequest);

        assertEquals(5003, IOUtils.toByteArray(textualFile).length);
    }
}
