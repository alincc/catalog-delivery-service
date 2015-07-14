package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.model.generic.ItemResource;
import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.model.text.TextResource;
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

    private TextService textService;
    private ZipService zipService;
    private ApplicationSettings applicationSettings;

    @Autowired
    public OrderService(TextService textService, ZipService zipService, ApplicationSettings applicationSettings) {
        this.textService = textService;
        this.zipService = zipService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void placeOrder(ItemOrder itemOrder) throws InterruptedException, ExecutionException {
        // Make async calls to get resources
        List<Future<TextResource>> textResourceFutures = itemOrder.getTextRequests().stream()
                        .map(request -> textService.getTextResourceAsync(request))
                        .collect(Collectors.toList());

        // Gather all async calls
        List<ItemResource> itemResources = new ArrayList<>();
        for (Future<TextResource> textResourceFuture : textResourceFutures) {
            itemResources.add(textResourceFuture.get());
        }

        // Zip all files to disk
        String zipOutputPath = applicationSettings.getZipFilePath() + itemOrder.getOrderId() + ".zip";
        zipService.zipIt(zipOutputPath, itemResources);

        // TODO: Call DeliveryMetadataRepository (own microservice) and store information about the order

        // TODO: Send email (own microservice) to customer with link to download order
    }
}
