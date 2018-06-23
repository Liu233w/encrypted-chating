package com.liu233w.encryption.encryptedChating.client.ui;

import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.client.core.ClientGlobalConfig;
import com.liu233w.encryption.encryptedChating.client.core.ConnectionFactory;
import com.liu233w.encryption.encryptedChating.securityConnection.SecurityConnection;
import javafx.scene.paint.Color;
import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.io.IOException;

public class ConfigResolver {

    private String connectAddress;

    private int connectPort;

    private SecurityConnection connection;

    public String getConnectAddress() {
        return connectAddress;
    }

    public int getConnectPort() {
        return connectPort;
    }

    public SecurityConnection getConnection() {
        return connection;
    }

    public void resolve() throws IOException {
        final TextIO textIO = TextIoFactory.getTextIO();
        final TextTerminal<?> terminal = textIO.getTextTerminal();
        final TerminalProperties<?> properties = terminal.getProperties();

        properties.setPromptColor("white");

        terminal.println("Generating RSA keys, please stand by...");
        ClientGlobalConfig.localKeyPair = RsaCipher.generateKey();
        terminal.println("Successful.");
        terminal.printf("Private Key: %s\n", ClientGlobalConfig.localKeyPair.getPrivateKey().toString());
        terminal.printf("Public Key: %s\n", ClientGlobalConfig.localKeyPair.getPublicKey().toString());
        terminal.println();

        properties.setPromptColor(Color.GREENYELLOW);

        ClientGlobalConfig.keyServerAddress = textIO.newStringInputReader()
                .withDefaultValue("localhost")
                .read("Key Server Address");
        ClientGlobalConfig.keyServerPort = textIO.newIntInputReader()
                .withDefaultValue(8000)
                .read("Key Server Port");
        terminal.println();

        ConnectionKind connectionKind;
        while (true) {
            connectionKind = textIO.newEnumInputReader(ConnectionKind.class)
                    .read("Select action");
            terminal.println();

            if (connectionKind == ConnectionKind.ConnectTo) {
                connectAddress = textIO.newStringInputReader()
                        .read("The address you will connect to");
                connectPort = textIO.newIntInputReader()
                        .read("The port you will connect to");

                terminal.println();
                terminal.println("You will connect to " + connectAddress + ":" + connectPort);

            } else if (connectionKind == ConnectionKind.WaitForConnection) {
                connectPort = textIO.newIntInputReader()
                        .read("The port you will listen");
                terminal.println();
                terminal.println("You will waiting for others to connect to your client at port " + connectPort);
            }

            boolean ok = textIO.newBooleanInputReader()
                    .withTrueInput("y")
                    .withFalseInput("n")
                    .withDefaultValue(true)
                    .read("Is that ok?");
            if (ok) break;
        }

        properties.setPromptColor("white");
        terminal.println();

        if (connectionKind == ConnectionKind.WaitForConnection) {
            terminal.println("Waiting for connection...");
            connection = ConnectionFactory.waitForConnection(connectPort);
        } else {
            terminal.println("Connecting...");
            connection = ConnectionFactory.connectTo(connectAddress, connectPort);
        }

        textIO.dispose();
    }

    public enum ConnectionKind {
        WaitForConnection,
        ConnectTo,
    }
}
