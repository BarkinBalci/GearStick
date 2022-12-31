package com.gearstick.controllers;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.ResourceBundle;

import com.gearstick.Cryptography;
import com.gearstick.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainController implements Initializable {

    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private TextField secretKeyTextField;
    @FXML
    private TextField saltTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    private void Encrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String inputText = inputTextField.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        String encryptedString = Cryptography.encrypt("AES/CBC/PKCS5Padding", inputText, originalKey, Cryptography.getIV());
        outputTextField.setText(encryptedString);
    }

    @FXML
    private void Decrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String inputText = inputTextField.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        String decryptedString = Cryptography.decrypt("AES/CBC/PKCS5Padding", inputText, originalKey, Cryptography.getIV());
        outputTextField.setText(decryptedString);
    }

    @FXML
    private void Randomize() throws NoSuchAlgorithmException {
        SecretKey secretKey = Cryptography.randomizeKey(128);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }

    @FXML
    private void Generate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String passwordText = passwordTextField.getText();
        SecretKey secretKey = Cryptography.generateKey(passwordText, Cryptography.getSalt());
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }

    @FXML
    public void switchToVault() {
        Main.setRoot("vault");
    }

    @FXML
    public void switchToCryptography() {
        Main.setRoot("main");
    }

    @FXML
    public void switchToGenerator() {
        Main.setRoot("generator");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
