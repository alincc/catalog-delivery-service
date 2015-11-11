package no.nb.microservices.delivery.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.List;

public class PrintedFileRequest {

    @Size(min = 1, max = 64)
    private List<PrintedResourceRequest> resources;

    private PrintFormat format = PrintFormat.PDF;

    @Max(6)
    private int quality = 3;

    private boolean text = false;

    public PrintedFileRequest() {
    }

    public PrintedFileRequest(List<PrintedResourceRequest> resources) {
        this.resources = resources;
    }

    public PrintedFileRequest(List<PrintedResourceRequest> resources, PrintFormat format) {
        this.resources = resources;
        this.format = format;
    }

    public PrintedFileRequest(List<PrintedResourceRequest> resources, PrintFormat format, int quality) {
        this.resources = resources;
        this.format = format;
        this.quality = quality;
    }

    public PrintedFileRequest(List<PrintedResourceRequest> resources, PrintFormat format, int quality, boolean text) {
        this.resources = resources;
        this.format = format;
        this.quality = quality;
        this.text = text;
    }

    public List<PrintedResourceRequest> getResources() {
        return resources;
    }

    public void setResources(List<PrintedResourceRequest> resources) {
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
}