package no.nb.microservices.delivery.model.request;

public enum PrintFormat {
    PDF("pdf"),
    EPUB("epub"),
    ALTO("alto"),
    TXT("txt"),
    TIF("tif"),
    JP2("jp2"),
    JPG("jpg");

    private final String text;

    PrintFormat(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
