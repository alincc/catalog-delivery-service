package no.nb.microservices.delivery.core.metadata.mapper;

import no.nb.microservices.delivery.core.print.builder.PrintedFileBuilder;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.PrintedFile;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.model.request.PrintedFileRequest;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static Order map(OrderRequest orderRequest) {
        List<PrintedFile> printedFiles = new ArrayList<>();
        for (PrintedFileRequest printedFileRequest : orderRequest.getPrints()) {
            PrintedFile printedFile = new PrintedFileBuilder()
                    .withFormat(printedFileRequest.getFormat())
                    .withQuality(printedFileRequest.getQuality())
                    .addText(printedFileRequest.isText())
                    .withResourceRequests(printedFileRequest.getResources())
                    .compressAs(orderRequest.getPackageFormat())
                    .build();

            printedFiles.add(printedFile);
        }

        Order orderMetadata = new Order();
        orderMetadata.setOrderId(orderRequest.getOrderId());
        orderMetadata.setEmailTo(orderRequest.getEmailTo());
        orderMetadata.setEmailCc(orderRequest.getEmailCc());
        orderMetadata.setPurpose(orderRequest.getPurpose());
        orderMetadata.setPackageFormat(orderRequest.getPackageFormat());
        orderMetadata.setPrints(printedFiles);

        return orderMetadata;
    }

    public static OrderRequest map(Order deliveryOrderRequest) {
        throw new NotImplementedException("Not implemented");
    }
}
