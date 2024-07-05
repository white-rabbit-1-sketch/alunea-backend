package me.silvermail.backend.server.smtp.event.listener;

import me.silvermail.backend.server.smtp.event.ClientConnectedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ClientConnectedEventListener implements ApplicationListener<ClientConnectedEvent> {
    @Override
    public void onApplicationEvent(ClientConnectedEvent event) {
        try {
            event.getClientSession().getWriter().write("220 localhost ESMTP Alunea");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}