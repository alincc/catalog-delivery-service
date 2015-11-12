package no.nb.microservices.delivery.core.metadata.service;

import no.nb.microservices.delivery.core.metadata.repository.OrderRepository;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.rest.assembler.OrderBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryMetadataTest {

    @InjectMocks
    DeliveryMetadataService deliveryMetadataService;

    @Mock
    OrderRepository orderRepository;

    @Test
    public void getOrderByKeyTest() {
        String key = "123KEY";
        Order order = new OrderBuilder().withKey(key).build();
        when(orderRepository.findByKey(eq(key))).thenReturn(order);
        Order response = deliveryMetadataService.getOrderByIdOrKey(key);
        assertEquals(key, response.getKey());
    }

    @Test
    public void getOrderByIDTest() {
        Order order = new OrderBuilder().withKey("123KEY").build();
        when(orderRepository.findOne(eq(order.getOrderId()))).thenReturn(order);
        Order response = deliveryMetadataService.getOrderByIdOrKey(order.getOrderId());
        assertEquals(order.getOrderId(), response.getOrderId());
    }
}
