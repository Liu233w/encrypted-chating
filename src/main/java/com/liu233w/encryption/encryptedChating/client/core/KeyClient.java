package com.liu233w.encryption.encryptedChating.client.core;

import com.liu233w.encryption.encryptedChating.cipher.RsaKey;

import java.io.*;
import java.net.Socket;

public class KeyClient {
    private String serverAddress;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public KeyClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    private void startConnection() throws IOException {
        socket = new Socket(serverAddress, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());
    }

    private void closeConnection() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

    /**
     * send local key to key server
     *
     * @param key
     */
    public void sendLocalKey(RsaKey key, String address) throws IOException {
        startConnection();
        writer.write("save\n");
        writer.write(address);
        writer.write("\n");
        writer.write(key.toString());
        writer.write("\n");
        writer.flush();
        final String line = reader.readLine();
        assert line.equals("ok") : "Send successfully";
        closeConnection();
    }

    public RsaKey loadTargetKey(String address) throws IOException {
        startConnection();
        writer.write("load\n");
        writer.write(address);
        writer.write("\n");
        writer.flush();

        final String line = reader.readLine();
        assert !line.equals("None") : "Should have keys";

        return RsaKey.parse(line);
    }
}
