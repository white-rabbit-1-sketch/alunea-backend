package me.silvermail.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class InternalEmailConfig {
    @Value("${email.internal.host}")
    protected String host;

    @Value("${email.internal.port}")
    protected int port;

    @Value("${email.internal.properties.mail.smtp.starttls.enable}")
    protected boolean isStarttlsEnabled;

    @Bean
    public JavaMailSender internalEmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", isStarttlsEnabled);

        return mailSender;
    }
}
