package no.nb.microservices.delivery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "delivery")
public class ApplicationSettings {
    private String zipFilePath;
    private String downloadPath;
    private EmailSettings email;

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public EmailSettings getEmail() {
        return email;
    }

    public void setEmail(EmailSettings email) {
        this.email = email;
    }
}
