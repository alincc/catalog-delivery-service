package no.nb.microservices.delivery.model.order;

import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
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

    @Length(max = 360)
    private String purpose;

    @Length(max = 6)
    private String compressionType = "zip";

    @Size(max = 64)
    private List<TextualFileRequest> textuals;

    @Size(max = 64)
    private List<PhotoRequest> photos;

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

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public List<TextualFileRequest> getTextuals() {
        return textuals;
    }

    public void setTextuals(List<TextualFileRequest> textuals) {
        this.textuals = textuals;
    }

    public List<PhotoRequest> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoRequest> photos) {
        this.photos = photos;
    }
}
