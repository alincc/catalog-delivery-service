package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.text.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 24.09.15.
 */
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
        String format = "zip";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_all.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(catalogDeliveryTextRepository.getAltos(urn, pages, pageSelection, format)).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest(urn, pages, pageSelection);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("alto", Arrays.asList(printedResourceRequest));
        PrintedFile textualFile = formatAltoService.getResource(printedFileRequest);

        assertNotNull(textualFile.getContent());
        assertEquals(22338, textualFile.getFileSizeInBytes());
    }

    @Test
    public void getResourceAltoTestWithPages() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "1-3";
        String pageSelection = "id";
        String format = "zip";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_1-3.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(catalogDeliveryTextRepository.getAltos(urn, pages, pageSelection, format)).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest(urn, pages, pageSelection);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("alto", Arrays.asList(printedResourceRequest));
        PrintedFile textualFile = formatAltoService.getResource(printedFileRequest);

        assertNotNull(textualFile.getContent());
        assertEquals(5003, textualFile.getFileSizeInBytes());
    }
}
