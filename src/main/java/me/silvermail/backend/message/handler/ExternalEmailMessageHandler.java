package me.silvermail.backend.message.handler;

import me.silvermail.backend.message.ExternalEmailMessage;
import me.silvermail.backend.service.email.external.handler.ExternalEmailHandlerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalEmailMessageHandler {

    @Autowired
    protected ExternalEmailHandlerService externalEmailHandlerService;

    @RabbitListener(queues = "#{externalEmailQueue.getName()}")
    public void handleMessage(ExternalEmailMessage emailMessage) throws Exception {
        externalEmailHandlerService.handleEmail(emailMessage.getExternalEmail());
    }
}
