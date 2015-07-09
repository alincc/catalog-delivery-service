package no.nb.microservices.delivery.repository;

import com.netflix.client.http.HttpResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by andreasb on 09.07.15.
 */
@FeignClient("catalog-pdf-generator-service")
public interface PdfGeneratorRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/pdf/generate")
    ByteArrayResource generate(@RequestParam("urn") List<String> urns,
                          @RequestParam("pages") String[] pages,
                          @RequestParam("type") String type,
                          @RequestParam("text") boolean text,
                          @RequestParam("resolutionlevel") List<String> resolutionlevel,
                          @RequestParam("filename") String filename,
                          @RequestParam("fileType") String filetype);
}
