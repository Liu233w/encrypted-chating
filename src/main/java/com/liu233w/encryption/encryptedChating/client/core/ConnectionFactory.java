package com.liu233w.encryption.encryptedChating.client.core;

import com.liu233w.encryption.encryptedChating.cipher.DesKey;
import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.cipher.RsaKey;
import com.liu233w.encryption.encryptedChating.securityConnection.SecurityConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * A factory to build security connection
 */
public class ConnectionFactory {

    /**
     * connect to the host, will send local public key to Key Server
     *
     * @param host ip
     * @param port port
     * @return the security connection
     * @throws IOException
     */
    public static SecurityConnection connectTo(String host, int port) throws IOException {

        final Socket socket = new Socket(host, port);
        // schema: computer-name:port
        final String localAddr = socket.getLocalAddress().getHostName() + String.valueOf(socket.getLocalPort());

        final KeyClient keyClient = new KeyClient(ClientGlobalConfig.keyServerAddress, ClientGlobalConfig.keyServerPort);
        keyClient.sendLocalKey(ClientGlobalConfig.localKeyPair.getPublicKey(), localAddr);
        final RsaKey destPublicKey = keyClient.loadTargetKey(host + ":" + String.valueOf(port));

        // send des key
        final DesKey desKey = DesKey.random();
        final byte[] desKeyBytes = new BigInteger(String.valueOf(desKey.getKey())).toByteArray();
        final byte[] encryptedDesKey = RsaCipher.encrypt(desKeyBytes, destPublicKey);
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write(encryptedDesKey);
        outputStream.write(0);
        outputStream.flush();

        return new SecurityConnection(socket, desKey, ClientGlobalConfig.localKeyPair.getPrivateKey(), destPublicKey);
    }

    /**
     * Block the thread until receive a socket connection. Will send local public key to Key Server
     *
     * @param port the port socket will listen to
     * @return the security connection
     * @throws IOException
     */
    public static SecurityConnection waitForConnection(int port) throws IOException {

        final ServerSocket serverSocket = new ServerSocket(port);
        final KeyClient keyClient = new KeyClient(ClientGlobalConfig.keyServerAddress, ClientGlobalConfig.keyServerPort);

        // schema: computer-name:port
        final String address = serverSocket.getInetAddress().getHostName() + ":" + String.valueOf(port);
        keyClient.sendLocalKey(ClientGlobalConfig.localKeyPair.getPublicKey(), address);

        final Socket socket = serverSocket.accept();

        // get remote public key
        final String remoteAddress = socket.getInetAddress().getHostName() + ":" + String.valueOf(socket.getPort());
        final RsaKey remotePublicKey = keyClient.loadTargetKey(remoteAddress);

        // read des key
        byte[] inputs = new byte[1024];
        int beginIdx = 0;
        int read;
        final InputStream inputStream = socket.getInputStream();
        while ((read = inputStream.read()) != 0) {
            inputs[beginIdx++] = (byte) read;
        }
        inputs = Arrays.copyOf(inputs, beginIdx);
        final byte[] decryptedKeyBytes = RsaCipher.encrypt(inputs, ClientGlobalConfig.localKeyPair.getPrivateKey());
        final DesKey desKey = new DesKey(new BigInteger(decryptedKeyBytes).longValue());

        return new SecurityConnection(socket, desKey, ClientGlobalConfig.localKeyPair.getPrivateKey(), remotePublicKey);
    }
}
