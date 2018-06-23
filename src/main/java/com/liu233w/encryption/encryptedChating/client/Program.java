package com.liu233w.encryption.encryptedChating.client;

import com.liu233w.encryption.encryptedChating.client.ui.ChatWindow;
import com.liu233w.encryption.encryptedChating.client.ui.ConfigResolver;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        final ConfigResolver configResolver = new ConfigResolver();
        configResolver.resolve();

        final ChatWindow chatWindow = new ChatWindow(configResolver.getConnection());
        chatWindow.start();
    }
}
