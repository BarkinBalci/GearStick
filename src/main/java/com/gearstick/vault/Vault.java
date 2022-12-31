package com.gearstick.vault;

import java.io.File;
import com.gearstick.Cryptography;

public class Vault implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public String name;

    private byte[] IV;
    private String SALT;
    private String cipherResult;

    public static String generateInput(byte[] IV, String SALT) {
        return new String(IV) + SALT;
    }

    public Vault(String name, byte[] IV, String SALT, String crypto) {
        this.name = name != null ? name : "default";
        this.IV = IV;
        this.SALT = SALT;
        this.cipherResult = crypto;
    }

    public Boolean validate(String KEY) {
        // TODO
        return Cryptography.decrypt("AES/CBC/PKCS5Padding", generateInput(IV, SALT), KEY, SALT, null)
                .equals(cipherResult);
    }
}
