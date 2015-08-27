package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.order.DeliveryFileRequest;

import java.util.List;

/**
 * Created by andreasb on 19.08.15.
 */
public class TextualFileRequest extends DeliveryFileRequest {

    private String pageSelection;
    private boolean text;
    private List<TextualResourceRequest> textualResourceRequests;

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

    public List<TextualResourceRequest> getTextualResourceRequests() {
        return textualResourceRequests;
    }

    public void setTextualResourceRequests(List<TextualResourceRequest> textualResourceRequests) {
        this.textualResourceRequests = textualResourceRequests;
    }
}
