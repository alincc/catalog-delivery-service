package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.DeliveryFile;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.delivery.service.cloud.IDeliveryMetadataService;
import no.nb.microservices.delivery.service.cloud.IEmailService;
import no.nb.microservices.delivery.service.cloud.IItemService;
import no.nb.microservices.delivery.service.print.IPrintedService;
import no.nb.microservices.email.model.Email;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    private final IPrintedService printedService;
    private final IZipService zipService;
    private final IDeliveryMetadataService deliveryMetadataService;
    private final IItemService itemService;
    private final IEmailService emailService;
    private final ApplicationSettings applicationSettings;

    @Autowired
    public OrderService(IPrintedService printedService, IZipService zipService, IDeliveryMetadataService deliveryMetadataService, IItemService itemService, IEmailService emailService, ApplicationSettings applicationSettings) {
        this.printedService = printedService;
        this.zipService = zipService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.itemService = itemService;
        this.emailService = emailService;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void placeOrder(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException, IOException {
        // Make async calls to get printed resources
        List<Future<PrintedFile>> textualResourcesFutureList = deliveryOrderRequest.getPrints().stream()
                .map(request -> printedService.getResourceAsync(request))
                .collect(Collectors.toList());

        // Gather all async calls
        List<DeliveryFile> deliveryFiles = new ArrayList<>();
        for (Future<PrintedFile> textualResourceFuture : textualResourcesFutureList) {
            deliveryFiles.add(textualResourceFuture.get());
        }

        // Zip all files to disk
        String zipFilename = deliveryOrderRequest.getOrderId() + ".zip";
        String zipOutputPath = applicationSettings.getZipFilePath() + zipFilename;
        File zippedFile = zipService.zipIt(zipOutputPath, deliveryFiles);

        // Store information about the order
        String orderKey = RandomStringUtils.randomAlphanumeric(16);
        DeliveryOrder orderMetadata = new DeliveryOrder() {{
            setOrderId(deliveryOrderRequest.getOrderId());
            setEmailTo(deliveryOrderRequest.getEmailTo());
            setEmailCc(deliveryOrderRequest.getEmailCc());
            setExpireDate(Date.from(Instant.now().plusSeconds(604800))); // 604800 seconds = 1 week
            setFilename(zipFilename);
            setFileSizeInBytes(zippedFile.length());
            setPurpose(deliveryOrderRequest.getPurpose());
            setOrderDate(Date.from(Instant.now()));
            setKey(orderKey);
            setDownloadUrl(applicationSettings.getDownloadPath() + orderKey);
            setPrints(deliveryFiles.stream().filter(q -> q instanceof PrintedFile).map(q -> (PrintedFile) q).collect(Collectors.toList()));
        }};

        // Save order to database
        DeliveryOrder orderMetadataResponseEntity = deliveryMetadataService.saveOrder(orderMetadata);

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

    @Override
    public File getOrder(String key) {
        DeliveryOrder order = deliveryMetadataService.getOrderByIdOrKey(key);
        if (Date.from(Instant.now()).after(order.getExpireDate())) {
            throw new AccessDeniedException("This order has expired");
        }
        return new File(applicationSettings.getZipFilePath() + order.getFilename());
    }
}
