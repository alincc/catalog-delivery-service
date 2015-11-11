package no.nb.microservices.delivery.core.metadata.repository;

import no.nb.microservices.delivery.model.metadata.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByKey(String key);
    List<Order> findByState(String state);
}
