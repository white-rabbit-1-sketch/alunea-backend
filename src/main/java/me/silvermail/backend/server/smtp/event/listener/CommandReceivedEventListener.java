package me.silvermail.backend.server.smtp.event.listener;

import me.silvermail.backend.exception.email.external.AbstractEmailRejectedException;
import me.silvermail.backend.server.smtp.event.CommandReceivedEvent;
import me.silvermail.backend.server.smtp.command.handler.AbstractCommandHandler;
import me.silvermail.backend.server.smtp.command.handler.UnrecognizedCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandReceivedEventListener implements ApplicationListener<CommandReceivedEvent> {
    @Autowired
    @Qualifier("server.smtp.command.handler")
    protected List<AbstractCommandHandler> commandHandlerList;

    @Autowired
    protected UnrecognizedCommandHandler unrecognizedCommandHandler;

    @Override
    public void onApplicationEvent(CommandReceivedEvent event) {
        try {
            commandLoop:
            while (true) {
                for (AbstractCommandHandler commandHandler : commandHandlerList) {
                    if (commandHandler.isSupport(event.getClientSession(), event.getCommand())) {
                        commandHandler.handle(event.getClientSession(), event.getCommand());

                        break commandLoop;
                    }
                }

                unrecognizedCommandHandler.handle(event.getClientSession(), event.getCommand());

                break;
            }
        } catch (AbstractEmailRejectedException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}