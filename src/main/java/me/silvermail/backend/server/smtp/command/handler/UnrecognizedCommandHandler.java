package me.silvermail.backend.server.smtp.command.handler;

import me.silvermail.backend.server.smtp.ClientSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("server.smtp.command.handler")
public class UnrecognizedCommandHandler extends AbstractCommandHandler {
    public void handle(
            ClientSession clientSession,
            String command
    ) throws Exception {
        clientSession.getWriter().write("500 Command unrecognized");
    }

    public boolean isSupport(
            ClientSession clientSession,
            String command
    ) throws Exception {
        return false;
    }
}
