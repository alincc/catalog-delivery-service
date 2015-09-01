package no.nb.microservices.delivery.model.textual;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * Created by andreasb on 08.07.15.
 */
public class TextualResourceRequest {

    @Length(max = 64)
    private String urn;

    @Length(max = 64)
    private String pages;

    @Max(6)
    private int quality;

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
}
