package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.metadata.model.DeliveryFile;
import no.nb.microservices.delivery.metadata.model.DeliveryOrder;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import no.nb.microservices.delivery.service.IItemService;
import no.nb.microservices.delivery.service.ITextualService;
import no.nb.microservices.email.model.Email;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
    public void placeOrder(DeliveryOrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException {
        // Make async calls to get textual resources
        List<Future<TextualFile>> textualResourcesFutureList = deliveryOrderRequest.getTextualFileRequests().stream()
                        .map(request -> textualService.getResourcesAsync(request))
                        .collect(Collectors.toList());

        // Make async calls to get video resources
        // Make async calls to get audio resources
        // Make async calls to get photo resources

        // Gather all async calls
        List<DeliveryFile> deliveryFiles = new ArrayList<>();
        for (Future<TextualFile> textualResourceFuture : textualResourcesFutureList) {
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
            setExpireDate(Date.from(Instant.now().plusSeconds(604800))); // 1 week
            setFilename(zipFilename);
            setFileSizeInBytes(zippedFile.length());
            setPurpose(deliveryOrderRequest.getPurpose());
            setOrderDate(Date.from(Instant.now()));
            setKey(orderKey);
            setDownloadUrl(applicationSettings.getDownloadPath() + orderKey);
            setTextualFiles(deliveryFiles.stream().filter(q -> q instanceof TextualFile).map(q -> (TextualFile) q).collect(Collectors.toList()));
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

    public File getOrder(String key) {
        DeliveryOrder order = deliveryMetadataService.getOrderByIdOrKey(key);
        if (Date.from(Instant.now()).after(order.getExpireDate())) {
            throw new AccessDeniedException("This order has expired");
        }
        File zippedFile = new File(applicationSettings.getZipFilePath() + order.getFilename());
        return zippedFile;
    }
}
