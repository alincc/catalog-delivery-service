package no.nb.microservices.delivery.core.email.service;

import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.email.model.Email;

/**
 * Created by andreasb on 15.07.15.
 */
public interface IEmailService {
    void sendEmail(Email email);

    void sendEmail(Order order);
}
