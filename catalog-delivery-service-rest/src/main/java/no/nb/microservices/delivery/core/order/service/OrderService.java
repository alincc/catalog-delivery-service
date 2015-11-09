package no.nb.microservices.delivery.core.order.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import no.nb.commons.io.compression.factory.Compressible;
import no.nb.commons.io.compression.factory.CompressionStrategyFactory;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.email.service.IEmailService;
import no.nb.microservices.delivery.core.item.service.IItemService;
import no.nb.microservices.delivery.core.metadata.mapper.OrderMapper;
import no.nb.microservices.delivery.core.metadata.model.Order;
import no.nb.microservices.delivery.core.metadata.model.State;
import no.nb.microservices.delivery.core.metadata.service.IDeliveryMetadataService;
import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.service.IPrintedService;
import no.nb.microservices.delivery.model.order.OrderRequest;
import no.nb.microservices.email.model.Email;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

@Service
public class OrderService implements IOrderService {

    private final IPrintedService printedService;
    private final IDeliveryMetadataService deliveryMetadataService;
    private final IItemService itemService;
    private final IEmailService emailService;
    private final ApplicationSettings applicationSettings;
    private final DiscoveryClient disoveryClient;

    @Autowired
    public OrderService(ApplicationSettings applicationSettings, IEmailService emailService, IItemService itemService, IDeliveryMetadataService deliveryMetadataService, IPrintedService printedService, DiscoveryClient disoveryClient) {
        this.applicationSettings = applicationSettings;
        this.emailService = emailService;
        this.itemService = itemService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.printedService = printedService;
        this.disoveryClient = disoveryClient;
    }

    @Override
    public Order placeOrder(OrderRequest deliveryOrderRequest) throws InterruptedException, ExecutionException, IOException {
        String orderKey = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        Order order = OrderMapper.map(deliveryOrderRequest);
        order.setState(State.OPEN);
        order.setKey(orderKey);
        order.setFilename(order.getOrderId() + "." + order.getPackageFormat());
        order.setExpireDate(Date.from(Instant.now().plusSeconds(604800))); // 604800 seconds = 1 week
        order.setOrderDate(Date.from(Instant.now()));

        InstanceInfo instance = disoveryClient.getNextServerFromEureka("ZUULSERVER", false);
        order.setDownloadUrl(instance.getHomePageUrl() + "delivery/orders/" + orderKey);

        return deliveryMetadataService.saveOrder(order);
    }

    @Override
    public File getOrder(String key) {
        Order order = deliveryMetadataService.getOrderByIdOrKey(key);
        if (Date.from(Instant.now()).after(order.getExpireDate())) {
            throw new AccessDeniedException("This order has expired");
        }
        return new File(applicationSettings.getZipFilePath() + order.getFilename());
    }

    @Async
    @Override
    public void processOrder(Order deliveryOrder) {
        // Make async calls to get printed resources
        try {
            List<CatalogFile> printedOutputStreams = getAllResources(deliveryOrder);

            // Package all files to disk
            String packageFilename = deliveryOrder.getOrderId() + "." + deliveryOrder.getPackageFormat();
            String packagePath = applicationSettings.getZipFilePath() + packageFilename;
            File zippedFile = CompressionStrategyFactory.create(deliveryOrder.getPackageFormat())
                    .compress(printedOutputStreams.stream().map(q -> (Compressible)q).collect(Collectors.toList()), packagePath);

            // Send email to user with download details
            sendConfirmationEmail(deliveryOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // TODO: Mark as error
        } catch (ExecutionException e) {
            e.printStackTrace();
            // TODO: Mark as error
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // TODO: Mark as error
        }
    }

    private List<CatalogFile> getAllResources(Order deliveryOrder) throws InterruptedException, ExecutionException {
        List<Future<CatalogFile>> textualResourcesFutureList = deliveryOrder.getPrints().stream()
                .map(request -> printedService.getResourceAsync(request, deliveryOrder.getPackageFormat()))
                .collect(Collectors.toList());

        // Gather all async calls
        List<CatalogFile> deliveryFiles = new ArrayList<>();
        for (Future<CatalogFile> textualResourceFuture : textualResourcesFutureList) {
            deliveryFiles.add(textualResourceFuture.get());
        }

        return deliveryFiles;
    }

    private void sendConfirmationEmail(final Order deliveryOrder) {
        Email email = new Email();
        email.setTo(deliveryOrder.getEmailTo());
        email.setCc(deliveryOrder.getEmailCc());
        email.setSubject(applicationSettings.getEmail().getSubject());
        email.setFrom(applicationSettings.getEmail().getFrom());
        email.setTemplate(applicationSettings.getEmail().getTemplate());
        email.setContent(deliveryOrder);

        emailService.sendEmail(email);
    }
}
