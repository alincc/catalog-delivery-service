package no.nb.microservices.delivery.model.printed;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by andreasb on 19.08.15.
 */
public class PrintedFileRequest {

    @Length(max = 4)
    private String format;

    @Size(max = 64)
    private List<PrintedResourceRequest> resources;

    public PrintedFileRequest() {
    }

    public PrintedFileRequest(String format, List<PrintedResourceRequest> resources) {
        this.format = format;
        this.resources = resources;
    }

    public boolean isImages() {
        if ("jp2".equalsIgnoreCase(this.getFormat()) ||
                "jpg".equalsIgnoreCase(this.getFormat()) ||
                "tif".equalsIgnoreCase(this.getFormat())) {
            return true;
        }
        return false;
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
}