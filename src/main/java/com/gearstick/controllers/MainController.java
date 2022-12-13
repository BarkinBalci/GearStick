package com.gearstick.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.Gears;
import com.gearstick.Gears.EncryptionType;

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
    private void Encrypt() {
        EncryptionType encryptionType = gearComboBox.getSelectionModel().getSelectedItem();
        String originalString = inputTextField.getText();
        try {
            String encryptedString = Gears.encrypt(originalString, secretKeyTextField.getText(),
                    saltTextField.getText(), encryptionType);
            outputTextField.setText(encryptedString);
        } catch (Exception e) {
            outputTextField.setText(e.getMessage());
        }
    }

    @FXML
    private void Decrypt() {
        try {
            String decryptedString = Gears.decrypt(inputTextField.getText(), secretKeyTextField.getText(),
                    saltTextField.getText());
            outputTextField.setText(decryptedString);

        } catch (Exception e) {
            outputTextField.setText(e.getMessage());
        }
    }

    @FXML
    private ComboBox<EncryptionType> gearComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gearComboBox.setItems(FXCollections.observableArrayList(EncryptionType.AES256,
                EncryptionType.AES512));
        gearComboBox.getSelectionModel().selectFirst();
    }
}
