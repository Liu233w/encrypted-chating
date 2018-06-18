package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;

public class RsaPrivateKey {

    private BigInteger n;

    private BigInteger d;

    public RsaPrivateKey(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getD() {
        return d;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", n.toString(), d.toString());
    }
}
