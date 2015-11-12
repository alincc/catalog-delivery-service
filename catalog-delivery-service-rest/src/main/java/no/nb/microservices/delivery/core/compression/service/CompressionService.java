package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.microservices.delivery.model.metadata.Order;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CompressionService {
    void compress(File output, List<Compressible> compressible) throws IOException;
    void compress(Order order, List<Compressible> compressible) throws IOException;
}
