package no.nb.microservices.delivery.model.order;

import org.hibernate.validator.constraints.Length;

/**
 * Created by andreasb on 18.08.15.
 */
public abstract class DeliveryFileRequest {
    @Length(max = 4)
    private String format;

    @Length(max = 64)
    private String filename;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
