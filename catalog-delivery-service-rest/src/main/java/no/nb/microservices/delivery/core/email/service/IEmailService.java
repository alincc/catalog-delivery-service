package no.nb.microservices.delivery.core.email.service;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.email.model.Email;

public interface IEmailService {
    void sendEmail(Email email);

    void sendEmail(Order order);
}
