package no.nb.microservices.delivery.model.order;

import no.nb.microservices.delivery.model.audio.AudioRequest;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.video.VideoRequest;
import org.hibernate.validator.constraints.Email;

import java.util.List;
import java.util.UUID;

/**
 * Created by andreasb on 10.07.15.
 */
public class ItemOrder {
    private String orderId;

    @Email
    private String destinationEmail;

    @Email
    private String destinationCCEmail;
    private String purpose;
    private CompressionType compressionType;
    private List<TextualRequest> textualRequests;
    private List<AudioRequest> audioRequests;
    private List<VideoRequest> videoRequests;
    private List<PhotoRequest> photoRequests;

    public ItemOrder() {
        this.orderId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public List<TextualRequest> getTextualRequests() {
        return textualRequests;
    }

    public void setTextualRequests(List<TextualRequest> textualRequests) {
        this.textualRequests = textualRequests;
    }

    public List<AudioRequest> getAudioRequests() {
        return audioRequests;
    }

    public void setAudioRequests(List<AudioRequest> audioRequests) {
        this.audioRequests = audioRequests;
    }

    public List<VideoRequest> getVideoRequests() {
        return videoRequests;
    }

    public void setVideoRequests(List<VideoRequest> videoRequests) {
        this.videoRequests = videoRequests;
    }

    public List<PhotoRequest> getPhotoRequests() {
        return photoRequests;
    }

    public void setPhotoRequests(List<PhotoRequest> photoRequests) {
        this.photoRequests = photoRequests;
    }
}
