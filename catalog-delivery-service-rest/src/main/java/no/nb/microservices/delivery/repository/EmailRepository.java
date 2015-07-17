package no.nb.microservices.delivery.repository;

import no.nb.microservices.delivery.metadata.model.ItemMetadata;
import no.nb.microservices.delivery.metadata.model.OrderMetadata;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by andreasb on 14.07.15.
 */
@FeignClient("email-service")
public interface EmailRepository {
    @RequestMapping(value = "/delivery/send", method = RequestMethod.POST)
    ResponseEntity sendDeliveryEmail(@Valid OrderMetadata orderMetadata);
}