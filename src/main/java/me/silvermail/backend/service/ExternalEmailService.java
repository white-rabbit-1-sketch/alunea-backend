package me.silvermail.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.message.ExternalEmailMessage;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.transport.email.SmtpTransport;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalEmailService {
    @Autowired
    protected Queue externalEmailQueue;

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    public void queueEmail(ExternalEmail email) {
        rabbitTemplate.convertAndSend(externalEmailQueue.getName(), new ExternalEmailMessage(email));
    }

    public void sendEmail(ExternalEmail externalEmail) throws MessagingException {
        //externalEmail.getMimeMessage().getSession().setDebug(true);
        SmtpTransport transport = new SmtpTransport(externalEmail.getMimeMessage().getSession(), null);
        transport.connect();

        transport.sendMessage(
                externalEmail.getMimeMessage(),
                new InternetAddress[]{externalEmail.getSmtpRecipientAddress()},
                externalEmail.getSmtpSenderAddress()
        );

        transport.close();
    }
}