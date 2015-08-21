package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.order.DeliveryFileRequest;

import java.util.List;

/**
 * Created by andreasb on 19.08.15.
 */
public class TextualFileRequest extends DeliveryFileRequest {

    private boolean text;
    private List<TextualResourceRequest> textualResourceRequests;

    public boolean isImages() {
        if (TextualFormat.BMP.equals(super.getFormat()) ||
                TextualFormat.JP2.equals(super.getFormat()) ||
                TextualFormat.JPEG.equals(super.getFormat()) ||
                TextualFormat.PNG.equals(super.getFormat()) ||
                TextualFormat.TIFF.equals(super.getFormat())) {
            return true;
        }
        return false;
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
