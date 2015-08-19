package no.nb.microservices.delivery.service.order;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import no.nb.microservices.delivery.repository.EmailRepository;
import no.nb.microservices.email.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by andreasb on 15.07.15.
 */
@Service
public class EmailService implements IEmailService {

    private EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "sendEmailFallback")
    public void sendEmail(Email email) {
        emailRepository.sendEmail(email);
    }

    private void sendEmailFallback(Email email) {
        // TODO: Send email fallback
    }
}
