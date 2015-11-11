package no.nb.microservices.delivery.model.request;

public enum PrintFormat {
    PDF("PDF"),
    EPUB("EPUB"),
    ALTO("ALTO"),
    TXT("TXT"),
    TIF("TIF"),
    JP2("JP2"),
    JPG("JPG");

    private final String text;

    PrintFormat(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
