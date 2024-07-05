package me.silvermail.backend.transport.email;

import jakarta.mail.*;
import org.eclipse.angus.mail.smtp.SMTPTransport;

public class SmtpTransport extends SMTPTransport {
    protected String name = null;

    public SmtpTransport(Session session, URLName urlname) {
        this(session, urlname, "smtp", false);
    }

    protected SmtpTransport(Session session, URLName urlname, String name, boolean isSSL) {
        super(session, urlname, name, isSSL);

        this.name = "smtp";
        if (urlname != null) {
            this.name = urlname.getProtocol();
        }
    }

    public synchronized void sendMessage(
            Message message,
            Address[] recipientAddresses,
            Address senderAddress
    ) throws MessagingException {
        String propertySenderKey = "mail." + this.name + ".from";
        String originalSender = this.session.getProperty(propertySenderKey);
        this.session.getProperties().setProperty(propertySenderKey, senderAddress.toString());

        super.sendMessage(message, recipientAddresses);

        if (originalSender != null) {
            this.session.getProperties().setProperty(propertySenderKey, originalSender);
        } else {
            this.session.getProperties().remove(propertySenderKey);
        }
    }
}