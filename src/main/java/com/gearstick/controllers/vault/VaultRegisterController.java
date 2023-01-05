package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class VaultRegisterController implements Initializable {

    @FXML
    public TextField vaultnameText = new TextField();
    @FXML
    public PasswordField vaultpasswordPasswordField = new PasswordField();
    @FXML
    public Text errorText = new Text();

    @FXML
    private void toggleShowPass() {
        vaultpasswordPasswordField.setManaged(!vaultpasswordPasswordField.isManaged());
    }

    @FXML
    private void createVault() {
        if (vaultnameText.getText().isEmpty() || vaultpasswordPasswordField.getText().isEmpty()) {
            errorText.setText("Error: Please fill all fields");
            return;
        }

        try {
            VaultController.register(vaultnameText.getText(),
                    vaultpasswordPasswordField.getText());
        } catch (Exception e) {
            errorText.setText("Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorText.setText("");
    }
}
