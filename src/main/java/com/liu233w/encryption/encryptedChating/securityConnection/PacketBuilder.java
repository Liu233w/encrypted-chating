package com.liu233w.encryption.encryptedChating.securityConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Build security packet and send it by stream
 */
public class PacketBuilder {
    private HashMap<String, String> headers;
    private byte[] data;

    public PacketBuilder() {
        this.headers = new HashMap<String, String>();
    }

    /**
     * add header to the request. Neither name nor value should contain '\n'.
     *
     * @param name  name of a header. Should not contain ':'
     * @param value
     * @return
     */
    public PacketBuilder withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public PacketBuilder withData(byte[] data) {
        this.data = data;
        this.headers.put("Length", String.valueOf(data.length));
        return this;
    }

    public void sendToStream(OutputStream out) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry :
                headers.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(':');
            stringBuilder.append(entry.getValue());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");

        out.write(stringBuilder.toString().getBytes(Charset.forName("utf-8")));
        out.write(data);
        out.flush();
    }
}
