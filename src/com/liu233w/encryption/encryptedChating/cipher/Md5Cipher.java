package com.liu233w.encryption.encryptedChating.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cipher for digital signature
 */
public class Md5Cipher {
    public byte[] encrypt(byte[] input) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(input);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
