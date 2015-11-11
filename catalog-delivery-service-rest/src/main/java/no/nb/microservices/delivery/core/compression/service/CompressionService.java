package no.nb.microservices.delivery.core.compression.service;

import no.nb.commons.io.compression.factory.Compressible;
import no.nb.microservices.delivery.model.metadata.Order;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CompressionService {
    File compress(String path, List<Compressible> compressible) throws IOException;
    File compress(Order order, List<Compressible> catalogFiles) throws IOException;
}
