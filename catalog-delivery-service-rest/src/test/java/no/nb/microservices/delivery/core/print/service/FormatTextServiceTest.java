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
import static org.mockito.Mockito.when;

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

        when(catalogDeliveryTextRepository.getText(urn, pages, pageSelection)).thenReturn(inputStream);

        PrintedResource printedResourceRequest = new PrintedResource(urn, pages, pageSelection);
        PrintedFile printedFileRequest = new PrintedFile(Arrays.asList(printedResourceRequest), PrintFormat.TXT);
        InputStream textualFile = formatTextService.getResource(printedFileRequest);

        assertEquals(428, IOUtils.toByteArray(textualFile).length);
    }
}
