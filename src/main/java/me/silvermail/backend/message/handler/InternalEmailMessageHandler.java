package me.silvermail.backend.message.handler;

import me.silvermail.backend.message.InternalEmailMessage;
import me.silvermail.backend.service.InternalEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternalEmailMessageHandler {

    @Autowired
    protected InternalEmailService emailService;

    @RabbitListener(queues = "#{internalEmailQueue.getName()}")
    public void handleMessage(InternalEmailMessage emailMessage) throws Exception {
        this.emailService.sendEmail(emailMessage.getEmail());
    }
}
