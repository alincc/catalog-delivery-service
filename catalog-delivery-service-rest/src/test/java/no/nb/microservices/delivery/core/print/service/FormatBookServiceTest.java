package no.nb.microservices.delivery.core.print.service;

import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
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
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 24.09.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormatBookServiceTest {

    @InjectMocks
    FormatBookService formatBookService;

    @Mock
    PrintGeneratorRepository printGeneratorRepository;

    @Test
    public void getBookServiceResourceTest() throws IOException, ExecutionException, InterruptedException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(printGeneratorRepository.generate(anyListOf(String.class), anyListOf(String.class), anyListOf(String.class), anyListOf(Boolean.class), anyListOf(String.class), anyString(), anyString())).thenReturn(byteArrayResource);

        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2008040300029", 1, "1", "id", false);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("pdf", Arrays.asList(printedResourceRequest));

        PrintedFile textualFile = formatBookService.getResource(printedFileRequest);

        assertNotNull(textualFile.getContent());
        assertEquals(1495308, textualFile.getFileSizeInBytes());
    }
}
