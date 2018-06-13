package com.liu233w.encryption.encryptedChating.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class DesCipher {

    private static final ThreadLocal<KeyGenerator> keyGeneratorThreadLocal = new ThreadLocal<>();

    /**
     * get a key pair generator instance
     */
    private static KeyGenerator getKeyGenerator() {
        KeyGenerator keyGenerator = keyGeneratorThreadLocal.get();
        if (keyGeneratorThreadLocal == null) {
            try {
                keyGenerator = KeyGenerator.getInstance(CipherConsts.DesAlgorithm);
                keyGeneratorThreadLocal.set(keyGenerator);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return keyGenerator;
    }

    public static SecretKey generateKey() {

        final KeyGenerator keyGenerator = getKeyGenerator();
        return keyGenerator.generateKey();
    }
}
