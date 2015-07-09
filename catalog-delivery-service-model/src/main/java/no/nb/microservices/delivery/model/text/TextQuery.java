package no.nb.microservices.delivery.model.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andreasb on 08.07.15.
 */
public class TextQuery {
    private String urn;
    private List<String> urns;
    private String pages;
    private TextFormat format;
    private String sendTo;
    private int quality;
    private boolean text;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
        urns = new ArrayList<>(Arrays.asList(this.urn));
    }

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
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

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
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
