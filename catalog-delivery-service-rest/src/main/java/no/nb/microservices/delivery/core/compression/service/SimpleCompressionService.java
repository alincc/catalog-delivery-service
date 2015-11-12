package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.commons.io.compression.factory.CompressionStrategyFactory;
import no.nb.microservices.delivery.config.ApplicationSettings;
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
    public void compress(File file, List<Compressible> compressible) throws IOException {
        CompressionStrategyFactory.create(FilenameUtils.getExtension(file.getPath())).compress(file, compressible);
    }

    @Override
    public void compress(Order order, List<Compressible> compressible) throws IOException {
        File output = new File(applicationSettings.getZipFilePath() + order.getFilename());
        CompressionStrategyFactory.create(FilenameUtils.getExtension(output.getPath())).compress(output, compressible);
    }
}
