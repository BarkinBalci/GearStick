package com.gearstick.vault;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.gearstick.Cryptography;

public class Vault implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public String name;

    private IvParameterSpec IV;
    private String SALT;
    private String cipherResult;

    /**
     * transient does not serialize the field
     * of course, SECURITY :)
     * 
     * @implNote storing the KEY
     *           for the CRUID password
     *           operations to vault.
     */
    private transient SecretKey KEY = null;

    public static String generateInput(IvParameterSpec IV, String SALT) {
        return new String(IV.getIV()) + SALT;
    }

    /**
     * import a vault
     */
    public Vault(String name, IvParameterSpec IV, String SALT, String crypto) {
        this.name = name;
        this.IV = IV;
        this.SALT = SALT;
        this.cipherResult = crypto;
    }

    /**
     * create a new vault
     */
    public Vault(String name, SecretKey KEY) throws Exception {
        this.name = name;
        this.IV = Cryptography.generateIv(16);
        this.SALT = Cryptography.generateSalt(16).toString();
        this.cipherResult = getEncryptionCipher(KEY);
    }

    /**
     * create a new vault with raw string
     */
    public Vault(String name, String password) throws Exception {
        this.name = name;
        this.IV = Cryptography.generateIv(16);
        this.SALT = Cryptography.generateSalt(16).toString();
        this.KEY = Cryptography.generateKey(password, SALT);
        this.cipherResult = getEncryptionCipher(KEY);
    }

    public String getEncryptionCipher(SecretKey KEY) throws Exception {
        return Cryptography
                .encrypt("AES/CBC/PKCS5Padding", generateInput(IV, SALT), KEY, IV);

    }

    public Boolean isValidated() {
        return KEY != null;
    }

    public Boolean validate(SecretKey KEY) throws Exception {
        if (getEncryptionCipher(KEY).equals(cipherResult)) {
            this.KEY = KEY;
            return true;
        }

        return false;
    }
}
