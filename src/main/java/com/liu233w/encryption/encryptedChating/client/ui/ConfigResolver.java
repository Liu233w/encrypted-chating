package com.liu233w.encryption.encryptedChating.client.ui;

import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.client.core.ClientGlobalConfig;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class ConfigResolver {

    private ConnectionKind connectionKind;

    private String connectAddress;

    private int connectPort;

    public ConnectionKind getConnectionKind() {
        return connectionKind;
    }

    public String getConnectAddress() {
        return connectAddress;
    }

    public int getConnectPort() {
        return connectPort;
    }

    public void resolve() {
        final TextIO textIO = TextIoFactory.getTextIO();

        textIO.getTextTerminal().println("Generating RSA keys, please stand by...");
        ClientGlobalConfig.localKeyPair = RsaCipher.generateKey();
        textIO.getTextTerminal().printf("Successful: %s\n", ClientGlobalConfig.localKeyPair.toString());

        ClientGlobalConfig.keyServerAddress = textIO.newStringInputReader()
                .withDefaultValue("localhost")
                .read("Key Server Address");
        ClientGlobalConfig.keyServerPort = textIO.newIntInputReader()
                .withDefaultValue(8000)
                .read("Key Server Port");

        while (true) {
            connectionKind = textIO.newEnumInputReader(ConnectionKind.class)
                    .read("Select action");

            if (connectionKind == ConnectionKind.ConnectTo) {
                connectAddress = textIO.newStringInputReader()
                        .read("The address you will connect to");
                connectPort = textIO.newIntInputReader()
                        .read("The port you will connect to");

                textIO.getTextTerminal().println("You will connect to " + connectAddress + ":" + connectPort);

            } else if (connectionKind == ConnectionKind.WaitForConnection) {
                connectPort = textIO.newIntInputReader()
                        .read("The port you will listen");
                textIO.getTextTerminal().println("You will waiting for others to connect to your client at port " + connectPort);
            }

            boolean ok = textIO.newBooleanInputReader()
                    .withTrueInput("y")
                    .withFalseInput("n")
                    .withDefaultValue(true)
                    .read("Is that ok?");
            if (ok) break;
        }

        textIO.dispose();
    }

    public enum ConnectionKind {
        WaitForConnection,
        ConnectTo,
    }
}
