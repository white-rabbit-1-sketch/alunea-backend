package me.silvermail.backend.config;

import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
public class ExternalEmailConfig {
    @Value("${email.external.host}")
    protected String host;

    @Value("${email.external.port}")
    protected int port;

    @Value("${email.external.properties.mail.smtp.starttls.enable}")
    protected boolean isStarttlsEnabled;

    @Bean
    public JavaMailSender externalEmailSender()  {
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
