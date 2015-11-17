package no.nb.microservices.delivery.core.print.repository;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import no.nb.microservices.delivery.core.print.exception.PrintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Repository
public class PrintGeneratorRepository implements IPrintGeneratorRepository {

    @Autowired
    private DiscoveryClient disoveryClient;

    public InputStream generate(@RequestParam("urn") List<String> urns,
                         @RequestParam("pages") List<String> pages,
                         @RequestParam("pageSelection") List<String> pageSelection,
                         @RequestParam("addText") Boolean addText,
                         @RequestParam("resolutionlevel") List<String> resolutionlevel,
                         @RequestParam("filename") String filename,
                         @RequestParam("filetype") String filetype) {

        InstanceInfo instance = disoveryClient.getNextServerFromEureka("catalog-pdf-generator-service", false);

        try {
            String urlString = UriComponentsBuilder.fromUriString(instance.getHomePageUrl() + "generate")
                    .queryParam("urn", StringUtils.arrayToCommaDelimitedString(urns.toArray()))
                    .queryParam("pages", StringUtils.arrayToCommaDelimitedString(pages.toArray()))
                    .queryParam("pageSelection", StringUtils.arrayToCommaDelimitedString(pageSelection.toArray()))
                    .queryParam("addText", addText)
                    .queryParam("resolutionlevel", StringUtils.arrayToCommaDelimitedString(resolutionlevel.toArray()))
                    .queryParam("filename", filename)
                    .queryParam("filetype", filetype)
                    .build().toUriString();
            URL url = new URL(urlString);
            return url.openStream();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            throw new PrintException("Failed to read data from service", ioe);
        }
    }
}
