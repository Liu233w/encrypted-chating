package com.liu233w.encryption.encryptedChating.cipher;

import org.junit.Test;

import java.nio.charset.Charset;

import static com.google.common.truth.Truth.assertThat;

public class DesCipherTest {

    @Test
    public void testEncryptionAndDecryption() {

        final byte[] plain = "Hello world".getBytes(Charset.forName("utf-8"));
        final DesKey key = DesKey.random();

        final DesCipher cipher = new DesCipher(key);

        final byte[] encrypted = cipher.encrypt(plain);
        assertThat(encrypted).isNotEmpty();

        assertThat(cipher.decrypt(encrypted)).isEqualTo(plain);
    }
}