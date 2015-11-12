package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.core.order.service.IOrderService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.request.OrderRequest;
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

@RestController
@RequestMapping("/delivery")
public class OrderController {

    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> placeOrder(@RequestBody @Valid OrderRequest deliveryOrderRequest) {
        return new ResponseEntity<Order>(orderService.placeOrder(deliveryOrderRequest), HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/{key}")
    public void downloadOrder(@PathVariable("key") String key, HttpServletResponse response) throws IOException {
        File order = orderService.getOrder(key);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + order.getName());
        response.getOutputStream().write(Files.readAllBytes(order.toPath()));
    }
}
