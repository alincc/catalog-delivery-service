package no.nb.microservices.delivery.core.order.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import no.nb.commons.io.compression.factory.Compressible;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.config.EmailSettings;
import no.nb.microservices.delivery.core.compression.service.CompressionService;
import no.nb.microservices.delivery.core.email.service.EmailService;
import no.nb.microservices.delivery.core.metadata.service.DeliveryMetadataService;
import no.nb.microservices.delivery.core.order.exception.OrderFailedException;
import no.nb.microservices.delivery.core.order.exception.OrderNotReadyException;
import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.service.PrintedService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.metadata.State;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.model.request.PrintedFileRequest;
import no.nb.microservices.delivery.model.request.PrintedResourceRequest;
import no.nb.microservices.delivery.rest.assembler.OrderBuilder;
import no.nb.microservices.delivery.rest.assembler.PrintedFileBuilder;
import no.nb.microservices.email.model.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    PrintedService printedService;

    @Mock
    DeliveryMetadataService deliveryMetadataService;

    @Mock
    EmailService emailService;

    @Mock
    ApplicationSettings applicationSettings;

    @Mock
    DiscoveryClient disoveryClient;

    @Mock
    CompressionService compressionService;

    @Test
    public void placeOrderTest() throws ExecutionException, InterruptedException, IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");

        OrderRequest deliveryOrderRequest = getDeliveryOrderRequest();
        Future<CatalogFile> printedFileFuture = getTextualItem();

        EmailSettings emailSettings = new EmailSettings();
        emailSettings.setSubject("placeOrderTest");
        emailSettings.setFrom("placeOrderTest@nb.no");
        emailSettings.setTemplate("template.vm");

        when(printedService.getResourceAsync(any(PrintedFile.class))).thenReturn(printedFileFuture);
        when(applicationSettings.getZipFilePath()).thenReturn(tmpDir + "/");
        when(applicationSettings.getEmail()).thenReturn(emailSettings);

        Resource zippedfile = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.zip");

        InstanceInfo instance = InstanceInfo.Builder.newBuilder().setAppName("testApp").setHostName("api.nb.no").setHomePageUrl("api", "/delivery").build();
        when(disoveryClient.getNextServerFromEureka(anyString(), anyBoolean())).thenReturn(instance);
        when(deliveryMetadataService.saveOrder(any(Order.class))).then(returnsFirstArg());

        orderService.placeOrder(deliveryOrderRequest);

        ArgumentCaptor<Email> argumentCaptor = ArgumentCaptor.forClass(Email.class);
        verify(deliveryMetadataService, times(1)).saveOrder(any(Order.class));
    }

    private Future<CatalogFile> getTextualItem() throws IOException {
        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
        CatalogFile catalogFile = new CatalogFile("ecd270f69cb8a9063306fcecd4b1a769.pdf", resource.getInputStream());
        return CompletableFuture.completedFuture(catalogFile);
    }

    private OrderRequest getDeliveryOrderRequest() {
        PrintedResourceRequest textualRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2014020626009", "ALL", "id");
        PrintedFileRequest printedFileRequest = new PrintedFileRequest(Arrays.asList(textualRequest), "pdf", 1);

        OrderRequest itemOrder = new OrderRequest() {{
            setEmailTo("example@example.com");
            setEmailCc("example-cc@example.com");
            setPurpose("Testing purpose");
            setPackageFormat("zip");
            setPrints(Arrays.asList(printedFileRequest));
        }};

        return itemOrder;
    }

    @Test
    public void getOrderTest() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        Order order = new OrderBuilder()
                .withFilename("ecd270f69cb8a9063306fcecd4b1a769.zip")
                .withExpireDate(3600)
                .withKey(orderKey)
                .withState(State.DONE)
                .build();

        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(order);

        File file = orderService.getOrder(orderKey);
        assertNotNull(file);
        assertEquals(order.getFilename(), file.getPath());
    }

    @Test(expected = OrderFailedException.class)
    public void getFailedOrderTest() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        Order order = new OrderBuilder()
                .withFilename("ecd270f69cb8a9063306fcecd4b1a769.zip")
                .withExpireDate(3600)
                .withKey(orderKey)
                .withState(State.ERROR)
                .build();

        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(order);

        File file = orderService.getOrder(orderKey);
    }

    @Test(expected = OrderNotReadyException.class)
    public void getNotReadyOrderTest() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        Order order = new OrderBuilder()
                .withFilename("ecd270f69cb8a9063306fcecd4b1a769.zip")
                .withExpireDate(3600)
                .withKey(orderKey)
                .withState(State.PROCESSING)
                .build();

        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(order);

        File file = orderService.getOrder(orderKey);
    }

    @Test(expected = AccessDeniedException.class)
    public void getOrderTestExpired() {
        String orderKey = "d8o00w7zz2cRy8Wm";
        Order deliveryOrder = new Order() {{
            setFilename("ecd270f69cb8a9063306fcecd4b1a769.zip");
            setExpireDate(Date.from(Instant.now().minusSeconds(3600)));
        }};
        when(applicationSettings.getZipFilePath()).thenReturn("");
        when(deliveryMetadataService.getOrderByIdOrKey(eq(orderKey))).thenReturn(deliveryOrder);

        File file = orderService.getOrder(orderKey);
    }

    @Test
    public void proccessOrderTest() throws IOException {
        Order order = new OrderBuilder()
                .withEmailTo("example@example.com")
                .withKey("hzeydrfdppbmtkck")
                .withFilename("order.zip")
                .addPrintedFile(
                        new PrintedFileBuilder()
                            .addResource("URN:NBN:no-nb_digibok_2014012027004", "1")
                            .build())
                .build();

        when(printedService.getResourceAsync(any(PrintedFile.class))).thenReturn(getTextualItem());
        when(applicationSettings.getZipFilePath()).thenReturn("/tmp");

        orderService.processOrder(order);

        verify(emailService, times(1)).sendEmail(any(Order.class));
        verify(compressionService, times(1)).openArchive(any(File.class), eq("zip"));
        verify(compressionService, times(1)).addEntry(any(Compressible.class));
        verify(compressionService, times(1)).closeArchive();
    }

    @Test
    public void proccessOrderWithExceptionTest() throws IOException {
        Order order = new OrderBuilder()
                .withEmailTo("example@example.com")
                .withKey("hzeydrfdppbmtkck")
                .withFilename("order.zip")
                .addPrintedFile(
                        new PrintedFileBuilder()
                                .addResource("URN:NBN:no-nb_digibok_2014012027004", "1")
                                .build())
                .build();

        when(printedService.getResourceAsync(any(PrintedFile.class))).thenReturn(getTextualItem());
        when(applicationSettings.getZipFilePath()).thenReturn("/tmp");
        doThrow(new IOException("Failed to write file to disk")).when(compressionService).openArchive(any(File.class), eq("zip"));

        orderService.processOrder(order);

        assertEquals(State.ERROR, order.getState());
    }
}
