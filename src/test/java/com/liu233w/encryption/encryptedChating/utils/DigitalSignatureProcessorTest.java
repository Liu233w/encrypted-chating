package com.liu233w.encryption.encryptedChating.utils;

import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.cipher.RsaKeyPair;
import org.junit.Test;

import java.nio.charset.Charset;

import static com.google.common.truth.Truth.assertThat;

public class DigitalSignatureProcessorTest {

    @Test
    public void testEncryptionAndVerify() {
        final byte[] plain = "Hello world".getBytes(Charset.forName("utf-8"));
        final RsaKeyPair rsaKeyPair = RsaCipher.generateKey();

        final byte[] signature = DigitalSignatureProcessor.generate(plain, rsaKeyPair.getPrivateKey());
        assertThat(DigitalSignatureProcessor.verify(plain, signature, rsaKeyPair.getPublicKey()))
                .isTrue();
    }
}