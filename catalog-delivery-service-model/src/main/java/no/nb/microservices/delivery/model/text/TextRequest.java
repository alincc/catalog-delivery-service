package no.nb.microservices.delivery.model.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andreasb on 08.07.15.
 */
public class TextRequest {
    private String urn;
    private String pages;
    private TextFormat format;
    private int quality;
    private boolean text;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public TextFormat getFormat() {
        return format;
    }

    public void setFormat(TextFormat format) {
        this.format = format;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }
}
