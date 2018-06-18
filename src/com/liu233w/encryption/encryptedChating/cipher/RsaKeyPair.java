package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;

public class RsaKeyPair {

    private RsaPublicKey rsaPublicKey;

    private RsaPrivateKey rsaPrivateKey;

    public RsaKeyPair(BigInteger n, BigInteger e, BigInteger d) {
        this.rsaPublicKey = new RsaPublicKey(n, e);
        this.rsaPrivateKey = new RsaPrivateKey(n, d);
    }

    public RsaPublicKey getPublicKey() {
        return rsaPublicKey;
    }

    public RsaPrivateKey getPrivateKey() {
        return rsaPrivateKey;
    }
}
