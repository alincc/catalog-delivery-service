package no.nb.microservices.delivery.core.print.exception;

public class PrintException extends RuntimeException {
    public PrintException(String message, Throwable cause) {
        super(message, cause);
    }
}
