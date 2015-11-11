package no.nb.microservices.delivery.model.metadata;

public enum State {
    OPEN("OPEN"),
    PROCESSING("PROCESSING"),
    READY("READY"),
    CLOSED("CLOSED"),
    ERROR("ERROR");

    private final String text;

    State(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
