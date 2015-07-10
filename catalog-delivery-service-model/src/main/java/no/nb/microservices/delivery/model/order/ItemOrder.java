package no.nb.microservices.delivery.model.order;

import no.nb.microservices.delivery.model.text.TextRequest;

import java.util.List;

/**
 * Created by andreasb on 10.07.15.
 */
public class ItemOrder {
    private String destinationEmail;
    private String destinationCCEmail;
    private String purpose;
    private CompressionType compressionType;
    private List<TextRequest> textRequestList;

    public String getDestinationEmail() {
        return destinationEmail;
    }

    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    public String getDestinationCCEmail() {
        return destinationCCEmail;
    }

    public void setDestinationCCEmail(String destinationCCEmail) {
        this.destinationCCEmail = destinationCCEmail;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    public List<TextRequest> getTextRequestList() {
        return textRequestList;
    }

    public void setTextRequestList(List<TextRequest> textRequestList) {
        this.textRequestList = textRequestList;
    }
}
