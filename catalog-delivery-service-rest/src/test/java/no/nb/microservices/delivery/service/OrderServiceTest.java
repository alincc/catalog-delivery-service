package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.PhotoFile;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.metadata.model.TextualResource;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.service.order.DeliveryMetadataService;
import no.nb.microservices.delivery.service.order.EmailService;
import no.nb.microservices.delivery.service.order.OrderService;
import no.nb.microservices.delivery.service.order.ZipService;
import no.nb.microservices.email.model.Email;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
/**
 * Created by andreasb on 16.07.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    TextualService textualService;

    @Mock
    PhotoService photoService;

    @Mock
    ZipService zipService;

    @Mock
    DeliveryMetadataService deliveryMetadataService;

    @Mock
    EmailService emailService;

    @Mock
    ApplicationSettings applicationSettings;

    @Test
    public void placeOrderTest() throws ExecutionException, InterruptedException, IOException {
        DeliveryOrderRequest deliveryOrderRequest = getDeliveryOrderRequest();
        Future<TextualFile> textualResourceListFuture = getTextualItem();
        Future<PhotoFile> photoFileFuture = getPhoto();

        when(textualService.getResourceAsync(eq(deliveryOrderRequest.getTextuals().get(0)))).thenReturn(textualResourceListFuture);
        when(photoService.getResourceAsync(eq(deliveryOrderRequest.getPhotos().get(0)))).thenReturn(photoFileFuture);
        when(applicationSettings.getZipFilePath()).thenReturn("");

        Resource zippedfile = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.zip");
        when(zipService.zipIt(anyString(), any(List.class))).thenReturn(zippedfile.getFile());

        when(deliveryMetadataService.saveOrder(any(DeliveryOrder.class))).then(returnsFirstArg());

        orderService.placeOrder(deliveryOrderRequest);

        ArgumentCaptor<Email> argumentCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailService, times(1)).sendEmail(argumentCaptor.capture());
        DeliveryOrder itemOrderCaptor = (DeliveryOrder)argumentCaptor.getValue().getContent();
        assertEquals(deliveryOrderRequest.getEmailTo(), argumentCaptor.getValue().getTo());
        assertTrue(itemOrderCaptor.getExpireDate().after(Date.from(Instant.now())));
        assertTrue(itemOrderCaptor.getKey().matches("^\\w{16}$"));
    }

    private Future<TextualFile> getTextualItem() throws IOException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        TextualResource textualResource = new TextualResource() {{
            setUrn("URN:NBN:no-nb_digibok_2014020626009");
        }};
        TextualFile textualFile = new TextualFile() {{
            setFilename("dummy.pdf");

            setResources(Arrays.asList(textualResource));
            setContent(byteArrayResource);
        }};
        return CompletableFuture.completedFuture(textualFile);
    }

    private Future<PhotoFile> getPhoto() throws IOException {
        Resource resource = new ClassPathResource("myphoto.zip");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        PhotoFile photoFile = new PhotoFile();
        photoFile.setUrn("URN:NBN:no-nb_digifoto_20150213_00406_NB_WF_NOK_097120");
        photoFile.setQuality(4);
        photoFile.setContent(byteArrayResource);
        photoFile.setFormat("jpg");
        photoFile.setFilename("myphoto.zip");
        photoFile.setFileSizeInBytes(byteArrayResource.contentLength());

        return CompletableFuture.completedFuture(photoFile);
    }

    private DeliveryOrderRequest getDeliveryOrderRequest() {
        TextualResourceRequest textualRequest = new TextualResourceRequest() {{
            setUrn("URN:NBN:no-nb_digibok_2014020626009");
            setPages("ALL");
            setQuality(1);
        }};

        TextualFileRequest textualFileRequest = new TextualFileRequest() {{
            setText(false);
            setFilename("dummy");
            setFormat("pdf");
            setResources(Arrays.asList(textualRequest));
        }};

        PhotoRequest photoRequest = new PhotoRequest();
        photoRequest.setUrn("URN:NBN:no-nb_digifoto_20150213_00406_NB_WF_NOK_097120");
        photoRequest.setFormat("jpg");
        photoRequest.setFilename("myphotos");
        photoRequest.setQuality(4);


        DeliveryOrderRequest itemOrder = new DeliveryOrderRequest() {{
            setEmailTo("example@example.com");
            setEmailCc("example-cc@example.com");
            setPurpose("Testing purpose");
            setCompressionType("zip");
            setTextuals(Arrays.asList(textualFileRequest));
            setPhotos(Arrays.asList(photoRequest));
        }};

        return itemOrder;
    }

    @Test
    public void getOrderTest() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        DeliveryOrder deliveryOrder = new DeliveryOrder() {{
            setFilename("ecd270f69cb8a9063306fcecd4b1a769.zip");
            setExpireDate(Date.from(Instant.now().plusSeconds(3600)));
        }};
        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(deliveryOrder);

        File file = orderService.getOrder(orderKey);
        assertNotNull(file);
        assertEquals(deliveryOrder.getFilename(), file.getPath());
    }

    @Test(expected = AccessDeniedException.class)
    public void getOrderTestExpired() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        DeliveryOrder deliveryOrder = new DeliveryOrder() {{
            setFilename("ecd270f69cb8a9063306fcecd4b1a769.zip");
            setExpireDate(Date.from(Instant.now().minusSeconds(3600)));
        }};
        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(deliveryOrder);

        File file = orderService.getOrder(orderKey);
    }
}
