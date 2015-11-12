package no.nb.microservices.delivery.core.scheduler;

import no.nb.microservices.delivery.core.metadata.service.IDeliveryMetadataService;
import no.nb.microservices.delivery.core.order.service.IOrderService;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.delivery.model.metadata.State;
import no.nb.microservices.delivery.rest.assembler.OrderBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderSchedulerTest {

    @InjectMocks
    OrderScheduler orderScheduler;

    @Mock
    IDeliveryMetadataService deliveryMetadataService;

    @Mock
    IOrderService orderService;

    @Test
    public void processTest() {
        Order order = new OrderBuilder().build();
        when(deliveryMetadataService.getOrdersByState(eq(State.OPEN))).thenReturn(Arrays.asList(order));

        orderScheduler.process();

        verify(deliveryMetadataService, times(1)).updateOrder(eq(order));
        verify(orderService, times(1)).processOrder(eq(order));
    }
}
