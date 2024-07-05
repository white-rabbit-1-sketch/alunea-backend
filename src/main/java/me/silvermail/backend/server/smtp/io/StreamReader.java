package me.silvermail.backend.server.smtp.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class StreamReader {
    BufferedReader reader;

    public StreamReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public String read() throws IOException {
        String line = reader.readLine();
        log.debug("Received data: {}", line);

        return line;
    }
}
