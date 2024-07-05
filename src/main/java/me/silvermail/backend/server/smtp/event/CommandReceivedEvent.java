package me.silvermail.backend.server.smtp.event;

import me.silvermail.backend.server.smtp.ClientSession;
import org.springframework.context.ApplicationEvent;

public class CommandReceivedEvent extends ApplicationEvent {
    protected ClientSession clientSession;

    public CommandReceivedEvent(
            ClientSession clientSession,
            String command
    ) {
        super(command);

        this.clientSession = clientSession;
    }

    public String getCommand() {
        return (String) source;
    }

    public ClientSession getClientSession() {
        return clientSession;
    }
}