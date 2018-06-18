package com.liu233w.encryption.encryptedChating.cipher;

import java.util.Random;

public class DesKey {
    private long key;

    public DesKey(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    /**
     * Generate random des key
     *
     * @return
     */
    public static DesKey random() {
        return new DesKey(new Random().nextLong());
    }
}
