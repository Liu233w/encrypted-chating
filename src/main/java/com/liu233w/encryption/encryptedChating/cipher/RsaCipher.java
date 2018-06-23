package com.liu233w.encryption.encryptedChating.cipher;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class RsaCipher {

    /**
     * generate public key and private key
     *
     * @return [public_key, private_key]
     */
    public static RsaKeyPair generateKey() {
        final Random seed = new Random();
        final BigInteger p = BigInteger.probablePrime(256, seed);
        final BigInteger q = BigInteger.probablePrime(256, seed);
        final BigInteger n = p.multiply(q);
        final BigInteger z = p.add(BigInteger.valueOf(-1)).multiply(q.add(BigInteger.valueOf(-1)));

        BigInteger e;
        do {
            // 直到 1<e<z
            e = BigInteger.probablePrime(z.bitLength(), seed);
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(z) >= 0);

        final BigInteger[] xyq = extEuclid(e, z);
        final BigInteger d = xyq[0].divide(xyq[2]);

        return new RsaKeyPair(n, e, d);
    }

    /**
     * RSA encrypt
     *
     * @param plain
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] plain, RsaKey key) {
        // rsa 的输入不能是负数，因此 plain 的第一个byte不能是负数,需要额外的处理
        final boolean negative = plain[0] < 0;
        if (negative) {
            // 防止改变原来的数据
            plain = Arrays.copyOf(plain, plain.length);
            plain[0] *= -1;
        }

        final byte[] encrypted = doRsa(new BigInteger(plain), key.getN(), key.getE()).toByteArray();

        // 加密后第一个byte有可能是0，必须额外添加一位做判断
        final byte[] res = Arrays.copyOf(encrypted, encrypted.length + 1);
        res[res.length - 1] = (byte) (negative ? 1 : 0);

        return res;
    }

    /**
     * decrypt
     *
     * @param encrypted must be encrypt outputs
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] encrypted, RsaKey key) {
        final byte[] input = Arrays.copyOf(encrypted, encrypted.length - 1);

        final byte[] plain = doRsa(new BigInteger(input), key.getN(), key.getE()).toByteArray();

        if (encrypted[encrypted.length - 1] == 1) {
            plain[0] *= -1;
        } else if (encrypted[encrypted.length - 1] != 0) {
            throw new RuntimeException("Not a valid encrypted format");
        }

        return plain;
    }

    /**
     * do the calculation (encrypt or decrypt)
     *
     * @param text plain or cipher
     * @param n    base
     * @param key  e or d
     * @return
     */
    private static BigInteger doRsa(BigInteger text, BigInteger n, BigInteger key) {
        if (text.compareTo(n) >= 0) {
            throw new IllegalArgumentException("text must be smaller than n");
        }

        return text.modPow(key, n);
    }

    /**
     * Extended Euclidean algorithm in BigInteger.
     * <p>
     * for ax + by = gcd(a,b)
     *
     * @param a a
     * @param b b
     * @return [x, y, gcd(a,b)]
     */
    private static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{BigInteger.ONE, BigInteger.ZERO, a};
        } else {

            final BigInteger[] divideAndRemainder = a.divideAndRemainder(b);
            final BigInteger[] res = extEuclid(b, divideAndRemainder[1]);

            final BigInteger x = res[0];
            final BigInteger y = res[1];
            final BigInteger q = res[2];

            return new BigInteger[]{y, x.subtract(divideAndRemainder[0].multiply(y)), q};
        }
    }
}
