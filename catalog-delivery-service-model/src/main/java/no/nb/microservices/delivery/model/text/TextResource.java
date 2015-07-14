package no.nb.microservices.delivery.model.text;

import no.nb.microservices.delivery.model.generic.ItemResource;
import org.springframework.core.io.ByteArrayResource;

/**
 * Created by andreasb on 09.07.15.
 */
public class TextResource extends ItemResource {
    private TextFormat format;

    public TextResource(String urn, TextFormat format, ByteArrayResource content) {
        this.urn = urn;
        this.format = format;
        this.content = content;
    }

    public TextFormat getFormat() {
        return format;
    }

    public void setFormat(TextFormat format) {
        this.format = format;
    }
}
