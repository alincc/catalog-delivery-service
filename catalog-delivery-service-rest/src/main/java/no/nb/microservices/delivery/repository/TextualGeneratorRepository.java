package no.nb.microservices.delivery.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by andreasb on 09.07.15.
 */
@FeignClient("catalog-pdf-generator-service")
public interface TextualGeneratorRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/generate")
    ByteArrayResource generate(@RequestParam("urn") List<String> urns,
                          @RequestParam("pages") List<String> pages,
                          @RequestParam("pageSelection") String pageSelection,
                          @RequestParam("text") boolean text,
                          @RequestParam("resolutionlevel") List<String> resolutionlevel,
                          @RequestParam("filename") String filename,
                          @RequestParam("filetype") String filetype);
}
