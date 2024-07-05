package me.silvermail.backend.server.milter.handler;

import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.nightcode.milter.*;
import org.nightcode.milter.codec.MilterPacket;
import org.springframework.context.ApplicationEventPublisher;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GeneralMilterHandler extends AbstractMilterHandler {
    protected ApplicationEventPublisher applicationEventPublisher;

    protected InternetAddress senderAddress = null;
    protected List<InternetAddress> recipientAddressList = new ArrayList<>();

    protected List<Map<String, String>> headerList = new ArrayList<>();

    public GeneralMilterHandler(
            Actions milterActions,
            ProtocolSteps milterProtocolSteps,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        super(milterActions, milterProtocolSteps);

        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void connect(
            MilterContext context,
            String hostname,
            int family,
            int port,
            @Nullable SocketAddress address
    ) throws MilterException {
        log.debug("<CONNECT> hostname: {}, family: {}, port: {}, address: {}", hostname, family, port, address);
        super.connect(context, hostname, family, port, address);
    }

    @Override
    public void helo(MilterContext context, String helohost) throws MilterException {
        log.debug("<HELO> helohost: {}", helohost);
        super.helo(context, helohost);
    }

    @Override
    public void envfrom(MilterContext context, List<String> senderList) throws MilterException {
        log.debug("<ENVFROM> from: {}", senderList);

        /*if (senderList == null || senderList.size() != 1) {
            throw new MilterException("Invalid sender list length");
        }

        try {
            senderAddress = new InternetAddress(senderList.getFirst());
        } catch (AddressException e) {
            throw new MilterException("Invalid sender list", e);
        }*/

        super.envfrom(context, senderList);
    }

    @Override
    public void envrcpt(MilterContext context, List<String> recipients) throws MilterException {
        log.debug("<ENVRCPT> recipients: {}", recipients);

       /* for (String recipient : recipients) {
            try {
                recipientAddressList.add(new InternetAddress(recipient));
            } catch (AddressException e) {
                throw new MilterException("Invalid recipients", e);
            }
        }*/

        super.envrcpt(context, recipients);
    }

    @Override
    public void header(MilterContext context, String headerName, String headerValue) throws MilterException {
        log.debug("<HEADER> {}: {}", headerName, headerValue);

        headerList.add(Map.of(headerName, headerValue));

        super.header(context, headerName, headerValue);
    }

    @Override
    public void eoh(MilterContext context) throws MilterException {
        log.debug("<EOH>");
        super.eoh(context);
    }

    @Override
    public void body(MilterContext context, byte[] bodyChunk) throws MilterException {
        log.debug("<BODY> bodyChunk: {}", new String(bodyChunk, StandardCharsets.UTF_8));
        super.body(context, bodyChunk);
    }

    @Override
    public void eom(MilterContext context, @Nullable byte[] bodyChunk) throws MilterException {
        log.debug("<EOM> final bodyChunk: {}", bodyChunk != null ? new String(bodyChunk, StandardCharsets.UTF_8) : "");
        super.eom(context, bodyChunk);
    }

    @Override
    public void abort(MilterContext context, MilterPacket packet) throws MilterException {
        log.debug("<ABORT> abort: {}", packet);
        super.abort(context, packet);
    }

    @Override
    public void quit(MilterContext context) {
        log.debug("<QUIT>");
    }

    @Override
    public void quitNc(MilterContext context) {
        log.debug("<QUIT_NC>");
    }

    @Override
    public void data(MilterContext context, byte[] payload) throws MilterException {
        log.debug("<DATA> {}", new String(payload, StandardCharsets.UTF_8));
        super.data(context, payload);
    }

    @Override
    public void optneg(
            MilterContext context,
            int mtaProtocolVersion,
            Actions mtaActions,
            ProtocolSteps mtaProtocolSteps
    ) throws MilterException {
        log.debug("<NEGOTIATE> {}, {}, {}", mtaProtocolVersion, mtaActions, mtaProtocolSteps);
        super.optneg(context, mtaProtocolVersion, mtaActions, mtaProtocolSteps);
    }

    @Override
    public void unknown(MilterContext context, byte[] payload) throws MilterException {
        log.debug("<UNKNOWN> unknown: {}", new String(payload, StandardCharsets.UTF_8));
        super.unknown(context, payload);
    }
}
