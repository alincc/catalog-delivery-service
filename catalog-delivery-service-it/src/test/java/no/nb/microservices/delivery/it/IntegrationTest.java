package no.nb.microservices.delivery.it;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.delivery.Application;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.metadata.repository.OrderRepository;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.model.request.PrintFormat;
import no.nb.microservices.delivery.model.request.PrintedFileRequest;
import no.nb.microservices.delivery.model.request.PrintedResourceRequest;
import okio.Buffer;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port:0")
public class IntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ILoadBalancer lb;

    @Autowired
    OrderRepository orderRepository;

    RestTemplate rest = new TestRestTemplate();

    MockWebServer server;

    @Autowired
    ApplicationSettings applicationSettings;

    @Before
    public void setup() throws Exception {
        orderRepository.deleteAll();
        URL path = this.getClass().getResource("/");
        applicationSettings.setZipFilePath(path.getPath());

        String itemId1Mock = IOUtils.toString(new ClassPathResource("catalog-item-service-id1.json").getInputStream());
        String deliveryMetadata1Mock = IOUtils.toString(new ClassPathResource("catalog-delivery-metadata-service-1.json").getInputStream());
        String deliveryMetadata2Mock = IOUtils.toString(new ClassPathResource("catalog-delivery-metadata-service-2.json").getInputStream());
        String deliveryMetadata3Mock = IOUtils.toString(new ClassPathResource("catalog-delivery-metadata-service-3.json").getInputStream());

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/urn%3Anbn%3Ano-nb_digibok_2014062307158")){
                    return new MockResponse().setBody(itemId1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/generate?urn=urn%3Anbn%3Ano-nb_digibok_2014062307158&pages=&pageSelection=id&addText=false&resolutionlevel=5&filename=filename&filetype=pdf")
                        || request.getPath().equals("/generate?urn=URN%3ANBN%3Ano-nb_digibok_2014091948005&addText=false&resolutionlevel=5&filename=filename&filetype=pdf")
                        || request.getPath().equals("/generate?urn=urn%3Anbn%3Ano-nb_digibok_2014062307158&pages=&addText=false&resolutionlevel=5&filename=filename&filetype=PDF")) {
                    return getMockResponse("d287191ca81f4bd702630e2ec74466bb-9088.pdf");
                }
                else if (request.getPath().equals("/deliveryx/orders")) {
                    return new MockResponse().setBody(deliveryMetadata1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/deliveryx/orders/0YvkQv9myztAmAfs")) {
                    return new MockResponse().setBody(deliveryMetadata1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/deliveryx/orders/YCFa5GcrIFQUlDKW")) {
                    return new MockResponse().setBody(deliveryMetadata2Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/deliveryx/orders/d8sjxnajhd87caxa")) {
                    return new MockResponse().setBody(deliveryMetadata3Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/alto/URN%3ANBN%3Ano-nb_digibok_2014062307158?packageFormat=tar.gz")) {
                    return getMockResponse("urn_nbn_no-nb_digibok_2014062307158.tar.gz");
                }

                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        server.start();

        BaseLoadBalancer blb = (BaseLoadBalancer) lb;
        blb.setServersList(Arrays.asList(new Server(server.getHostName(), server.getPort())));

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private MockResponse getMockResponse(String path) {
        Buffer buffer = new Buffer();
        try {
            InputStream inputStream = new ClassPathResource(path).getInputStream();
            ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
            buffer.write(byteArrayResource.getByteArray());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new MockResponse().setBody(buffer);
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void downloadPrintdResponseShouldBeOk() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/delivery/download/prints/urn:nbn:no-nb_digibok_2014062307158");
        ResponseEntity<ByteArrayResource> response = rest.getForEntity(uri, ByteArrayResource.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1009939, response.getBody().contentLength());
        assertEquals("attachment; filename=urn:nbn:no-nb_digibok_2014062307158.pdf", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void placeOrderWithPdfShouldBeOk() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/delivery/orders");
        OrderRequest deliveryOrderRequest = new OrderRequest();
        deliveryOrderRequest.setEmailTo("dev@nb.no");
        deliveryOrderRequest.setPurpose("test");
        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2014091948005");
        PrintedFileRequest printedFileRequest = new PrintedFileRequest(Arrays.asList(printedResourceRequest), PrintFormat.PDF, 5);
        deliveryOrderRequest.setPrints(Arrays.asList(printedFileRequest));

        ResponseEntity<Order> response = rest.postForEntity(uri, deliveryOrderRequest, Order.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getOrderId());
    }

    @Test
    public void placeOrderWithAltoShouldBeOk() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/delivery/orders");
        OrderRequest deliveryOrderRequest = new OrderRequest();
        deliveryOrderRequest.setEmailTo("dev@nb.no");
        deliveryOrderRequest.setPurpose("test");
        deliveryOrderRequest.setPackageFormat("tar.gz");
        PrintedResourceRequest printedResourceRequest = new PrintedResourceRequest("URN:NBN:no-nb_digibok_2014062307158");
        PrintedFileRequest printedFileRequest = new PrintedFileRequest(Arrays.asList(printedResourceRequest), PrintFormat.ALTO);
        deliveryOrderRequest.setPrints(Arrays.asList(printedFileRequest));

        ResponseEntity<String> response = rest.postForEntity(uri, deliveryOrderRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getOrderWithZip() throws URISyntaxException {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setKey("0YvkQv9myztAmAfs");
        order.setFilename("d287191ca81f4bd702630e2ec74466bb-9088.zip");
        order.setOrderDate(Date.from(Instant.now()));
        order.setExpireDate(Date.from(Instant.now().plusSeconds(604800)));
        orderRepository.save(order);

        URI uri = new URI("http://localhost:" + port + "/delivery/orders/0YvkQv9myztAmAfs");
        ResponseEntity<ByteArrayResource> response = rest.getForEntity(uri, ByteArrayResource.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(982565, response.getBody().contentLength());
    }

    @Test
    public void getOrderWithTarGz() throws URISyntaxException {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setKey("YCFa5GcrIFQUlDKW");
        order.setFilename("d287191ca81f4bd702630e2ec74466bb-9088.tar.gz");
        order.setOrderDate(Date.from(Instant.now()));
        order.setExpireDate(Date.from(Instant.now().plusSeconds(604800)));
        orderRepository.save(order);

        URI uri = new URI("http://localhost:" + port + "/delivery/orders/YCFa5GcrIFQUlDKW");
        ResponseEntity<ByteArrayResource> response = rest.getForEntity(uri, ByteArrayResource.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(983218, response.getBody().contentLength());
    }

    @Test
    public void getOrderThatHasExpired() throws URISyntaxException, ParseException {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setKey("d8sjxnajhd87caxa");
        order.setOrderDate(new Date(1446074535971L));
        order.setExpireDate(new Date(1446679335971L));
        orderRepository.save(order);

        URI uri = new URI("http://localhost:" + port + "/delivery/orders/d8sjxnajhd87caxa");
        ResponseEntity<String> response = rest.getForEntity(uri, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}

@Configuration
class RibbonClientConfiguration {
    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}