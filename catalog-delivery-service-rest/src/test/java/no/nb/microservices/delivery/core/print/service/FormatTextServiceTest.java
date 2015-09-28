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
public class FormatTextServiceTest {

    @InjectMocks
    FormatTextService formatTextService;

    @Mock
    CatalogDeliveryTextRepository catalogDeliveryTextRepository;

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
        PrintedFile textualFile = formatTextService.getResource(printedFileRequest);

        assertNotNull(textualFile.getContent());
        assertEquals(428, textualFile.getFileSizeInBytes());
    }
}
