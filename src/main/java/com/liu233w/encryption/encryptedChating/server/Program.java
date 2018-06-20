package com.liu233w.encryption.encryptedChating.server;

import com.liu233w.encryption.encryptedChating.server.core.KeyServer;
import com.liu233w.encryption.encryptedChating.server.core.ServerGlobalConfig;

import java.io.IOException;

public class Program {

    public static void main(String[] args) {
        System.out.println("Will start listening at " + String.valueOf(ServerGlobalConfig.port));

        final KeyServer keyServer = new KeyServer();
        try {
            keyServer.startListening(ServerGlobalConfig.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
