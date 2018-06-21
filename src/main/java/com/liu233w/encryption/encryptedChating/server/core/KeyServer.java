package com.liu233w.encryption.encryptedChating.server.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Socket socket;

    /**
     * start server at localhost:port, will block the thread
     *
     * @param port
     * @throws IOException
     */
    public void startListening(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            socket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream());

            final String s = reader.readLine();
            switch (s) {
                case "save":
                    handleSave();
                    break;
                case "load":
                    handleLoad();
                    break;
                default:
                    // No error handling
                    break;
            }

            writer.flush();

            reader.close();
            writer.close();
            socket.close();
        }
    }

    private void handleLoad() throws IOException {
        final String address = reader.readLine();
        String res = keys.get(address);
        if (res == null) {
            res = "None";
        }

        System.out.printf("Load key %s value %s\n", address, res);

        writer.write(res);
        writer.write("\n");
    }

    private void handleSave() throws IOException {
        final String port = reader.readLine();
        final String key = reader.readLine();
        // read ip from socket
        final String address = socket.getInetAddress().getHostAddress() + ":" + port;
        keys.put(address, key);

        System.out.printf("Save key %s value %s\n", address, key);

        writer.write("ok\n");
    }
}
