package com.liu233w.encryption.encryptedChating.securityConnection;

import java.security.SignatureException;

public class WrongSignatureException extends SignatureException {

    private byte[] data;

    public WrongSignatureException(String msg, byte[] data) {
        super(msg);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
