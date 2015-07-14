package no.nb.microservices.delivery.model.printedMaterial;

import no.nb.microservices.delivery.model.generic.ItemResource;
import org.springframework.core.io.ByteArrayResource;

/**
 * Created by andreasb on 09.07.15.
 */
public class PrintedMaterialResource extends ItemResource {
    private PrintedMaterialFormat format;

    public PrintedMaterialResource(String urn, PrintedMaterialFormat format, ByteArrayResource content) {
        this.urn = urn;
        this.format = format;
        this.content = content;
    }

    public PrintedMaterialFormat getFormat() {
        return format;
    }

    public void setFormat(PrintedMaterialFormat format) {
        this.format = format;
    }
}
