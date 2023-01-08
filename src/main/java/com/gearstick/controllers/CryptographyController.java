package com.gearstick.controllers;

import com.gearstick.Cryptography;
import com.gearstick.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.awt.Desktop;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static com.gearstick.Main.copyClipboard;

public class CryptographyController {

    FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private TextField secretKeyTextField;

    // TODO: error handling

    @FXML
    private void Encrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        String inputText = inputTextField.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        File inputFile = new File(inputText);
        if (inputFile.exists()) {
            File outputFile = new File(inputFile.getParent(), inputFile.getName() + ".gear");
            Cryptography.encryptFile("AES/CBC/PKCS5Padding", inputFile, originalKey, Cryptography.getIV(), outputFile);
            outputTextField.setText(outputFile.getAbsolutePath());
            Desktop.getDesktop().open(outputFile.getParentFile());
        } else {
            String encryptedString = Cryptography.encrypt("AES/CBC/PKCS5Padding", inputText, originalKey,
                    Cryptography.getIV());
            outputTextField.setText(encryptedString);
        }
    }

    @FXML
    private void Decrypt() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        String inputText = inputTextField.getText();
        String encodedKey = secretKeyTextField.getText();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        File inputFile = new File(inputText);
        if (inputFile.exists()) {
            File outputFile = new File(inputFile.getParent(), inputFile.getName().replace(".gear", ""));
            Cryptography.decryptFile("AES/CBC/PKCS5Padding", inputFile, originalKey, Cryptography.getIV(), outputFile);
            outputTextField.setText(outputFile.getAbsolutePath());
            Desktop.getDesktop().open(outputFile.getParentFile());
        } else {
            String decryptedString = Cryptography.decrypt("AES/CBC/PKCS5Padding", inputText, originalKey,
                    Cryptography.getIV());
            outputTextField.setText(decryptedString);
        }
    }

    @FXML
    private void openFile() {
        fileChooser.setTitle("Open Any File");
        File file = fileChooser.showOpenDialog(Main.scene.getWindow());
        if (file != null)
            inputTextField.setText(file.getAbsolutePath());
    }

    @FXML
    private void copyCipher() {
        copyClipboard(outputTextField.getText());
    }

    @FXML
    private void Randomize() throws NoSuchAlgorithmException {
        SecretKey secretKey = Cryptography.randomizeKey(256);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }

    @FXML
    private void Generate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String passwordText = secretKeyTextField.getText();
        SecretKey secretKey = Cryptography.generateKey(passwordText, Cryptography.getSalt());
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        secretKeyTextField.setText(encodedKey);
    }
}
