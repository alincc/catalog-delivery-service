package no.nb.microservices.delivery.model.printed;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * Created by andreasb on 08.07.15.
 */
public class PrintedResourceRequest {

    @Length(max = 64)
    private String urn;

    @Max(6)
    private int quality;

    @Length(max = 64)
    private String pages;

    @Length(max = 6)
    private String pageSelection;

    private boolean isText;

    public PrintedResourceRequest() {}


    public PrintedResourceRequest(String urn, int quality) {
        this.urn = urn;
        this.quality = quality;
    }

    public PrintedResourceRequest(String urn, int quality, String pages, String pageSelection, boolean isText) {
        this.urn = urn;
        this.quality = quality;
        this.pages = pages;
        this.pageSelection = pageSelection;
        this.isText = isText;
    }

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

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(String pageSelection) {
        this.pageSelection = pageSelection;
    }

    public boolean isText() {
        return isText;
    }

    public void setIsText(boolean isText) {
        this.isText = isText;
    }
}
