package no.nb.microservices.delivery.model.printed;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

public class PrintedFileRequest {

    @Length(max = 4)
    private String format;

    private boolean text;

    @Size(min = 1, max = 64)
    private List<PrintedResourceRequest> resources;

    public PrintedFileRequest() {
    }

    public PrintedFileRequest(String format, List<PrintedResourceRequest> resources) {
        this.format = format;
        this.resources = resources;
    }

    public List<PrintedResourceRequest> getResources() {
        return resources;
    }

    public void setResources(List<PrintedResourceRequest> resources) {
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
}