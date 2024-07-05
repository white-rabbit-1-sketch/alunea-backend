package me.silvermail.backend.server.smtp.command.handler;

import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.server.smtp.ClientSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("server.smtp.command.handler")
public class MailFromCommandHandler extends AbstractCommandHandler {
    public void handle(
            ClientSession clientSession,
            String command
    ) throws Exception {
        clientSession.setSenderAddress(new InternetAddress(command.substring("MAIL FROM:".length()).trim()));

        clientSession.getWriter().write("250 OK");;
    }

    public boolean isSupport(
            ClientSession clientSession,
            String command
    ) throws Exception {
        return command.toUpperCase().startsWith("MAIL FROM:");
    }
}
