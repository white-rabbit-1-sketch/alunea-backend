package me.silvermail.backend.config;

import me.silvermail.backend.server.smtp.SmtpServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmtpServerConfig {

    @Value("${server.smtp.port}")
    protected int smtpServerPort;

    @Value("${server.smtp.pool-size}")
    protected int smtpServerPoolSize;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnProperty(name = "server.smtp.enabled", havingValue = "true", matchIfMissing = true)
    public SmtpServer smtpServer() {
        return new SmtpServer(smtpServerPort, smtpServerPoolSize);
    }
}