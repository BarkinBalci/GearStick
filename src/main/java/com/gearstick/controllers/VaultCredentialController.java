package com.gearstick.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class VaultCredentialController implements Initializable {

    public String key;

    private SimpleBooleanProperty isShowed = new SimpleBooleanProperty(false);

    @FXML
    public TitledPane titledPane = new TitledPane();

    @FXML
    public Text passwordText = new Text();

    @FXML
    public Button toggleButton = new Button();

    public VaultCredentialController() {
        this.key = "DEFAULT";
        titledPane.setVisible(false);
    }

    public VaultCredentialController(String key) {
        this.key = key;
    }

    public void toggleShowPassword() {
        isShowed.set(!isShowed.get());
    }

    private String getPassword() {
        String password = null;
        try {
            password = VaultController.currentVault.get().getCredential(key);
        } catch (Exception e) {
            // vault is not validated, redirect to login
            VaultController.requestLoginOrRegister();
        }

        if (password == null) {
            // credential is not found, remove from ui
            titledPane.setVisible(false);
        }

        return password;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titledPane.setText(key);
        toggleButton.onActionProperty().set(e -> toggleShowPassword());
        isShowed.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordText.setText(getPassword());
                toggleButton.setText("Hide");
            } else {
                passwordText.setText("");
                toggleButton.setText("Show");
            }
        });
    }
}
