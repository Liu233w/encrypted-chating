package com.liu233w.encryption.encryptedChating.cipher;

import org.junit.Test;

import java.math.BigInteger;

import static com.google.common.truth.Truth.assertThat;

public class DesCipherTest {


    @Test
    public void testEncryptAndDecrypt() {
        final com.liu233w.encryption.encryptedChating.cipher.RsaKeyPair rsaKeyPair = new com.liu233w.encryption.encryptedChating.cipher.RsaKeyPair(BigInteger.valueOf(3 * 11), BigInteger.valueOf(3), BigInteger.valueOf(7));

        assertThat(RsaCipher.encrypt(BigInteger.valueOf(24).toByteArray(), rsaKeyPair.getPublicKey()))
                .isEqualTo(BigInteger.valueOf(30).toByteArray());

        assertThat(RsaCipher.encrypt(BigInteger.valueOf(30).toByteArray(), rsaKeyPair.getPrivateKey()))
                .isEqualTo(BigInteger.valueOf(24).toByteArray());
    }
}