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

        final KeyClient keyClient = new KeyClient(ClientGlobalConfig.keyServerAddress, ClientGlobalConfig.keyServerPort);
        keyClient.sendLocalKey(ClientGlobalConfig.localKeyPair.getPublicKey(), socket.getLocalPort());

        final RsaKey destPublicKey = keyClient.loadTargetKey(
                (host.equals("localhost") ? "127.0.0.1" : host)
                        + ":" + String.valueOf(port));

        // send des key
        final DesKey desKey = DesKey.random();
        final byte[] desKeyBytes = new BigInteger(String.valueOf(desKey.getKey())).toByteArray();
        final byte[] encryptedDesKey = RsaCipher.encrypt(desKeyBytes, destPublicKey);
        final OutputStream outputStream = socket.getOutputStream();
        sendDataPackToStream(outputStream, encryptedDesKey);

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

        keyClient.sendLocalKey(ClientGlobalConfig.localKeyPair.getPublicKey(), port);

        final Socket socket = serverSocket.accept();
        final InputStream inputStream = socket.getInputStream();

        // read des key
        final byte[] encryptedDes = readDataPackFromStream(inputStream);
        final byte[] decryptedKeyBytes = RsaCipher.encrypt(encryptedDes, ClientGlobalConfig.localKeyPair.getPrivateKey());
        final DesKey desKey = new DesKey(new BigInteger(decryptedKeyBytes).longValue());

        // get remote public key
        final String remoteAddress = socket.getInetAddress().getHostName() + ":" + socket.getPort();
        RsaKey remotePublicKey = keyClient.loadTargetKey(remoteAddress);

        return new SecurityConnection(socket, desKey, ClientGlobalConfig.localKeyPair.getPrivateKey(), remotePublicKey);
    }

    public static byte[] readDataPackFromStream(InputStream in) throws IOException {
        // the first 2 bytes are sizes. They specifies the pack size.

        final int size = in.read() << 8 & 0x0000ff00 | in.read() & 0x000000ff;
        final byte[] data = new byte[size];
        int beginIdx = 0;
        while (beginIdx < size) {
            beginIdx += in.read(data, beginIdx, size - beginIdx);
        }
        return data;
    }

    public static void sendDataPackToStream(OutputStream out, byte[] data) throws IOException {
        final int size = data.length;
        out.write((size & 0x0000ff00) >> 8);
        out.write(size & 0x000000ff);
        out.write(data);
        out.flush();
    }
}
