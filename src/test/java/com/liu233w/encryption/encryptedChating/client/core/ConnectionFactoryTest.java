package com.liu233w.encryption.encryptedChating.client.core;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static com.google.common.truth.Truth.assertThat;

public class ConnectionFactoryTest {

    @Test
    public void testDataSending() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ConnectionFactory.sendDataPackToStream(byteArrayOutputStream, BigInteger.valueOf(2333).toByteArray());

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        final byte[] bytes = ConnectionFactory.readDataPackFromStream(byteArrayInputStream);
        final BigInteger bigInteger = new BigInteger(bytes);

        assertThat(bigInteger.longValue()).isEqualTo(2333);
    }
}