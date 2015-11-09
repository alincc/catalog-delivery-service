package no.nb.microservices.delivery.core.order.model;

import no.nb.commons.io.compression.factory.Compressible;

import java.io.IOException;
import java.io.InputStream;

public class CatalogFile implements Compressible {

    private String filename;
    private InputStream inputStream;
    private int length;

    public CatalogFile(String filename, InputStream inputStream) {
        this.filename = filename;
        this.inputStream = inputStream;
    }

    public CatalogFile(String filename, InputStream inputStream, int length) {
        this.filename = filename;
        this.inputStream = inputStream;
        this.length = length;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public long length() {
        return length;
    }
}
