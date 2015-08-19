package no.nb.microservices.delivery.model.generic;

import no.nb.microservices.delivery.model.audio.AudioRequest;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualFormat;
import no.nb.microservices.delivery.model.textual.TextualRequest;
import no.nb.microservices.delivery.model.video.VideoRequest;

import java.util.List;

/**
 * Created by andreasb on 18.08.15.
 */
public class FileRequest {
    private String format;
    private String filename;
    private boolean text;
    private List<TextualRequest> textualRequests;
    private List<AudioRequest> audioRequests;
    private List<VideoRequest> videoRequests;
    private List<PhotoRequest> photoRequests;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public boolean isImages() {
        if (TextualFormat.BMP.equals(this.format) ||
                TextualFormat.JP2.equals(this.format) ||
                TextualFormat.JPEG.equals(this.format) ||
                TextualFormat.PNG.equals(this.format) ||
                TextualFormat.TIFF.equals(this.format)) {
            return true;
        }
        return false;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isText() {
        return text;
    }
}
