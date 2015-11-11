package no.nb.microservices.delivery.model.metadata;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PrintedResource {
    private String urn;
    private String pages;
    private String pageSelection;
    private String mediatype;

    public PrintedResource() {
    }

    public PrintedResource(String urn) {
        this.urn = urn;
    }

    public PrintedResource(String urn, String pages) {
        this.urn = urn;
        this.pages = pages;
    }

    public PrintedResource(String urn, String pages, String pageSelection) {
        this.urn = urn;
        this.pages = pages;
        this.pageSelection = pageSelection;
    }

    public PrintedResource(String urn, String pages, String pageSelection, String mediatype) {
        this.urn = urn;
        this.pages = pages;
        this.pageSelection = pageSelection;
        this.mediatype = mediatype;
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

    public String getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(String pageSelection) {
        this.pageSelection = pageSelection;
    }
}
