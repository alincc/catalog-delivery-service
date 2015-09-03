package no.nb.microservices.delivery.model.printed;

import no.nb.microservices.delivery.model.order.DeliveryFileRequest;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by andreasb on 19.08.15.
 */
public class PrintedFileRequest extends DeliveryFileRequest {

    @Size(max = 64)
    private List<PrintedResourceRequest> resources;

    public PrintedFileRequest() {
    }

    public PrintedFileRequest(String filename, String format, List<PrintedResourceRequest> resources) {
        super(filename, format);
        this.resources = resources;
    }

    public boolean isImages() {
        if ("jp2".equalsIgnoreCase(super.getFormat()) ||
                "jpg".equalsIgnoreCase(super.getFormat()) ||
                "tif".equalsIgnoreCase(super.getFormat())) {
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

}