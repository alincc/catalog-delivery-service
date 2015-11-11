package no.nb.microservices.delivery.core.print.service;

import feign.Response;
import no.nb.microservices.delivery.core.print.repository.PrintGeneratorRepository;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.PrintedResource;
import no.nb.microservices.delivery.model.request.PrintFormat;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormatImageServiceTest {
    @InjectMocks
    FormatImageService formatImageService;

    @Mock
    PrintGeneratorRepository printGeneratorRepository;

    @Test
    public void getBookServiceResourceTest() throws IOException, ExecutionException, InterruptedException {
        Resource resource = new ClassPathResource("myphoto.zip");
        InputStream inputStream = resource.getInputStream();
        Map<String, Collection<String>> headers = new HashedMap();
        Response response = Response.create(200, "test", headers, inputStream, (int)resource.contentLength());

        when(printGeneratorRepository.generate(anyListOf(String.class), anyListOf(String.class), anyListOf(String.class), any(Boolean.class), anyListOf(String.class), anyString(), anyString())).thenReturn(response);

        PrintedResource printedResourceRequest = new PrintedResource("URN:NBN:no-nb_digibok_2008040300029", "1", "id");
        PrintedFile printedFileRequest = new PrintedFile(Arrays.asList(printedResourceRequest), PrintFormat.JPG);

        InputStream textualFile = formatImageService.getResource(printedFileRequest);

        assertEquals(405556, IOUtils.toByteArray(textualFile).length);
    }
}
