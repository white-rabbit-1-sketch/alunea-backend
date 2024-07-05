package me.silvermail.backend.server.smtp.command.handler;

import me.silvermail.backend.server.smtp.ClientSession;

abstract public class AbstractCommandHandler {
    abstract public void handle(
            ClientSession clientSession,
            String command
    ) throws Exception;

    abstract public boolean isSupport(
            ClientSession clientSession,
            String command
    ) throws Exception;
}
