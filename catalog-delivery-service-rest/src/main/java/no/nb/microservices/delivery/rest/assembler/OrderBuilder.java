package no.nb.microservices.delivery.rest.assembler;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.model.request.PrintedFileRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {

    private Order order;

    private final String HOME_URL = "http://api.nb.no/v1/";

    public OrderBuilder() {
        order = new Order();
    }

    public OrderBuilder(Order order) {
        this.order = order;
    }

    public OrderBuilder(OrderRequest order) {
        List<PrintedFile> printedFiles = new ArrayList<>();
        for (PrintedFileRequest printedFileRequest : order.getPrints()) {
            PrintedFile printedFile = new PrintedFileBuilder()
                    .withFormat(printedFileRequest.getFormat())
                    .withQuality(printedFileRequest.getQuality())
                    .addText(printedFileRequest.isText())
                    .withResourceRequests(printedFileRequest.getResources())
                    .compressAs(order.getPackageFormat())
                    .build();

            printedFiles.add(printedFile);
        }

        Order orderMetadata = new Order();
        orderMetadata.setEmailTo(order.getEmailTo());
        orderMetadata.setEmailCc(order.getEmailCc());
        orderMetadata.setPurpose(order.getPurpose());
        orderMetadata.setPackageFormat(order.getPackageFormat());
        orderMetadata.setPrints(printedFiles);

        this.order = orderMetadata;
    }

    public OrderBuilder withEmailTo(String emailTo) {
        order.setEmailTo(emailTo);
        return this;
    }

    public OrderBuilder withKey(String key) {
        order.setKey(key);
        return this;
    }

    public OrderBuilder generateKey() {
        order.setKey(RandomStringUtils.randomAlphanumeric(16).toLowerCase());
        return this;
    }

    public OrderBuilder withFilename(String filename) {
        order.setFilename(filename);
        return this;
    }

    public OrderBuilder withDownloadPath(DiscoveryClient client, String orderKey) {
        try {
            InstanceInfo instance = client.getNextServerFromEureka("ZUULSERVER", false);
            order.setDownloadUrl(instance.getHomePageUrl() + "delivery/orders/" + orderKey);
        }
        catch (Exception e) {
            order.setDownloadUrl(HOME_URL + "delivery/orders/" + orderKey);
        }
        return this;
    }

    public OrderBuilder withDownloadPath(DiscoveryClient client) {
        if (StringUtils.isEmpty(order.getKey())) {
            generateKey();
        }

        try {
            InstanceInfo instance = client.getNextServerFromEureka("ZUULSERVER", false);
            order.setDownloadUrl(instance.getHomePageUrl() + "delivery/orders/" + this.order.getKey());
        }
        catch (Exception e) {
            order.setDownloadUrl(HOME_URL + "delivery/orders/" + this.order.getKey());
        }
        return this;
    }

    public OrderBuilder withExpireDate(int secondsFromNow) {
        order.setExpireDate(Date.from(Instant.now().plusSeconds(secondsFromNow)));
        return this;
    }

    public OrderBuilder addPrintedFile(PrintedFile printedFile) {
        if (order.getPrints() == null) {
            order.setPrints(new ArrayList<>());
        }

        order.getPrints().add(printedFile);

        return this;
    }

    public Order build() {
        if (StringUtils.isEmpty(order.getFilename())) {
            order.setFilename(order.getOrderId() + "." + order.getPackageFormat());
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(Date.from(Instant.now()));
        }

        order.setOrderId(UUID.randomUUID().toString());
        return order;
    }
}
