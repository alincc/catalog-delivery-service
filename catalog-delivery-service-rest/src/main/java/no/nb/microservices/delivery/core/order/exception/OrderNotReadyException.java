package no.nb.microservices.delivery.core.order.exception;

public class OrderNotReadyException extends RuntimeException {
    public OrderNotReadyException(String message) {
        super(message);
    }

    public OrderNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }
}
