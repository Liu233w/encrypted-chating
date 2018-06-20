package com.liu233w.encryption.encryptedChating.cipher;

import org.junit.Test;

import java.math.BigInteger;

import static com.google.common.truth.Truth.assertThat;

public class RsaCipherTest {


    @Test
    public void testEncryptAndDecrypt() {
        final RsaKeyPair rsaKeyPair = new RsaKeyPair(BigInteger.valueOf(3 * 11), BigInteger.valueOf(3), BigInteger.valueOf(7));

        assertThat(RsaCipher.encrypt(BigInteger.valueOf(24).toByteArray(), rsaKeyPair.getPublicKey()))
                .isEqualTo(BigInteger.valueOf(30).toByteArray());

        assertThat(RsaCipher.encrypt(BigInteger.valueOf(30).toByteArray(), rsaKeyPair.getPrivateKey()))
                .isEqualTo(BigInteger.valueOf(24).toByteArray());
    }

    @Test
    public void testRsaKeyParse() {
        final RsaKey key = RsaKey.parse("[123456,78900]");
        assertThat(key.toString()).isEqualTo("[123456,78900]");
    }
}