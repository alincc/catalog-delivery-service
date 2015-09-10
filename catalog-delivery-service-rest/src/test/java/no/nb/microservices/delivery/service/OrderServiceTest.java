package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.metadata.model.PrintedResource;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import no.nb.microservices.delivery.service.cloud.DeliveryMetadataService;
import no.nb.microservices.delivery.service.cloud.EmailService;
import no.nb.microservices.delivery.service.order.OrderService;
import no.nb.microservices.delivery.service.order.ZipService;
import no.nb.microservices.delivery.service.print.PrintedService;
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
    PrintedService printedService;

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
        Future<PrintedFile> printedFileFuture = getTextualItem();

        when(printedService.getResourceAsync(eq(deliveryOrderRequest.getPrints().get(0)))).thenReturn(printedFileFuture);
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

    private Future<PrintedFile> getTextualItem() throws IOException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        InputStream inputStream = resource.getInputStream();
        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        PrintedResource printedResource = new PrintedResource("URN:NBN:no-nb_digibok_2014020626009", "book", 4, "ALL");
        PrintedFile printedFile = new PrintedFile("dummy.pdf", "pdf", Arrays.asList(printedResource));
        printedFile.setContent(byteArrayResource);
        return CompletableFuture.completedFuture(printedFile);
    }

    private DeliveryOrderRequest getDeliveryOrderRequest() {
        PrintedResourceRequest textualRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2014020626009", 1, "ALL", "id", false);
        PrintedFileRequest printedFileRequest = new PrintedFileRequest("pdf", Arrays.asList(textualRequest));

        DeliveryOrderRequest itemOrder = new DeliveryOrderRequest() {{
            setEmailTo("example@example.com");
            setEmailCc("example-cc@example.com");
            setPurpose("Testing purpose");
            setCompressionType("zip");
            setPrints(Arrays.asList(printedFileRequest));
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
