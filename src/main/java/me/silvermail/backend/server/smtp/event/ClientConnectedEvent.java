package me.silvermail.backend.server.smtp.event;

import me.silvermail.backend.server.smtp.ClientSession;
import org.springframework.context.ApplicationEvent;

public class ClientConnectedEvent extends ApplicationEvent {
    public ClientConnectedEvent(
            ClientSession clientSession
    ) {
        super(clientSession);
    }

    public ClientSession getClientSession() {
        return (ClientSession) source;
    }
}