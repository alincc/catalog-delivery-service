package no.nb.microservices.delivery.core.order.exception;

public class OrderFailedException extends RuntimeException {
    public OrderFailedException(String message) {
        super(message);
    }

    public OrderFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
