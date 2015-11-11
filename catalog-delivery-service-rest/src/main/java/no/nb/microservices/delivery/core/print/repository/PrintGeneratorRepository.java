package no.nb.microservices.delivery.core.print.repository;

import feign.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("catalog-pdf-generator-service")
public interface PrintGeneratorRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/generate")
    Response generate(@RequestParam("urn") List<String> urns,
                      @RequestParam("pages") List<String> pages,
                      @RequestParam("pageSelection") List<String> pageSelection,
                      @RequestParam("addText") Boolean addText,
                      @RequestParam("resolutionlevel") List<String> resolutionlevel,
                      @RequestParam("filename") String filename,
                      @RequestParam("filetype") String filetype);

}
