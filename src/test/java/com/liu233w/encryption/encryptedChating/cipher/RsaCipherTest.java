package com.liu233w.encryption.encryptedChating.cipher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.math.BigInteger;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class RsaCipherTest {

    private RsaKeyPair randomKey;

    public RsaCipherTest() {
        randomKey = RsaCipher.generateKey();
    }

    @Test
    public void testEncryptAndDecrypt() {
        final RsaKeyPair rsaKeyPair = new RsaKeyPair(BigInteger.valueOf(3 * 11), BigInteger.valueOf(3), BigInteger.valueOf(7));

        assertThat(RsaCipher.encrypt(BigInteger.valueOf(24).toByteArray(), rsaKeyPair.getPublicKey()))
                .isEqualTo(new byte[]{30, 0});

        assertThat(RsaCipher.decrypt(new byte[]{30, 0}, rsaKeyPair.getPrivateKey()))
                .isEqualTo(BigInteger.valueOf(24).toByteArray());
    }

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void testRsaKeyParse() {
        final RsaKey key = RsaKey.parse("[123456,78900]");
        assertThat(key.toString()).isEqualTo("[123456,78900]");
    }

    @Test
    public void testEncryptByPrivateKey() {

        doTestEncryptByPrivateKey(new byte[]{36, -12, 28, 76});

        // bytes begin at negative number should work
        doTestEncryptByPrivateKey(new byte[]{-36, -12, 28, 76});
        doTestEncryptByPrivateKey(new byte[]{-1, -12, 28, 76});
        doTestEncryptByPrivateKey(new byte[]{0, -12, 28, 76});
    }

    private void doTestEncryptByPrivateKey(byte[] input) {
        final byte[] encrypt = RsaCipher.encrypt(input, randomKey.getPrivateKey());
        errorCollector.checkThat(RsaCipher.decrypt(encrypt, randomKey.getPublicKey()), equalTo(input));
    }
}