package com.liu233w.encryption.encryptedChating.utils;

import com.liu233w.encryption.encryptedChating.cipher.Md5Cipher;
import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.cipher.RsaKey;

import java.util.Arrays;

public class DigitalSignatureProcessor {

    /**
     * generate digital signature
     *
     * @param input plain input
     * @param key   private key
     * @return digital signature
     */
    public static byte[] generate(byte[] input, RsaKey key) {
        final byte[] md5 = Md5Cipher.encrypt(input);
        return RsaCipher.encrypt(md5, key);
    }

    /**
     * verify the plain text with digital signature
     *
     * @param plain     plain text to verify
     * @param signature digital signature
     * @param key       public key
     * @return passed
     */
    public static boolean verify(byte[] plain, byte[] signature, RsaKey key) {
        final byte[] md5 = Md5Cipher.encrypt(plain);
        final byte[] decryptedMd5 = RsaCipher.encrypt(signature, key);

        return Arrays.equals(md5, decryptedMd5);
    }
}
