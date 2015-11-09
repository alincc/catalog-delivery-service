package no.nb.microservices.delivery.core.metadata.mapper;

import no.nb.microservices.delivery.core.metadata.model.Order;
import no.nb.microservices.delivery.core.metadata.model.PrintedFile;
import no.nb.microservices.delivery.core.metadata.model.PrintedResource;
import no.nb.microservices.delivery.model.order.OrderRequest;
import no.nb.microservices.delivery.model.printed.PrintedFileRequest;
import no.nb.microservices.delivery.model.printed.PrintedResourceRequest;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static Order map(OrderRequest deliveryOrderRequest) {
        List<PrintedFile> printedFiles = new ArrayList<>();
        for (PrintedFileRequest printedFileRequest : deliveryOrderRequest.getPrints()) {
            PrintedFile printedFile = new PrintedFile();
            List<PrintedResource> resources = new ArrayList<>();
            for (PrintedResourceRequest printedResourceRequest : printedFileRequest.getResources()) {
                PrintedResource printedResource = new PrintedResource();
                printedResource.setUrn(printedResourceRequest.getUrn());
                printedResource.setQuality(printedResourceRequest.getQuality());
                printedResource.setPages(printedResourceRequest.getPages());
                printedResource.setPageSelection(printedResourceRequest.getPageSelection());
                resources.add(printedResource);
            }
            printedFile.setResources(resources);
            printedFile.setFormat(printedFileRequest.getFormat());
            printedFile.setText(printedFileRequest.hasText());
            printedFiles.add(printedFile);
        }

        Order orderMetadata = new Order();
        orderMetadata.setOrderId(deliveryOrderRequest.getOrderId());
        orderMetadata.setEmailTo(deliveryOrderRequest.getEmailTo());
        orderMetadata.setEmailCc(deliveryOrderRequest.getEmailCc());
        orderMetadata.setPurpose(deliveryOrderRequest.getPurpose());
        orderMetadata.setPackageFormat(deliveryOrderRequest.getPackageFormat());
        orderMetadata.setPrints(printedFiles);

        return orderMetadata;
    }

    public OrderRequest map(Order deliveryOrderRequest) {
        throw new NotImplementedException("Not implemented");
    }
}
