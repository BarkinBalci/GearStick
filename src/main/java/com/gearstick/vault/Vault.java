package com.gearstick.vault;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.gearstick.Cryptography;

public class Vault implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public final String name;

    private final byte[] IV;
    private final String SALT;
    private final String cipherResult;
    private final HashMap<String, String> credentials = new HashMap<>();

    /**
     * transient does not serialize the field
     * of course, SECURITY :)
     * 
     * @implNote storing the KEY
     *           for the CRUID password
     *           operations to vault.
     */
    private transient SecretKey KEY;
    private transient HashMap<String, String> decryptedCredentials = new HashMap<>();

    public static String generateInput(byte[] IV, String SALT) {
        return new String(IV) + SALT;
    }

    /**
     * import a vault
     */
    public Vault(String name, byte[] IV, String SALT, String crypto) {
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
        this.IV = Cryptography.generateIv(16).getIV();
        this.SALT = String.valueOf(Cryptography.generateSalt(16));
        this.cipherResult = getEncryptionCipher(KEY);
    }

    /**
     * create a new vault with raw string
     */
    public Vault(String name, String password) throws Exception {
        this.name = name;
        this.IV = Cryptography.generateIv(16).getIV();
        this.SALT = String.valueOf(Cryptography.generateSalt(16));
        this.KEY = Cryptography.generateKey(password, SALT);
        this.cipherResult = getEncryptionCipher(KEY);
    }

    public String getEncryptionCipher(SecretKey KEY) throws Exception {
        return Cryptography
                .encrypt("AES/CBC/PKCS5Padding", generateInput(IV, SALT), KEY, new IvParameterSpec(IV));

    }

    public boolean isValidated() {
        return KEY != null;
    }

    public void invalidate() {
        KEY = null;
    }

    public boolean validate(SecretKey KEY) throws Exception {
        if (getEncryptionCipher(KEY).equals(cipherResult)) {
            this.KEY = KEY;

            if (decryptedCredentials == null)
                decryptedCredentials = new HashMap<String, String>();

            // decrypt credentials
            credentials.forEach((key, value) -> {
                try {
                    String decrypted = Cryptography.decrypt("AES/CBC/PKCS5Padding", value, KEY,
                            new IvParameterSpec(IV));
                    decryptedCredentials.put(key, decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return true;
        }

        return false;
    }

    public SecretKey getKey(String password) throws Exception {
        return Cryptography.generateKey(password, SALT);
    }

    public byte[] getIV() {
        return IV;
    }

    public String getSalt() {
        return SALT;
    }

    public ArrayList<String> getCredentialKeys() {
        return new ArrayList<>(credentials.keySet());
    }

    public String getCredential(String key) throws Exception {
        if (isValidated())
            // since user is validated, we can return the decrypted credentials
            return decryptedCredentials.get(key);

        throw new Exception("Vault is not validated");
    }

    // safe to call if secret key is present
    public String getCredential(String key, SecretKey KEY) {
        try {
            if (!isValidated())
                validate(KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedCredentials.get(key);
    }

    public boolean addCredential(String key, String value) {
        if (isValidated()) {
            try {
                String encrypted = Cryptography.encrypt("AES/CBC/PKCS5Padding", value, KEY,
                        new IvParameterSpec(IV));
                credentials.put(key, encrypted);
                decryptedCredentials.put(key, value);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
