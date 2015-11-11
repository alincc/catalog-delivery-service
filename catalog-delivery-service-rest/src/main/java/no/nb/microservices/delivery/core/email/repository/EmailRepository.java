package no.nb.microservices.delivery.core.email.repository;

import no.nb.microservices.email.model.Email;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("email-service")
public interface EmailRepository {
    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendEmail(Email email);
}
