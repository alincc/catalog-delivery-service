package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.core.order.service.OrderService;
import no.nb.microservices.delivery.model.order.DeliveryOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<String> placeOrder(@RequestBody @Valid DeliveryOrderRequest deliveryOrderRequest) throws ExecutionException, InterruptedException, IOException {
        orderService.placeOrder(deliveryOrderRequest);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/{key}")
    public void downloadOrder(@PathVariable("key") String key,
                              HttpServletResponse response) throws IOException {
        File order = orderService.getOrder(key);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + order.getName());
        response.getOutputStream().write(Files.readAllBytes(order.toPath()));
    }
}
