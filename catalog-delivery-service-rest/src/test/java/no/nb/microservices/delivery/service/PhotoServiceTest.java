package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.PhotoFile;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.repository.TextualGeneratorRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by andreasb on 31.08.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class PhotoServiceTest {

    @InjectMocks
    PhotoService photoService;

    @Mock
    TextualGeneratorRepository textualGeneratorRepository;

    @Test
    public void getResougetFormatrcesTest() throws IOException {
        Resource resource = new ClassPathResource("myphoto.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));

        when(textualGeneratorRepository.generate(anyListOf(String.class), anyListOf(String.class), anyString(), anyBoolean(), anyListOf(String.class), anyString(), anyString())).thenReturn(byteArrayResource);

        PhotoRequest photoRequest = new PhotoRequest();
        photoRequest.setQuality(4);
        photoRequest.setUrn("URN:NBN:no-nb_digifoto_20150213_00406_NB_WF_NOK_097120");
        photoRequest.setFilename("myphoto");
        photoRequest.setFormat("jpg");

        PhotoFile photo = photoService.getResource(photoRequest);
        assertEquals("myphoto.zip", photo.getFilename());
        assertEquals("URN:NBN:no-nb_digifoto_20150213_00406_NB_WF_NOK_097120", photo.getUrn());
        assertEquals("jpg", photo.getFormat());
        assertEquals(4, photo.getQuality());
        assertNotNull(photo.getContent());
        assertEquals(405556, photo.getFileSizeInBytes());
    }


}
