package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.core.order.service.IOrderService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/delivery")
public class OrderController {

    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    // TODO: Must have user role
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> placeOrder(@RequestBody @Valid OrderRequest deliveryOrderRequest) {
        return new ResponseEntity<Order>(orderService.placeOrder(deliveryOrderRequest), HttpStatus.OK);
    }

    // TODO: Must have admin role
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<Page<Order>> getOrders(@PageableDefault Pageable pageable) {
        return new ResponseEntity<Page<Order>>(orderService.getOrders(pageable), HttpStatus.OK);
    }

    // TODO: Must have user role
    @RequestMapping(value = "/orders/{key}")
    public void downloadOrder(@PathVariable("key") String key, HttpServletResponse response) throws IOException {
        File order = orderService.getOrder(key);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + order.getName());

        int buffer = 2048;
        byte[] data = new byte[buffer];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(order))) {
            int count;
            while ((count = bis.read(data, 0, buffer)) != -1) {
                response.getOutputStream().write(data, 0, count);
            }
        }
    }
}
