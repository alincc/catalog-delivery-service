package no.nb.microservices.delivery.core.email.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.email.repository.EmailRepository;
import no.nb.microservices.delivery.model.metadata.Order;
import no.nb.microservices.email.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    private EmailRepository emailRepository;
    private ApplicationSettings applicationSettings;

    @Autowired
    public EmailService(EmailRepository emailRepository, ApplicationSettings applicationSettings) {
        this.emailRepository = emailRepository;
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void sendEmail(Email email) {
        emailRepository.sendEmail(email);
    }

    @Override
    public void sendEmail(Order order) {
        Email email = new Email();
        email.setTo(order.getEmailTo());
        email.setCc(order.getEmailCc());
        email.setSubject(applicationSettings.getEmail().getSubject());
        email.setFrom(applicationSettings.getEmail().getFrom());
        email.setTemplate(applicationSettings.getEmail().getTemplate());
        email.setContent(order);

        this.sendEmail(email);
    }
}
