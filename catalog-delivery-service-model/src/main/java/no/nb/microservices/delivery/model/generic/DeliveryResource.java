package no.nb.microservices.delivery.model.generic;

import org.springframework.core.io.ByteArrayResource;

import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
public class DeliveryResource {
    protected List<String> urn;
    protected String filename;
    protected String extension;
    protected ByteArrayResource content;

    public DeliveryResource(List<String> urn, String extension, ByteArrayResource content) {
        this.urn = urn;
        this.extension = extension;
        this.content = content;
    }

    public DeliveryResource(List<String> urn, String filename, String extension, ByteArrayResource content) {
        this.urn = urn;
        this.filename = filename;
        this.extension = extension;
        this.content = content;
    }

    public List<String> getUrn() {
        return urn;
    }

    public void setUrn(List<String> urn) {
        this.urn = urn;
    }

    public ByteArrayResource getContent() {
        return content;
    }

    public void setContent(ByteArrayResource content) {
        this.content = content;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFullName() {
        return this.filename + "." + this.extension;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
