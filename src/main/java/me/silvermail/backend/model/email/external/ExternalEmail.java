package me.silvermail.backend.model.email.external;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ExternalEmail {
    protected InternetAddress originalSmtpSenderAddress;
    protected InternetAddress originalSmtpRecipientAddress;
    protected InternetAddress smtpSenderAddress;
    protected InternetAddress smtpRecipientAddress;

    protected MimeMessage mimeMessage;

    public ExternalEmail() {}

    public ExternalEmail(InternetAddress smtpSenderAddress, InternetAddress smtpRecipientAddress, MimeMessage mimeMessage) throws AddressException {
        this.originalSmtpSenderAddress = (InternetAddress) smtpSenderAddress.clone();
        this.originalSmtpRecipientAddress = (InternetAddress) smtpRecipientAddress.clone();
        this.smtpSenderAddress = smtpSenderAddress;
        this.smtpRecipientAddress = smtpRecipientAddress;
        this.mimeMessage = mimeMessage;
    }

    public InternetAddress getOriginalSmtpSenderAddress() {
        return originalSmtpSenderAddress;
    }

    public void setOriginalSmtpSenderAddress(InternetAddress originalSmtpSenderAddress) {
        this.originalSmtpSenderAddress = originalSmtpSenderAddress;
    }

    public InternetAddress getOriginalSmtpRecipientAddress() {
        return originalSmtpRecipientAddress;
    }

    public void setOriginalSmtpRecipientAddress(InternetAddress originalSmtpRecipientAddress) {
        this.originalSmtpRecipientAddress = originalSmtpRecipientAddress;
    }

    public InternetAddress getSmtpSenderAddress() {
        return smtpSenderAddress;
    }

    public void setSmtpSenderAddress(InternetAddress smtpSenderAddress) {
        this.smtpSenderAddress = smtpSenderAddress;
    }

    public InternetAddress getSmtpRecipientAddress() {
        return smtpRecipientAddress;
    }

    public void setSmtpRecipientAddress(InternetAddress smtpRecipientAddress) {
        this.smtpRecipientAddress = smtpRecipientAddress;
    }

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }
}
