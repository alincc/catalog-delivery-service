package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.commons.io.compression.factory.CompressionStrategy;
import no.nb.commons.io.compression.factory.CompressionStrategyFactory;
import no.nb.microservices.delivery.config.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class SimpleCompressionService implements CompressionService {

    private final ApplicationSettings applicationSettings;
    private static final int BUFFER = 2048;

    private CompressionStrategy compressionStrategy;
    private String packageFormat;

    @Autowired
    public SimpleCompressionService(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @PostConstruct
    public void init() {
        File zipPath = new File(applicationSettings.getZipFilePath());
        if (!zipPath.exists()) {
            zipPath.mkdir();
        }
    }

    @Override
    public void openArchive(File file, String packageFormat) throws IOException {
        this.packageFormat = packageFormat;
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
