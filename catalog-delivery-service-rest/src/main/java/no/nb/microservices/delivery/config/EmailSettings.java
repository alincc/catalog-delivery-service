package no.nb.microservices.delivery.config;

/**
 * Created by andreasb on 24.09.15.
 */
public class EmailSettings {
    private String subject;
    private String from;
    private String template;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
