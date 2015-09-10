package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import no.nb.microservices.delivery.repository.CatalogDeliveryTextRepository;
import no.nb.microservices.delivery.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.service.print.PrintedService;
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
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
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

    @Test
    public void getResourcesTest() throws IOException, ExecutionException, InterruptedException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(printGeneratorRepository.generate(anyListOf(String.class), anyListOf(String.class), anyListOf(String.class), anyListOf(Boolean.class), anyListOf(String.class), anyString(), anyString())).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2008040300029", 1, "1", "id", false);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("pdf", Arrays.asList(printedResourceRequest));

        PrintedFile textualFile = printedService.getResource(printedFileRequest);

        assertEquals("URN:NBN:no-nb_digibok_2008040300029.pdf", textualFile.getFilename());
        assertNotNull(textualFile.getContent());
        assertEquals(1495308, textualFile.getFileSizeInBytes());
    }

    @Test
    public void getResourceAltoTest() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "";
        String pageSelection = "id";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_all.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(catalogDeliveryTextRepository.getAltos(urn, pages, pageSelection)).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest(urn, pages, pageSelection);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("alto", Arrays.asList(printedResourceRequest));
        PrintedFile textualFile = printedService.getResource(printedFileRequest);

        assertEquals("urn:nbn:no-nb_digibok_2014062307158.zip", textualFile.getFilename());
        assertNotNull(textualFile.getContent());
        assertEquals(22338, textualFile.getFileSizeInBytes());
    }

    @Test
    public void getResourceAltoTestWithPages() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "1-3";
        String pageSelection = "id";

        Resource resource = new ClassPathResource("alto/urn:nbn:no-nb_digibok_2014062307158_1-3.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(catalogDeliveryTextRepository.getAltos(urn, pages, pageSelection)).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest(urn, pages, pageSelection);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("alto", Arrays.asList(printedResourceRequest));
        PrintedFile textualFile = printedService.getResource(printedFileRequest);

        assertEquals("urn:nbn:no-nb_digibok_2014062307158.zip", textualFile.getFilename());
        assertNotNull(textualFile.getContent());
        assertEquals(5003, textualFile.getFileSizeInBytes());
    }

    @Test
    public void getResourceTextTest() throws IOException {
        String urn = "urn:nbn:no-nb_digibok_2014062307158";
        String pages = "";
        String pageSelection = "id";

        Resource resource = new ClassPathResource("text/URN:NBN:no-nb_digibok_2014062307158.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(catalogDeliveryTextRepository.getText(urn, pages, pageSelection)).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest(urn, pages, pageSelection);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("txt", Arrays.asList(printedResourceRequest));
        PrintedFile textualFile = printedService.getResource(printedFileRequest);

        assertEquals("urn:nbn:no-nb_digibok_2014062307158.zip", textualFile.getFilename());
        assertNotNull(textualFile.getContent());
        assertEquals(428, textualFile.getFileSizeInBytes());
    }
}
