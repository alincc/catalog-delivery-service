package no.nb.microservices.delivery.core.metadata.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by andreasb on 20.08.15.
 */
@Document
public class PrintedFile {
    private String format;
    private boolean text;
    private List<PrintedResource> resources;

    public PrintedFile() {
    }

    public PrintedFile(String format) {
        this.format = format;
    }

    public PrintedFile(String format, List<PrintedResource> resources) {
        this.format = format;
        this.resources = resources;
    }

    public List<PrintedResource> getResources() {
        return resources;
    }

    public void setResources(List<PrintedResource> resources) {
        this.resources = resources;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean hasText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public String getFilename() {
        if (resources.size() == 1) {
            return resources.get(0).getUrn() + "." + format;
        }
        else {
            return resources.get(0).getUrn() + "-MULTI." + format;
        }

    }
}
