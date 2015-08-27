package no.nb.microservices.delivery.model.order;

import org.hibernate.validator.constraints.Length;

/**
 * Created by andreasb on 13.07.15.
 */
public abstract class DeliveryResourceRequest {

    @Length(max = 64)
    private String urn;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }
}
