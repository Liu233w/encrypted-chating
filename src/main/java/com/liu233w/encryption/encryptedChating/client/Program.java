package com.liu233w.encryption.encryptedChating.client;

import com.liu233w.encryption.encryptedChating.client.core.ConnectionFactory;
import com.liu233w.encryption.encryptedChating.client.ui.ChatWindow;
import com.liu233w.encryption.encryptedChating.client.ui.ConfigResolver;
import com.liu233w.encryption.encryptedChating.securityConnection.SecurityConnection;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        final ConfigResolver configResolver = new ConfigResolver();
        configResolver.resolve();

        SecurityConnection securityConnection;
        if (configResolver.getConnectionKind() == ConfigResolver.ConnectionKind.WaitForConnection) {
            System.out.println("Waiting for connection...");
            securityConnection = ConnectionFactory.waitForConnection(configResolver.getConnectPort());
        } else {
            System.out.println("Connecting...");
            securityConnection = ConnectionFactory.connectTo(configResolver.getConnectAddress(), configResolver.getConnectPort());
        }

        final ChatWindow chatWindow = new ChatWindow(securityConnection);
        chatWindow.start();
    }
}
