package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.metadata.model.TextualResource;
import no.nb.microservices.delivery.model.order.CompressionType;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.service.order.*;
import no.nb.microservices.email.model.Email;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
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

        when(textualService.getResourcesAsync(eq(deliveryOrderRequest.getTextualFileRequests().get(0)))).thenReturn(textualResourceListFuture);
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
            setFilename("dummy");
            setExtension("pdf");
            setTextualResources(Arrays.asList(textualResource));
            setContent(byteArrayResource);
        }};
        return CompletableFuture.completedFuture(textualFile);
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
            setTextualResourceRequests(Arrays.asList(textualRequest));
        }};

        DeliveryOrderRequest itemOrder = new DeliveryOrderRequest() {{
            setEmailTo("example@example.com");
            setEmailCc("example-cc@example.com");
            setPurpose("Testing purpose");
            setCompressionType(CompressionType.ZIP);
            setTextualFileRequests(Arrays.asList(textualFileRequest));
        }};

        return itemOrder;
    }
}
