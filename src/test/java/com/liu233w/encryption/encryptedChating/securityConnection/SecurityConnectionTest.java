package com.liu233w.encryption.encryptedChating.securityConnection;

import com.liu233w.encryption.encryptedChating.cipher.DesKey;
import com.liu233w.encryption.encryptedChating.cipher.RsaCipher;
import com.liu233w.encryption.encryptedChating.cipher.RsaKeyPair;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.SignatureException;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityConnectionTest {

    @Test
    public void testMessageSending() throws IOException, SignatureException {
        final DesKey sessionKey = DesKey.random();
        final RsaKeyPair u1Keys = RsaCipher.generateKey();
        final RsaKeyPair u2Keys = RsaCipher.generateKey();

        final Socket u1Socket = mock(Socket.class);
        final Socket u2Socket = mock(Socket.class);

        // u1 send to u2
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(u1Socket.getOutputStream()).thenReturn(byteArrayOutputStream);

        final byte[] data = "Hello World".getBytes(Charset.forName("utf-8"));

        final SecurityConnection u1Connection = new SecurityConnection(u1Socket, sessionKey, u1Keys.getPrivateKey(), u2Keys.getPublicKey());
        u1Connection.send(data);

        final byte[] sended = byteArrayOutputStream.toByteArray();

        // for u2
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sended);
        when(u2Socket.getInputStream()).thenReturn(byteArrayInputStream);

        final SecurityConnection u2Connection = new SecurityConnection(u2Socket, sessionKey, u2Keys.getPrivateKey(), u1Keys.getPublicKey());
        final byte[] recved = u2Connection.recv();

        // assert
        assertThat(recved).isEqualTo(data);
    }
}