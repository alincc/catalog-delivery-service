package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.model.generic.ItemResource;
import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 10.07.15.
 */
public class OrderService implements IOrderService {

    private PrintedMaterialService printedMaterialService;
    private ZipService zipService;
    private ApplicationSettings applicationSettings;

    @Autowired
    public OrderService(PrintedMaterialService printedMaterialService, ZipService zipService, ApplicationSettings applicationSettings) {
        this.printedMaterialService = printedMaterialService;
        this.zipService = zipService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void placeOrder(ItemOrder itemOrder) throws InterruptedException, ExecutionException {
        // Make async calls to get resources
        List<Future<PrintedMaterialResource>> printedMaterialResourceFutures = itemOrder.getPrintedMaterialRequests().stream()
                        .map(request -> printedMaterialService.getTextResourceAsync(request))
                        .collect(Collectors.toList());

        // Gather all async calls
        List<ItemResource> itemResources = new ArrayList<>();
        for (Future<PrintedMaterialResource> printedMaterialResourceFuture : printedMaterialResourceFutures) {
            itemResources.add(printedMaterialResourceFuture.get());
        }

        // Zip all files to disk
        String zipOutputPath = applicationSettings.getZipFilePath() + itemOrder.getOrderId() + ".zip";
        zipService.zipIt(zipOutputPath, itemResources);

        // TODO: Call DeliveryMetadataRepository (own microservice) and store information about the order

        // TODO: Send email (own microservice) to customer with link to download order
    }
}
