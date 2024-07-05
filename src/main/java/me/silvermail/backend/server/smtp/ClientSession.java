package me.silvermail.backend.server.smtp;

import jakarta.mail.internet.InternetAddress;
import me.silvermail.backend.server.smtp.io.StreamReader;
import me.silvermail.backend.server.smtp.io.StreamWriter;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSession {
    protected Socket socket;
    protected StreamReader reader;
    protected StreamWriter writer;
    protected boolean isDisconnectRequired = false;

    protected List<InternetAddress> recipientAddressList = new ArrayList<>();
    protected InternetAddress senderAddress;

    public ClientSession(Socket socket, StreamReader reader, StreamWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public StreamReader getReader() {
        return reader;
    }

    public StreamWriter getWriter() {
        return writer;
    }

    public List<InternetAddress> getRecipientAddressList() {
        return recipientAddressList;
    }

    public void setRecipientAddressList(List<InternetAddress> recipientAddressList) {
        this.recipientAddressList = recipientAddressList;
    }

    public InternetAddress getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(InternetAddress senderAddress) {
        this.senderAddress = senderAddress;
    }

    public boolean isDisconnectRequired() {
        return isDisconnectRequired;
    }

    public void setDisconnectRequired(boolean disconnectRequired) {
        isDisconnectRequired = disconnectRequired;
    }
}
