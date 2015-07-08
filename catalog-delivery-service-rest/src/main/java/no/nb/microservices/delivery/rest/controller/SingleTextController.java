package no.nb.microservices.delivery.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andreasb on 07.07.15.
 */
@RestController
public class SingleTextController {
    @RequestMapping(value = "/download/single/text/{urn}", method = RequestMethod.GET)
    public String home(@PathVariable String urn) {
        return "Hello world";
    }
}
