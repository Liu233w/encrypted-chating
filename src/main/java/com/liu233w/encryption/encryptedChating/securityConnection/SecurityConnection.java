package com.liu233w.encryption.encryptedChating.securityConnection;

import com.liu233w.encryption.encryptedChating.cipher.DesCipher;
import com.liu233w.encryption.encryptedChating.cipher.DesKey;
import com.liu233w.encryption.encryptedChating.cipher.RsaKey;
import com.liu233w.encryption.encryptedChating.utils.DigitalSignatureProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.security.SignatureException;
import java.util.Base64;

/**
 * A Connection with encryption
 */
public class SecurityConnection {

    private Socket socket;

    /**
     * To encrypt and decrypt data;
     */
    private DesCipher cipher;

    private OutputStream out;

    private PushbackInputStream in;

    private RsaKey myPrivateKey;

    private RsaKey destPublicKey;

    /**
     * A security connection which hold the socket
     *
     * @param socket        connection. After construction, can't be used at anywhere else
     * @param key           DesKey to encrypt data
     * @param myPrivateKey  RsaKey to generate digital signature
     * @param destPublicKey RsaKey to verify digital signature
     * @throws IOException
     */
    public SecurityConnection(Socket socket, DesKey key, RsaKey myPrivateKey, RsaKey destPublicKey) throws IOException {
        this.socket = socket;
        this.cipher = new DesCipher(key);
        out = socket.getOutputStream();
        in = new PushbackInputStream(socket.getInputStream());
        this.myPrivateKey = myPrivateKey;
        this.destPublicKey = destPublicKey;
    }

    public void close() throws IOException {
        socket.close();
    }

    /**
     * send packet
     *
     * @param data
     * @throws IOException
     */
    public void send(byte[] data) throws IOException {
        final byte[] encrypted = cipher.encrypt(data);
        final byte[] signature = DigitalSignatureProcessor.generate(data, destPublicKey);
        final String signBase64 = Base64.getEncoder().encodeToString(signature);

        new PacketBuilder()
                .withHeader("Signature", signBase64)
                .withData(encrypted)
                .sendToStream(out);
    }

    /**
     * receive a packet, will block the thread until reading the entire packet
     *
     * @return
     * @throws IOException
     * @throws SignatureException
     */
    public byte[] recv() throws IOException, SignatureException {
        final PacketParser packetParser = new PacketParser(in);

        final String signBase64 = packetParser.getHeader("Signature");
        final byte[] signature = Base64.getDecoder().decode(signBase64);
        final byte[] data = cipher.decrypt(packetParser.getData());
        if (DigitalSignatureProcessor.verify(data, signature, destPublicKey)) {
            throw new SignatureException("Wrong digital signature");
        }
        return data;
    }
}
