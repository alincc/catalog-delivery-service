package no.nb.microservices.delivery.model.metadata;

import no.nb.microservices.delivery.model.request.PrintFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class PrintedFile {
    private String filename;
    private String extension;
    private PrintFormat format;
    private int quality;
    private boolean text;
    private List<PrintedResource> resources;

    public PrintedFile() {
    }

    public PrintedFile(List<PrintedResource> resources, PrintFormat format) {
        this.resources = resources;
        this.format = format;
    }

    public PrintedFile(List<PrintedResource> resources, PrintFormat format, int quality) {
        this.resources = resources;
        this.format = format;
        this.quality = quality;
    }

    public PrintedFile(List<PrintedResource> resources, PrintFormat format, int quality, boolean text) {
        this.resources = resources;
        this.format = format;
        this.quality = quality;
        this.text = text;
    }

    public PrintedFile(List<PrintedResource> resources, PrintFormat format, int quality, boolean text, String extension, String filename) {
        this.resources = resources;
        this.format = format;
        this.quality = quality;
        this.text = text;
        this.extension = extension;
        this.filename = filename;
    }

    public PrintedFile(List<PrintedResource> resources) {
        this.resources = resources;
    }

    public PrintFormat getFormat() {
        return format;
    }

    public void setFormat(PrintFormat format) {
        this.format = format;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<PrintedResource> getResources() {
        return resources;
    }

    public void setResources(List<PrintedResource> resources) {
        this.resources = resources;
    }
}
