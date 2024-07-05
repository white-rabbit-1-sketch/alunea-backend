package me.silvermail.backend.server.milter;

import me.silvermail.backend.server.milter.handler.GeneralMilterHandler;
import org.nightcode.milter.Actions;
import org.nightcode.milter.MilterHandler;
import org.nightcode.milter.ProtocolSteps;
import org.nightcode.milter.net.MilterGatewayManager;
import org.nightcode.milter.net.ServerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MilterServer {
    protected ApplicationEventPublisher applicationEventPublisher;

    protected String host;
    protected int port;
    protected int poolSize;

    public MilterServer(
            String host,
            int port,
            int poolSize,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.host = host;
        this.port = port;
        this.poolSize = poolSize;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void start() throws IOException {
        Actions milterActions = Actions.builder()
                .addHeader()
                .replaceBody()
                .addRecipients()
                .deleteRecipients()
                .changeDeleteHeaders()
                .quarantineEnvelope()
                .changeFrom()
                .addRecipientsInclArgs()
                .setSymList()
                .build();

        ProtocolSteps milterProtocolSteps = ProtocolSteps.builder()
                .build();

        InetSocketAddress address = new InetSocketAddress(host, port);
        ServerFactory<InetSocketAddress> serverFactory = ServerFactory.tcpIpFactory(address);

        MilterHandler milterHandler = new GeneralMilterHandler(
                milterActions,
                milterProtocolSteps,
                applicationEventPublisher
        );

        MilterGatewayManager<InetSocketAddress> gatewayManager = new MilterGatewayManager<>(serverFactory, milterHandler);

        gatewayManager.bind();
    }
}
