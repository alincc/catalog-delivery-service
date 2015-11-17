package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;

import java.io.File;
import java.io.IOException;

public interface CompressionService {
    void openArchive(File file, String packageFormat) throws IOException;

    void addEntry(Compressible entry) throws IOException;

    void closeArchive() throws IOException;
}
