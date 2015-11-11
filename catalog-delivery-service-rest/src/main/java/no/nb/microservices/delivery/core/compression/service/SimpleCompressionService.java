package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.commons.io.compression.factory.CompressionStrategyFactory;
import no.nb.microservices.delivery.config.ApplicationSettings;
import no.nb.microservices.delivery.core.order.model.CatalogFile;
import no.nb.microservices.delivery.model.metadata.Order;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SimpleCompressionService implements CompressionService {

    private final ApplicationSettings applicationSettings;

    @Autowired
    public SimpleCompressionService(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @Override
    public File compress(String path, List<Compressible> compressible) throws IOException {
        return CompressionStrategyFactory.create(FilenameUtils.getExtension(path)).compress(compressible, path);
    }

    @Override
    public File compress(Order order, List<Compressible> catalogFiles) throws IOException {
        String filename = order.getOrderId() + "." + order.getPackageFormat();
        String path = applicationSettings.getZipFilePath() + filename;

        return CompressionStrategyFactory.create(FilenameUtils.getExtension(path)).compress(catalogFiles, path);
    }
}
