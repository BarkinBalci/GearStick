package com.gearstick;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.gearstick.controllers.vault.VaultController;
import com.gearstick.vault.Vault;
import com.gearstick.vault.VaultStore;

public class Cryptography {

    public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static SecretKey randomizeKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    /**
     * Generates a key by deriving it from a password. Using PBKDF2
     */
    public static SecretKey generateKey(String masterPassword, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }
    /**
     * Generates Initialization Vector using SecureRandom()
     */
    public static IvParameterSpec generateIv(int length) {
        byte[] iv = new byte[length];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Generates Salt using SecureRandom()
     */
    public static byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static Vault getAnyVault() {
        if (VaultController.currentVault.get() != null)
            // if user logged in, return current vault
            return VaultController.currentVault.get();
        if (VaultStore.vaults.size() > 0)
            // if user not logged in, return first vault
            return VaultStore.vaults.values().iterator().next();

        return null;
    }

    public static String getSalt() {
        if (getAnyVault() != null)
            return getAnyVault().getSalt();

        // default salt (which is not secure since user is not logged)
        return "default-salt";
    }

    public static IvParameterSpec getIV() {
        if (getAnyVault() != null)
            return new IvParameterSpec(getAnyVault().getIV());

        // default IV (which is not secure since user is not logged)
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        return new IvParameterSpec(iv);
    }
}
