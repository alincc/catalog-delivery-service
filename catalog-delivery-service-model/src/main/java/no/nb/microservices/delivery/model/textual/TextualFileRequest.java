package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.order.DeliveryFileRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by andreasb on 19.08.15.
 */
public class TextualFileRequest extends DeliveryFileRequest {

    private boolean text;

    @Length(max = 6)
    private String pageSelection;

    @Size(max = 64)
    private List<TextualResourceRequest> resources;

    public boolean isImages() {
        if ("jp2".equalsIgnoreCase(super.getFormat()) ||
                "jpg".equalsIgnoreCase(super.getFormat()) ||
               "tif".equalsIgnoreCase(super.getFormat())) {
            return true;
        }
        return false;
    }

    public String getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(String pageSelection) {
        this.pageSelection = pageSelection;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public List<TextualResourceRequest> getResources() {
        return resources;
    }

    public void setResources(List<TextualResourceRequest> resources) {
        this.resources = resources;
    }
}
