package com.gearstick.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.AES256;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    private void Encrypt() throws IOException {
        String originalString = inputTextField.getText();
        String encryptedString = AES256.encrypt(originalString, secretKeyTextField.getText(), saltTextField.getText());
        outputTextField.setText(encryptedString);
    }
    @FXML
    private void Decrypt() throws IOException{
        String decryptedString = AES256.decrypt(inputTextField.getText(), secretKeyTextField.getText(), saltTextField.getText());
        outputTextField.setText(decryptedString);
    }

    @FXML
    private ComboBox<String> gearComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gearComboBox.setItems(FXCollections.observableArrayList("AES256", "AES512", "SHA256", "SHA512"));
        gearComboBox.getSelectionModel().selectFirst();
    }
}
