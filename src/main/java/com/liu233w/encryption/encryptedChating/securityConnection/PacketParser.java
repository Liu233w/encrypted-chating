package com.liu233w.encryption.encryptedChating.securityConnection;

import java.io.*;
import java.util.HashMap;

/**
 * Read security packet from stream and parse it
 */
public class PacketParser {

    private byte[] data;

    private HashMap<String, String> headers;

    /**
     * read all headers to fill the field. Until meet the empty line.
     *
     * @param in
     */
    private void readHeaders(PushbackInputStream in) throws IOException {

        int b;

        while ((b = in.read()) != '\n') {

            final StringBuilder name = new StringBuilder();
            final StringBuilder value = new StringBuilder();
            boolean atName = true;

            in.unread(b);
            while ((b = in.read()) != '\n') {
                if (atName) {
                    if (b == ':') {
                        atName = false;
                    } else {
                        name.append((char) b);
                    }
                } else {
                    value.append((char) b);
                }
            }
            headers.put(name.toString(), value.toString());
        }

        final int length = Integer.parseInt(headers.get("Length"));
        data = new byte[length];
        int begin = 0;
        while (begin < length) {
            final int read = in.read(data, begin, length - begin);
            if (read == -1) {
                throw new IOException("Can't read entire data, the data read before stream close doesn't meet packet length");
            }
            begin += read;
        }
    }

    public PacketParser(PushbackInputStream in) throws IOException {
        headers = new HashMap<>();
        readHeaders(in);
    }

    public byte[] getData() {
        return data;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}
