package no.nb.microservices.delivery.service.order;

import no.nb.microservices.email.model.Email;

/**
 * Created by andreasb on 15.07.15.
 */
public interface IEmailService {
    void sendEmail(Email email);
}
