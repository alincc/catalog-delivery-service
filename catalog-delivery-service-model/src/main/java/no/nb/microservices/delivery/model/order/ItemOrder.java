package no.nb.microservices.delivery.model.order;

import no.nb.microservices.delivery.model.audio.AudioRequest;
import no.nb.microservices.delivery.model.text.TextRequest;
import no.nb.microservices.delivery.model.video.VideoRequest;

import java.util.List;
import java.util.UUID;

/**
 * Created by andreasb on 10.07.15.
 */
public class ItemOrder {
    private String orderId;
    private String destinationEmail;
    private String destinationCCEmail;
    private String purpose;
    private CompressionType compressionType;
    private List<TextRequest> textRequests;
    private List<AudioRequest> audioRequests;
    private List<VideoRequest> videoRequests;

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

    public List<TextRequest> getTextRequests() {
        return textRequests;
    }

    public void setTextRequests(List<TextRequest> textRequests) {
        this.textRequests = textRequests;
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
}
