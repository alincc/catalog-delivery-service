package no.nb.microservices.delivery.model.textual;

import no.nb.microservices.delivery.model.generic.ItemRequest;

/**
 * Created by andreasb on 08.07.15.
 */
public class TextualRequest extends ItemRequest {
    private String pages;
    private TextualFormat format;
    private int quality;
    private boolean text;

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public TextualFormat getFormat() {
        return format;
    }

    public void setFormat(TextualFormat format) {
        this.format = format;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean isImages() {
        if (TextualFormat.BMP.equals(this.format) ||
                TextualFormat.JP2.equals(this.format) ||
                TextualFormat.JPEG.equals(this.format) ||
                TextualFormat.PNG.equals(this.format) ||
                TextualFormat.TIFF.equals(this.format)) {
           return true;
        }
        return false;
    }
}
