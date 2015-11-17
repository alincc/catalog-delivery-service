package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.commons.io.compression.factory.CompressionStrategy;
import no.nb.commons.io.compression.factory.CompressionStrategyFactory;
import no.nb.microservices.delivery.config.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SimpleCompressionService implements CompressionService {

    private final ApplicationSettings applicationSettings;
    private CompressionStrategy compressionStrategy;

    @Autowired
    public SimpleCompressionService(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @Override
    public void openArchive(File file, String packageFormat) throws IOException {
        compressionStrategy = CompressionStrategyFactory.create(packageFormat);
        compressionStrategy.openArchive(file);
    }

    @Override
    public void addEntry(Compressible entry) throws IOException {
        compressionStrategy.addEntry(entry);
    }

    @Override
    public void closeArchive() throws IOException {
        compressionStrategy.closeArchive();
    }
}
