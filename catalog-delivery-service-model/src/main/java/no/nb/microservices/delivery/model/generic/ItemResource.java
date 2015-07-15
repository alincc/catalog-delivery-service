package no.nb.microservices.delivery.model.generic;

import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;
import org.springframework.core.io.ByteArrayResource;

/**
 * Created by andreasb on 14.07.15.
 */
public abstract class ItemResource {
    protected String urn;
    protected String title;
    protected String extension;
    protected ByteArrayResource content;

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ByteArrayResource getContent() {
        return content;
    }

    public void setContent(ByteArrayResource content) {
        this.content = content;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFilename() {
        return this.getTitle() + "." + extension;
    }


}
