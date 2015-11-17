package no.nb.microservices.delivery.core.metadata.repository;

import no.nb.microservices.delivery.model.metadata.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByKey(String key);

    Order findByState(String state);
}
