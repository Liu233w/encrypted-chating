package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;

public class RsaPublicKey {

    private BigInteger n;

    private BigInteger e;

    public RsaPublicKey(BigInteger n, BigInteger e) {
        this.n = n;
        this.e = e;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", n.toString(), e.toString());
    }
}
