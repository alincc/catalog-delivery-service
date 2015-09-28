package no.nb.microservices.delivery.core.order.service;

import no.nb.commons.io.packaging.factory.PackageFactory;
import no.nb.commons.io.packaging.service.Packable;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.email.service.IEmailService;
import no.nb.microservices.delivery.core.item.service.IItemService;
import no.nb.microservices.delivery.core.metadata.service.IDeliveryMetadataService;
import no.nb.microservices.delivery.core.print.service.IPrintedService;
import no.nb.microservices.delivery.metadata.model.DeliveryFile;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.PrintedFile;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.email.model.Email;
import org.apache.commons.lang3.RandomStringUtils;
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
    private final IDeliveryMetadataService deliveryMetadataService;
    private final IItemService itemService;
    private final IEmailService emailService;
    private final ApplicationSettings applicationSettings;
    private final PackageFactory packageFactory;

    @Autowired
    public OrderService(IPrintedService printedService, IDeliveryMetadataService deliveryMetadataService, IItemService itemService, IEmailService emailService, ApplicationSettings applicationSettings, PackageFactory packageFactory) {
        this.printedService = printedService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.itemService = itemService;
        this.emailService = emailService;
        this.applicationSettings = applicationSettings;
        this.packageFactory = packageFactory;
    }

    @Override
    public void placeOrder(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException, IOException {
        // Make async calls to get printed resources
        List<DeliveryFile> deliveryFiles = getAllResources(deliveryOrderRequest);

        // Package all files to disk
        String packageFilename = deliveryOrderRequest.getOrderId() + "." + deliveryOrderRequest.getPackageFormat();
        String packagePath = applicationSettings.getZipFilePath() + packageFilename;
        File zippedFile = packageFactory.getPackageService(deliveryOrderRequest.getPackageFormat())
                .packToPath(deliveryFiles.stream().map(q -> (Packable) q).collect(Collectors.toList()), packagePath);

        // Store information about the order
        DeliveryOrder orderMetadataResponseEntity = saveOrder(deliveryOrderRequest, deliveryFiles, packageFilename, zippedFile);

        // Send email to user with download details
        sendConfirmationEmail(orderMetadataResponseEntity);
    }

    private List<DeliveryFile> getAllResources(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException {
        deliveryOrderRequest.getPrints().stream().forEach(q -> q.setPackageFormat(deliveryOrderRequest.getPackageFormat()));
        List<Future<PrintedFile>> textualResourcesFutureList = deliveryOrderRequest.getPrints().stream()
                .map(request -> printedService.getResourceAsync(request))
                .collect(Collectors.toList());

        // Gather all async calls
        List<DeliveryFile> deliveryFiles = new ArrayList<>();
        for (Future<PrintedFile> textualResourceFuture : textualResourcesFutureList) {
            deliveryFiles.add(textualResourceFuture.get());
        }

        return deliveryFiles;
    }

    private DeliveryOrder saveOrder(final DeliveryOrderRequest deliveryOrderRequest, final List<DeliveryFile> deliveryFiles, final String zipFilename, final File zippedFile) {
        String orderKey = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
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
        return deliveryMetadataService.saveOrder(orderMetadata);
    }

    private void sendConfirmationEmail(final DeliveryOrder orderMetadataResponseEntity) {
        Email email = new Email() {{
            setTo(orderMetadataResponseEntity.getEmailTo());
            setCc(orderMetadataResponseEntity.getEmailCc());
            setSubject(applicationSettings.getEmail().getSubject());
            setFrom(applicationSettings.getEmail().getFrom());
            setTemplate(applicationSettings.getEmail().getTemplate());
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
