package com.liu233w.encryption.encryptedChating.server.core;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Single thread server
 */
public class KeyServer {
    private HashMap<String, String> keys;

    public KeyServer() {
        keys = new HashMap<>();
    }

    private BufferedReader reader;
    private OutputStreamWriter writer;
    private String requestAddr;

    /**
     * start server at localhost:port, will block the thread
     *
     * @param port
     * @throws IOException
     */
    public void startListening(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            final Socket socket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream());
            requestAddr = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

            final String s = reader.readLine();
            if (s.charAt(0) == 's') {
                handleSave(s.substring(1));
            } else if (s.charAt(0) == 'l') {
                handleLoad(s.substring(1));
            } else {
                // No error handling
            }

            writer.flush();

            reader.close();
            writer.close();
            socket.close();
        }
    }

    private void handleLoad(String address) throws IOException {
        String res = keys.get(address);
        if (res == null) {
            res = "None";
        }

        System.out.printf("%s - Send key %s value %s\n", requestAddr, address, res);

        writer.write(res);
        writer.write("\n");
    }

    private void handleSave(String key) throws IOException {
        keys.put(requestAddr, key);

        System.out.printf("%s - Save %s", requestAddr, key);

        writer.write("ok\n");
    }
}
