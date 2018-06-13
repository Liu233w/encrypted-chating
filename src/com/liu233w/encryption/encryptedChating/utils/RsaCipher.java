package com.liu233w.encryption.encryptedChating.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RsaCipher {

    private static final ThreadLocal<KeyPairGenerator> keyPairGeneratorThreadLocal = new ThreadLocal<>();

    /**
     * get a key pair generator instance
     */
    private static KeyPairGenerator getKeyPairGenerator() {
        KeyPairGenerator keyPairGenerator = keyPairGeneratorThreadLocal.get();
        if (keyPairGeneratorThreadLocal == null) {
            try {
                keyPairGenerator = KeyPairGenerator.getInstance(CipherConsts.RsaAlgorithm);
                keyPairGenerator.initialize(CipherConsts.RsaKeySize);
                keyPairGeneratorThreadLocal.set(keyPairGenerator);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return keyPairGenerator;
    }

    public static KeyPair generateKeyPair() {
        final KeyPairGenerator keyPairGenerator = getKeyPairGenerator();
        return keyPairGenerator.generateKeyPair();
    }
}
