package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;

public class RsaKeyPair {

    private RsaKey rsaPublicKey;

    private RsaKey rsaPrivateKey;

    public RsaKeyPair(BigInteger n, BigInteger e, BigInteger d) {
        this.rsaPublicKey = new RsaKey(n, e);
        this.rsaPrivateKey = new RsaKey(n, d);
    }

    public RsaKey getPublicKey() {
        return rsaPublicKey;
    }

    public RsaKey getPrivateKey() {
        return rsaPrivateKey;
    }
}
