package no.nb.microservices.delivery.model.text;

import org.springframework.core.io.ByteArrayResource;

/**
 * Created by andreasb on 09.07.15.
 */
public class TextResource {
    private String urn;
    private TextFormat format;
    private ByteArrayResource content;

    public TextResource(String urn, TextFormat format, ByteArrayResource content) {
        this.urn = urn;
        this.format = format;
        this.content = content;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public TextFormat getFormat() {
        return format;
    }

    public void setFormat(TextFormat format) {
        this.format = format;
    }

    public ByteArrayResource getContent() {
        return content;
    }

    public void setContent(ByteArrayResource content) {
        this.content = content;
    }
}
