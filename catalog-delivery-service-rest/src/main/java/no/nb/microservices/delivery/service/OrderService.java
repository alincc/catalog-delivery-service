package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.ItemMetadata;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.delivery.model.generic.ItemResource;
import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreasb on 10.07.15.
 */
@Service
public class OrderService implements IOrderService {

    private PrintedMaterialService printedMaterialService;
    private ZipService zipService;
    private DeliveryMetadataService deliveryMetadataService;
    private EmailService emailService;
    private ApplicationSettings applicationSettings;

    @Autowired
    public OrderService(PrintedMaterialService printedMaterialService, ZipService zipService, DeliveryMetadataService deliveryMetadataService, EmailService emailService, ApplicationSettings applicationSettings) {
        this.printedMaterialService = printedMaterialService;
        this.zipService = zipService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.emailService = emailService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void placeOrder(ItemOrder itemOrder) throws InterruptedException, ExecutionException {
        // Make async calls to get printed resources
        List<Future<List<PrintedMaterialResource>>> printedMaterialResourceFutures = itemOrder.getPrintedMaterialRequests().stream()
                        .map(request -> printedMaterialService.getPrintedMaterialResourcesAsync(request))
                        .collect(Collectors.toList());

        // Make async calls to get video resources
        // Make async calls to get audio resources
        // Make async calls to get photo resources

        // Gather all async calls
        List<ItemResource> itemResources = new ArrayList<>();
        for (Future<List<PrintedMaterialResource>> printedMaterialResourceFuture : printedMaterialResourceFutures) {
            itemResources.addAll(printedMaterialResourceFuture.get());
        }

        // Zip all files to disk
        String zipFilename = itemOrder.getOrderId() + ".zip";
        String zipOutputPath = applicationSettings.getZipFilePath() + zipFilename;
        File zippedFile = zipService.zipIt(zipOutputPath, itemResources);

        // Store information about the order
        OrderMetadata orderMetadata = new OrderMetadata() {{
            setOrderId(itemOrder.getOrderId());
            setDestinationEmail(itemOrder.getDestinationEmail());
            setDestinationCCEmail(itemOrder.getDestinationCCEmail());
            setExpireDate(Date.from(Instant.now().plusSeconds(604800))); // 1 week
            setFilename(zipFilename);
            setFileSizeInBytes(zippedFile.length());
            setPurpose(itemOrder.getPurpose());
            setOrderDate(Date.from(Instant.now()));
            setKey(RandomStringUtils.random(16));
            setItemMetadatas(itemResources.stream().map(resource -> mapItem(resource)).collect(Collectors.toList()));
        }};

        // Save order to database
        OrderMetadata orderMetadataResponseEntity = deliveryMetadataService.saveOrder(orderMetadata);

        // Send email to user with download details
        emailService.sendDeliveryEmail(orderMetadataResponseEntity);
    }

    private ItemMetadata mapItem(ItemResource itemResource) {
        ItemMetadata itemMetadata = new ItemMetadata() {{
            setUrn(itemResource.getUrn());
        }};

        return itemMetadata;
    }
}
