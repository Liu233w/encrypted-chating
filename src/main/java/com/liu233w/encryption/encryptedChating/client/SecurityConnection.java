package com.liu233w.encryption.encryptedChating.client;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.Key;

/**
 * A Connection with encryption and digital signature
 */
public class SecurityConnection {

    private Socket socket;

    /**
     * To validate the digital signature.
     */
    private Key destPublicKey;

    /**
     * To add digital signature.
     */
    private Key myPrivateKey;

    /**
     * To encrypt and decrypt data;
     */
    private SecretKey key;

    public SecurityConnection(Socket socket, Key destPublicKey, Key myPrivateKey, SecretKey key) {
        this.socket = socket;
        this.destPublicKey = destPublicKey;
        this.myPrivateKey = myPrivateKey;
        this.key = key;
    }

//    public void sendData(byte[] data) {
//        socket.getOutputStream();
//    }
}
