package no.nb.microservices.delivery.rest.global;

import no.nb.microservices.delivery.core.order.exception.OrderFailedException;
import no.nb.microservices.delivery.core.order.exception.OrderNotReadyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No access")
    public void noAccessHandler(HttpServletRequest req, Exception e) {
        LOG.warn("User have no access", e);
    }

    @ExceptionHandler(OrderFailedException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "The processing of your order has failed")
    public void orderFailedtHandler(HttpServletRequest req, Exception e) {
        LOG.error("The processing of the order has failed", e);
    }

    @ExceptionHandler(OrderNotReadyException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "The order is not finished processing")
    public void orderNotReadytHandler(HttpServletRequest req, Exception e) {
        LOG.info("The order is still processing", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "It looks like we have a internal error in our application. The error have been logged and will be looked at by our development team.")
    public void defaultHandler(HttpServletRequest req, Exception e) {

        // Build Header string
        StringBuilder headers = new StringBuilder();
        for (String headerKey : Collections.list(req.getHeaderNames())) {
            String headerValue = req.getHeader(headerKey);
            headers.append(headerKey + ": " + headerValue + ", ");
        }

        LOG.error("" +
                "Got an unexcepted exception.\n" +
                "Context Path: " + req.getContextPath() + "\n" +
                "Request URI: " + req.getRequestURI() + "\n" +
                "Query String: " + req.getQueryString() + "\n" +
                "Method: " + req.getMethod() + "\n" +
                "Headers: " + headers + "\n" +
                "Auth Type: " + req.getAuthType() + "\n" +
                "Remote User: " + req.getRemoteUser() + "\n" +
                "Username: " + ((req.getUserPrincipal() != null) ? req.getUserPrincipal().getName() : "Anonymous") + "\n"
                , e);
    }
}
