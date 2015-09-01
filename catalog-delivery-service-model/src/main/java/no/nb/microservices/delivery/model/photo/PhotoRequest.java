package no.nb.microservices.delivery.model.photo;

import no.nb.microservices.delivery.model.order.DeliveryFileRequest;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * Created by andreasb on 14.07.15.
 */
public class PhotoRequest extends DeliveryFileRequest {
    @Length(max = 64)
    private String urn;

    @Max(6)
    private int quality;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
