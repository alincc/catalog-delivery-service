package no.nb.microservices.delivery.model.request;

import org.hibernate.validator.constraints.Length;

public class PrintedResourceRequest {

    @Length(max = 64)
    private String urn;

    @Length(max = 64)
    private String pages = "";

    @Length(max = 6)
    private String pageSelection = "ID";

    public PrintedResourceRequest() {}

    public PrintedResourceRequest(String urn) {
        this.urn = urn;
    }

    public PrintedResourceRequest(String urn, String pages) {
        this.urn = urn;
        this.pages = pages;
    }

    public PrintedResourceRequest(String urn, String pages, String pageSelection) {
        this.urn = urn;
        this.pages = pages;
        this.pageSelection = pageSelection;
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

    public String getPageSelection() {
        return pageSelection;
    }

    public void setPageSelection(String pageSelection) {
        this.pageSelection = pageSelection;
    }
}
