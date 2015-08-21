package no.nb.microservices.delivery.model.order;

import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import org.hibernate.validator.constraints.Email;

import java.util.List;
import java.util.UUID;

/**
 * Created by andreasb on 10.07.15.
 */
public class DeliveryOrderRequest {
    private String orderId;

    @Email
    private String emailTo;

    @Email
    private String emailCc;

    private String purpose;
    private CompressionType compressionType;
    private List<TextualFileRequest> textualFileRequests;

    public DeliveryOrderRequest() {
        this.orderId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
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

    public List<TextualFileRequest> getTextualFileRequests() {
        return textualFileRequests;
    }

    public void setTextualFileRequests(List<TextualFileRequest> textualFileRequests) {
        this.textualFileRequests = textualFileRequests;
    }
}
