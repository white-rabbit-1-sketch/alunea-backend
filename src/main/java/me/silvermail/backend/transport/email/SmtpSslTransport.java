package me.silvermail.backend.transport.email;

import jakarta.mail.Session;
import jakarta.mail.URLName;

public class SmtpSslTransport extends SmtpTransport {
    public SmtpSslTransport(Session session, URLName urlname) {
        super(session, urlname, "smtps", true);
    }
}