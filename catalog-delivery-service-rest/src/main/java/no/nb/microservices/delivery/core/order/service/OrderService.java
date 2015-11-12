package no.nb.microservices.delivery.core.order.service;

import com.netflix.discovery.DiscoveryClient;
import no.nb.commons.io.compression.factory.Compressible;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.compression.service.CompressionService;
import no.nb.microservices.delivery.core.email.service.IEmailService;
import no.nb.microservices.delivery.core.metadata.service.IDeliveryMetadataService;
import no.nb.microservices.delivery.core.order.exception.OrderFailedException;
import no.nb.microservices.delivery.core.order.exception.OrderNotReadyException;
import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.core.print.service.IPrintedService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import no.nb.microservices.delivery.model.request.OrderRequest;
import no.nb.microservices.delivery.rest.assembler.OrderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    private final IPrintedService printedService;
    private final IDeliveryMetadataService deliveryMetadataService;
    private final IEmailService emailService;
    private final ApplicationSettings applicationSettings;
    private final DiscoveryClient disoveryClient;
    private final CompressionService compressionService;

    @Autowired
    public OrderService(ApplicationSettings applicationSettings, IEmailService emailService, IDeliveryMetadataService deliveryMetadataService,
                        IPrintedService printedService, DiscoveryClient disoveryClient, CompressionService compressionService) {
        this.applicationSettings = applicationSettings;
        this.emailService = emailService;
        this.deliveryMetadataService = deliveryMetadataService;
        this.printedService = printedService;
        this.disoveryClient = disoveryClient;
        this.compressionService = compressionService;
    }

    @Override
    public Order placeOrder(OrderRequest deliveryOrderRequest) {
        Order order = new OrderBuilder(deliveryOrderRequest)
                .generateKey()
                .withDownloadPath(disoveryClient)
                .withExpireDate(604800)
                .build();

        return deliveryMetadataService.saveOrder(order);
    }

    @Override
    public File getOrder(String key) {
        Order order = deliveryMetadataService.getOrderByIdOrKey(key);

        if (Date.from(Instant.now()).after(order.getExpireDate())) {
            throw new AccessDeniedException("This order has expired");
        }
        else if (order.getState() == State.ERROR)  {
            throw new OrderFailedException("This order is marked as error");
        }
        else if (order.getState() == State.OPEN || order.getState() == State.PROCESSING)  {
            throw new OrderNotReadyException("This order is still processing");
        }
        else if (order.getState() == State.DONE) {
            return new File(applicationSettings.getZipFilePath() + order.getFilename());
        }
        else {
            throw new RuntimeException("This order has a unknown state");
        }
    }

    @Async
    @Override
    public void processOrder(Order deliveryOrder) {
        // Make async calls to get printed resources
        try {
            List<Future<CatalogFile>> textualResourcesFutureList = deliveryOrder.getPrints().stream()
                    .map(request -> printedService.getResourceAsync(request))
                    .collect(Collectors.toList());

            // Gather all async calls
            List<CatalogFile> printedOutputStreams = new ArrayList<>();
            for (Future<CatalogFile> textualResourceFuture : textualResourcesFutureList) {
                printedOutputStreams.add(textualResourceFuture.get());
            }

            // Package all files to disk
            compressionService.compress(deliveryOrder, printedOutputStreams.stream().map(q -> (Compressible) q).collect(Collectors.toList()));

            // Send email to user with download details
            emailService.sendEmail(deliveryOrder);

            deliveryOrder.setState(State.DONE);
            deliveryMetadataService.updateOrder(deliveryOrder);
        } catch (Exception e) {
            LOG.error("Failed to process order with id: " + deliveryOrder.getOrderId(), e);

            deliveryOrder.setState(State.ERROR);
            deliveryMetadataService.updateOrder(deliveryOrder);
        }
    }
}
