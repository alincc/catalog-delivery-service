package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.delivery.model.order.CompressionType;
import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.textual.TextualResource;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void placeOrderTest() throws ExecutionException, InterruptedException, IOException {
        ItemOrder itemOrder = getItemOrderOne();

        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        TextualResource textualResource = new TextualResource(itemOrder.getTextualRequests().get(0).getUrn(), TextualFormat.PDF, byteArrayResource);
        Future<List<TextualResource>> textualResourceListFuture = CompletableFuture.completedFuture(Arrays.asList(textualResource));
        when(textualService.getResourcesAsync(eq(itemOrder.getTextualRequests().get(0)))).thenReturn(textualResourceListFuture);

        when(applicationSettings.getZipFilePath()).thenReturn("");

        Resource zippedfile = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.zip");
        when(zipService.zipIt(anyString(), any(List.class))).thenReturn(zippedfile.getFile());

        when(deliveryMetadataService.saveOrder(any(OrderMetadata.class))).then(returnsFirstArg());

        orderService.placeOrder(itemOrder);

        ArgumentCaptor<OrderMetadata> argumentCaptor = ArgumentCaptor.forClass(OrderMetadata.class);
        verify(emailService, times(1)).sendDeliveryEmail(argumentCaptor.capture());
        assertEquals(itemOrder.getDestinationEmail(), argumentCaptor.getValue().getDestinationEmail());
        assertTrue(argumentCaptor.getValue().getExpireDate().after(Date.from(Instant.now())));
        assertTrue(argumentCaptor.getValue().getKey().matches("^\\w{16}$"));
    }

    private ItemOrder getItemOrderOne() {
        TextualRequest textualRequest = new TextualRequest() {{
            setUrn("URN:NBN:no-nb_digibok_2014020626009");
            setFormat(TextualFormat.PDF);
            setPages("ALL");
            setQuality(6);
            setText(false);
        }};

        ItemOrder itemOrder = new ItemOrder() {{
            setDestinationEmail("example@example.com");
            setDestinationCCEmail("example-cc@example.com");
            setPurpose("Testing purpose");
            setCompressionType(CompressionType.ZIP);
            setTextualRequests(Arrays.asList(textualRequest));
            setAudioRequests(null);
            setVideoRequests(null);
            setPhotoRequests(null);
        }};

        return itemOrder;
    }
}
