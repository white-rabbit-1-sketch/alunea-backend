package me.silvermail.backend.config;

import me.silvermail.backend.server.milter.MilterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilterServerConfig {
    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    @Value("${server.milter.host}")
    protected String milterServerHost;

    @Value("${server.milter.port}")
    protected int milterServerPort;

    @Value("${server.milter.pool-size}")
    protected int milterServerPoolSize;

    //@Bean(initMethod = "start")
    public MilterServer milterServer() {
        return new MilterServer(milterServerHost, milterServerPort, milterServerPoolSize, applicationEventPublisher);
    }
}