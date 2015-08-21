package no.nb.microservices.delivery.model.order;

/**
 * Created by andreasb on 13.07.15.
 */
public abstract class DeliveryResourceRequest {
    private String urn;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }
}
