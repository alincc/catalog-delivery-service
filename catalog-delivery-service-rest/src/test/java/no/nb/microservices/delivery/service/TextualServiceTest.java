package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.metadata.model.TextualResource;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.repository.PdfGeneratorRepository;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 24.08.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class TextualServiceTest {
    @InjectMocks
    TextualService textualService;

    @Mock
    PdfGeneratorRepository pdfGeneratorRepository;

    @Test
    public void getResourcesTest() throws IOException, ExecutionException, InterruptedException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(pdfGeneratorRepository.generate(anyListOf(String.class), anyListOf(String.class), eq("book"), anyBoolean(), anyListOf(String.class), anyString(), anyString())).thenReturn(byteArrayResource);

        TextualResourceRequest textualResourceRequest = new TextualResourceRequest() {{
            setUrn("URN:NBN:no-nb_digibok_2008040300029");
            setPages("1");
            setQuality(1);
        }};
        TextualFileRequest textualFileRequest = new TextualFileRequest() {{
            setText(false);
            setFormat("pdf");
            setFilename("dummy");
            setTextualResourceRequests(Arrays.asList(textualResourceRequest));
        }};

        TextualFile textualFile = textualService.getResource(textualFileRequest);

        assertEquals("pdf", textualFile.getExtension());
        assertEquals("dummy", textualFile.getFilename());
        assertEquals("dummy.pdf", textualFile.getFullFilename());
        assertNotNull(textualFile.getContent());
        assertEquals(1495308, textualFile.getFileSizeInBytes());
    }
}
