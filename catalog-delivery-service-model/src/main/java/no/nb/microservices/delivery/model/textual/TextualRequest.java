package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.generic.DeliveryRequest;

/**
 * Created by andreasb on 08.07.15.
 */
public class TextualRequest extends DeliveryRequest {
    private String pages;
    private int quality;

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
