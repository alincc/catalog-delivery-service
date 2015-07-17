package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.generic.ItemResource;
import org.springframework.core.io.ByteArrayResource;

/**
 * Created by andreasb on 09.07.15.
 */
public class TextualResource extends ItemResource {
    private TextualFormat format;

    public TextualResource(String urn, TextualFormat format, ByteArrayResource content) {
        this.urn = urn;
        this.format = format;
        this.content = content;
    }

    public TextualFormat getFormat() {
        return format;
    }

    public void setFormat(TextualFormat format) {
        this.format = format;
    }
}
