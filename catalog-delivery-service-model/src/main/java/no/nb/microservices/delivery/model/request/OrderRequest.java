package no.nb.microservices.delivery.model.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public class OrderRequest {
    private String orderId;

    @Email
    private String emailTo;

    @Email
    private String emailCc;

    @Length(max = 360)
    private String purpose;

    @Length(max = 6)
    private String packageFormat = "zip";

    @Size(max = 64)
    private List<PrintedFileRequest> prints;

    public OrderRequest() {
        this.orderId = UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat;
    }

    public List<PrintedFileRequest> getPrints() {
        return prints;
    }

    public void setPrints(List<PrintedFileRequest> prints) {
        this.prints = prints;
    }
}
