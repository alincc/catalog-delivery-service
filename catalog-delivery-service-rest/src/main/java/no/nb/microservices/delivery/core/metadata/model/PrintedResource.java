package no.nb.microservices.delivery.core.metadata.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by andreasb on 20.08.15.
 */
@Document
public class PrintedResource {
    private String urn;
    private String format;
    private String mediatype;
    private int quality;
    private String pages;
    private String pageSelection;

    public PrintedResource() {
    }

    public PrintedResource(String urn, String mediatype, int quality) {
        this.urn = urn;
        this.mediatype = mediatype;
        this.quality = quality;
    }

    public PrintedResource(String urn, String mediatype, int quality, String pages) {
        this.urn = urn;
        this.mediatype = mediatype;
        this.pages = pages;
        this.quality = quality;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(String pageSelection) {
        this.pageSelection = pageSelection;
    }
}
