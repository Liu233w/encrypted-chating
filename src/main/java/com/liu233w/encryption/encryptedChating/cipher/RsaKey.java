package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;

public class RsaKey {

    private BigInteger n;

    private BigInteger e;

    public RsaKey(BigInteger n, BigInteger e) {
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

    /**
     * Parse the input as RsaKey, don't have any error checking
     *
     * @param input what toString() outputs
     * @return
     */
    public static RsaKey parse(String input) {
        final String[] split = input.split("[\\[\\],]");
        return new RsaKey(new BigInteger(split[1]), new BigInteger(split[2]));
    }
}
