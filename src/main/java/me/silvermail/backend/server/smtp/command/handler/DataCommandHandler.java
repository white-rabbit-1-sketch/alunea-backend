package me.silvermail.backend.server.smtp.command.handler;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.server.smtp.exception.InvalidFromClauseException;
import me.silvermail.backend.server.smtp.exception.InvalidRcptToClauseException;
import me.silvermail.backend.server.smtp.ClientSession;
import me.silvermail.backend.service.ExternalEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Component
@Qualifier("server.smtp.command.handler")
public class DataCommandHandler extends AbstractCommandHandler {
    @Autowired
    protected ExternalEmailService externalEmailService;

    @Autowired
    protected JavaMailSender externalEmailSender;

    public void handle(
            ClientSession clientSession,
            String command
    ) throws Exception {
        clientSession.getWriter().write("354 Start mail input; end with <CRLF>.<CRLF>");

        StringBuilder emailContent = new StringBuilder();
        String line;
        while (!(line = clientSession.getReader().read()).equals(".")) {
            emailContent.append(line).append("\n");
        }

        MimeMessage mimeMessage = externalEmailSender.createMimeMessage(
                new ByteArrayInputStream(emailContent.toString().getBytes())
        );

        if (clientSession.getSenderAddress() == null) {
            throw new InvalidFromClauseException();
        }

        if (clientSession.getRecipientAddressList().isEmpty()) {
            throw new InvalidRcptToClauseException();
        }

        for (InternetAddress recipient : clientSession.getRecipientAddressList()) {
            externalEmailService.queueEmail(new ExternalEmail(
                    clientSession.getSenderAddress(),
                    recipient,
                    mimeMessage
            ));
        }

        clientSession.getWriter().write("250 Message received: " + emailContent.toString().trim().length() + " bytes");
        clientSession.setDisconnectRequired(true);
    }

    public boolean isSupport(
            ClientSession clientSession,
            String command
    ) throws Exception {
        return command.toUpperCase().startsWith("DATA");
    }
}
