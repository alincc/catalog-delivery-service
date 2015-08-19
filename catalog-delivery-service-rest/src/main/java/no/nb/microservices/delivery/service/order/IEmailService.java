package no.nb.microservices.delivery.service.order;

import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.email.model.Email;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * Created by andreasb on 15.07.15.
 */
public interface IEmailService {
    void sendEmail(Email email);
}
