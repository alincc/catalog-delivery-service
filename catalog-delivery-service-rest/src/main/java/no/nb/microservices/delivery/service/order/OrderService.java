package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.ItemMetadata;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.delivery.model.generic.DeliveryResource;
import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.service.IItemService;
import no.nb.microservices.delivery.service.ITextualService;
import no.nb.microservices.email.model.Email;
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

    private final ITextualService textualService;
    private final IZipService zipService;
    private final IDeliveryMetadataService deliveryMetadataService;
    private final IItemService itemService;
    private final IEmailService emailService;
    private final ApplicationSettings applicationSettings;

    @Autowired
    public OrderService(ITextualService textualService, IZipService zipService, IDeliveryMetadataService deliveryMetadataService, IItemService itemService, IEmailService emailService, ApplicationSettings applicationSettings) {
        this.textualService = textualService;
        this.zipService = zipService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.itemService = itemService;
        this.emailService = emailService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void placeOrder(ItemOrder itemOrder) throws InterruptedException, ExecutionException {
        // Make async calls to get textual resources
        List<Future<DeliveryResource>> textualResourcesFutureList = itemOrder.getFileRequests().stream()
                        .map(request -> textualService.getResourcesAsync(request))
                        .collect(Collectors.toList());

        // Make async calls to get video resources
        // Make async calls to get audio resources
        // Make async calls to get photo resources

        // Gather all async calls
        List<DeliveryResource> deliveryResources = new ArrayList<>();
        for (Future<DeliveryResource> textualResourceFuture : textualResourcesFutureList) {
            deliveryResources.add(textualResourceFuture.get());
        }

        // Zip all files to disk
        String zipFilename = itemOrder.getOrderId() + ".zip";
        String zipOutputPath = applicationSettings.getZipFilePath() + zipFilename;
        File zippedFile = zipService.zipIt(zipOutputPath, deliveryResources);

        // Store information about the order
        OrderMetadata orderMetadata = new OrderMetadata() {{
            setOrderId(itemOrder.getOrderId());
            setEmailTo(itemOrder.getEmailTo());
            setEmailCc(itemOrder.getEmailCc());
            setExpireDate(Date.from(Instant.now().plusSeconds(604800))); // 1 week
            setFilename(zipFilename);
            setFileSizeInBytes(zippedFile.length());
            setPurpose(itemOrder.getPurpose());
            setOrderDate(Date.from(Instant.now()));
            setKey(RandomStringUtils.randomAlphanumeric(16));
            setItems(deliveryResources.stream().map(resource -> mapItem(resource)).collect(Collectors.toList()));
        }};

        // Save order to database
        OrderMetadata orderMetadataResponseEntity = deliveryMetadataService.saveOrder(orderMetadata);

        // Send email to user with download details
        Email email = new Email() {{
            setTo(orderMetadataResponseEntity.getEmailTo());
            setCc(orderMetadataResponseEntity.getEmailCc());
            setSubject("Din bestilling");
            setFrom("bestilling@nb.no");
            setTemplate("delivery.vm");
            setContent(orderMetadataResponseEntity);

        }};
        emailService.sendEmail(email);
    }

    private ItemMetadata mapItem(DeliveryResource deliveryResource) {
        ItemMetadata itemMetadata = new ItemMetadata() {{
            //setUrn(deliveryResource.getUrn());
        }};

        return itemMetadata;
    }
}
