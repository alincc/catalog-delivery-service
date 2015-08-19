package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.service.order.IOrderService;
import no.nb.microservices.delivery.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class OrderItemController {

    private final OrderService orderService;

    @Autowired
    public OrderItemController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<String> orderItems(@RequestBody @Valid ItemOrder itemOrder) throws ExecutionException, InterruptedException {
        orderService.placeOrder(itemOrder);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
