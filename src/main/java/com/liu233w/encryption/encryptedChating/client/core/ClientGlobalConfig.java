package com.liu233w.encryption.encryptedChating.client.core;

import com.liu233w.encryption.encryptedChating.cipher.RsaKeyPair;

public class ClientGlobalConfig {

    public static volatile String keyServerAddress = "localhost";

    public static volatile int keyServerPort = 8000;

    public static volatile RsaKeyPair localKeyPair;
}
