package me.silvermail.backend.server.smtp.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class StreamWriter {
    PrintWriter writer;

    public StreamWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void write(String data) throws IOException {
        writer.println(data);
        writer.flush();
        log.debug("Send data: {}", data);
    }
}
