package me.silvermail.backend.server.smtp;

import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.exception.email.external.AbstractEmailRejectedException;
import me.silvermail.backend.exception.email.external.InternalException;
import me.silvermail.backend.server.smtp.event.ClientConnectedEvent;
import me.silvermail.backend.server.smtp.event.CommandReceivedEvent;
import me.silvermail.backend.server.smtp.io.StreamReader;
import me.silvermail.backend.server.smtp.io.StreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SmtpServer {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    protected final int port;
    protected ServerSocket serverSocket;
    protected final ExecutorService threadPool;

    public SmtpServer(int port, int poolSize) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        log.info("Server started on port {}", port);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    log.info("Client connected [socket={}]", socket.toString());

                    threadPool.submit(() -> {
                        try {
                            handleConnection(socket);
                        } catch (Throwable e) {
                            log.error("Error occurred while handling connection", e);
                        }

                        try {
                            socket.close();
                        } catch (Throwable e) {
                            log.error("Error occurred while closing client socket", e);
                        }
                        log.info("Client was disconnected [socket={}]", socket);
                    });
                } catch (Throwable e) {
                    log.error("Error occurred while accepting connection from socket", e);

                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (Throwable e) {
                log.error("Error occurred while closing server socket", e);
            }
        }
        threadPool.shutdown();
        log.info("Server stopped");
    }

    protected void handleConnection(Socket socket) throws Exception {
        StreamReader reader = new StreamReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        StreamWriter writer = new StreamWriter(new PrintWriter(socket.getOutputStream(), true));

        ClientSession clientSession = new ClientSession(socket, reader, writer);
        log.debug("Client session was established: {}", clientSession);

        try {
            ClientConnectedEvent clientConnectedEvent = new ClientConnectedEvent(clientSession);
            applicationEventPublisher.publishEvent(clientConnectedEvent);

            String command;
            while ((command = reader.read()) != null) {
                log.debug("Command was read {}", command);
                CommandReceivedEvent commandReceivedEvent = new CommandReceivedEvent(clientSession, command);
                applicationEventPublisher.publishEvent(commandReceivedEvent);

                if (clientSession.isDisconnectRequired()) {
                    log.debug("Client disconnect requested {}", clientSession);
                    break;
                }
            }
        } catch (AbstractEmailRejectedException e) {
            log.warn("Email was rejected: {}", e.getCode() + " " + e.getMessage());
            // we don't want duplicates
            clientSession.getWriter().write( "250 " + e.getMessage());
        } catch (Throwable e) {
            InternalException internalException = new InternalException(e);
            // we don't want duplicates
            clientSession.getWriter().write( "250 " + internalException.getMessage());

            throw e;
        }
    }
}