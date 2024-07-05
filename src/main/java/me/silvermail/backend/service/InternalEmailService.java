package me.silvermail.backend.service;

import jakarta.mail.internet.MimeMessage;
import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import me.silvermail.backend.message.InternalEmailMessage;
import me.silvermail.backend.service.email.internal.builder.AbstractEmailBuilderService;
import org.simplejavamail.utils.mail.dkim.DkimMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class InternalEmailService {
    @Autowired
    protected JavaMailSender internalEmailSender;
    @Autowired
    protected BeanFactory beanFactory;
    @Autowired
    protected RabbitTemplate rabbitTemplate;
    @Autowired
    protected Queue internalEmailQueue;

    @Value("${email.internal.sender.address}")
    protected String senderAddress;

    public void queueEmail(AbstractInternalEmail email) {
        rabbitTemplate.convertAndSend(internalEmailQueue.getName(), new InternalEmailMessage(email));
    }

    public void sendEmail(AbstractInternalEmail email) throws Exception {
        AbstractEmailBuilderService emailBuilderService = (AbstractEmailBuilderService) beanFactory.getBean(
                Character.toLowerCase(email.getClass().getSimpleName().charAt(0)) + email.getClass().getSimpleName().substring(1) + "BuilderService"
        );

        emailBuilderService.build(email);

        MimeMessage mimeMessage = internalEmailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(this.senderAddress);
        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setText(email.getBody(), true);

        internalEmailSender.send(mimeMessage);
    }
}
