package com.gearstick.controllers;

import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.ResourceBundle;

import com.gearstick.Cryptography;
import com.gearstick.Generator;
import com.gearstick.Main;
import com.gearstick.controllers.vault.VaultController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainController implements Initializable {

    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextField outputTextField;
    @FXML
    private TextField secretKeyTextField;

    @FXML
    private Label lengthLabel = new Label("8");
    @FXML
    private TextArea passwordTextArea;
    @FXML
    private Slider lengthSlider = new Slider(0, 128, 8);
    @FXML
    private CheckBox numberCheckBox;
    @FXML
    private CheckBox lowercaseCheckBox;
    @FXML
    private CheckBox uppercaseCheckBox;
    @FXML
    private CheckBox specialCheckBox;

    @FXML
    private void Encrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String inputText = inputTextArea.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        String encryptedString = Cryptography.encrypt("AES/CBC/PKCS5Padding", inputText, originalKey,
                Cryptography.getIV());
        outputTextField.setText(encryptedString);
    }

    @FXML
    private void Decrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String inputText = inputTextArea.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        String decryptedString = Cryptography.decrypt("AES/CBC/PKCS5Padding", inputText, originalKey,
                Cryptography.getIV());
        outputTextField.setText(decryptedString);
    }

    public static void copyClipboard(String str){
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        Clipboard.getSystemClipboard().setContent(content);
    }
    @FXML
    private void copyGenerated(){
        copyClipboard(passwordTextArea.getText());
    }
    @FXML
    private void copyCipher(){
        copyClipboard(outputTextField.getText());
    }
    @FXML
    private void Randomize() throws NoSuchAlgorithmException {
        SecretKey secretKey = Cryptography.randomizeKey(128);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }

    @FXML
    private void Generate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String passwordText = secretKeyTextField.getText();
        SecretKey secretKey = Cryptography.generateKey(passwordText, Cryptography.getSalt().toString());
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }

    @FXML
    private void generatePassword() {
        passwordTextArea.setText(Generator.generatePassword((int) lengthSlider.getValue(), specialCheckBox.isSelected(),
                numberCheckBox.isSelected(), lowercaseCheckBox.isSelected(), uppercaseCheckBox.isSelected()));
    }

    @FXML
    public void switchToVault() {
        VaultController.requestLoginOrRegister();
    }

    @FXML
    public void switchToRegister() {
        Main.setRoot("register");
    }

    @FXML
    public void switchToCryptography() {
        Main.setRoot("main");
    }

    @FXML
    public void switchToGenerator() {
        Main.setRoot("generator");
    }

    @FXML
    public void switchToChecksum() {
        Main.setRoot("checksum");
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lengthSlider.valueProperty()
                .addListener((observableValue, oldNumber, newNumber) -> lengthLabel.setText(newNumber.intValue() + ""));
    }
}
