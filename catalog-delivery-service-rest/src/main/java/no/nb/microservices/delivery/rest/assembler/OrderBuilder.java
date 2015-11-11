package no.nb.microservices.delivery.rest.assembler;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.model.request.PrintedFileRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {

    private Order order;

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

    public OrderBuilder withFilename(String filename) {
        order.setFilename(filename);
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
        order.setOrderId(UUID.randomUUID().toString());
        return order;
    }
}
