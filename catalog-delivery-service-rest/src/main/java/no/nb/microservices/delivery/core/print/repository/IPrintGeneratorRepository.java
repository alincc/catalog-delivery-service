package no.nb.microservices.delivery.core.print.repository;

import java.io.InputStream;
import java.util.List;

public interface IPrintGeneratorRepository {
    InputStream generate(List<String> urns, List<String> pages, List<String> pageSelection,
                         Boolean addText, List<String> resolutionlevel, String filename, String filetype);
}
