package no.nb.microservices.delivery.it;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.delivery.Application;
import okio.Buffer;
import org.apache.commons.io.IOUtils;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

/**
 * Created by andreasb on 14.09.15.
 */
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

    RestTemplate template = new TestRestTemplate();

    MockWebServer server;

    @Before
    public void setup() throws Exception {
        String itemId1Mock = IOUtils.toString(new ClassPathResource("catalog-item-service-id1.json").getInputStream());

        server = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/urn%3Anbn%3Ano-nb_digibok_2014062307158")){
                    return new MockResponse().setBody(itemId1Mock).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                }
                else if (request.getPath().equals("/generate?urn=urn%3Anbn%3Ano-nb_digibok_2014062307158&pages=&pageSelection=id&addText=false&resolutionlevel=5&filename=filename&filetype=pdf")) {
                    Buffer buffer = new Buffer();
                    try {
                        InputStream inputStream = new ClassPathResource("d287191ca81f4bd702630e2ec74466bb-9088.pdf").getInputStream();
                        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
                        buffer.write(byteArrayResource.getByteArray());
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    return new MockResponse().setBody(buffer);
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

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }


    @Test
    public void downloadPrintdResponseShouldBeOk() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/download/prints/urn:nbn:no-nb_digibok_2014062307158");
        byte[] execute = new TestRestTemplate().execute(uri, HttpMethod.GET, new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
                HttpHeaders headers = request.getHeaders();
                headers.add("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
        }, new ResponseExtractor<byte[]>() {
            @Override
            public byte[] extractData(ClientHttpResponse response) throws IOException {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Expected 200 got " + response.getStatusCode());
                }
                return IOUtils.toByteArray(response.getBody());
            }
        });
        assertNotNull(execute);
    }
}

@Configuration
class RibbonClientConfiguration {

    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}