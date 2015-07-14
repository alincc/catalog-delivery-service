package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.delivery.model.order.ItemOrder;
import no.nb.microservices.delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class OrderItemController {

    private OrderService orderService;

    @Autowired
    public OrderItemController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<String> orderItems(@Valid ItemOrder itemOrder) throws ExecutionException, InterruptedException {
        orderService.placeOrder(itemOrder);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
