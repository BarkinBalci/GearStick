package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.Main;
import com.gearstick.vault.VaultStore;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class VaultLoginController implements Initializable {

    @FXML
    public ComboBox<String> vaultnameComboBox = new ComboBox<>();
    @FXML
    public PasswordField vaultpasswordPasswordField = new PasswordField();
    @FXML
    public Text errorText = new Text();

    @FXML
    private void toggleShowPass() {
        vaultpasswordPasswordField.setManaged(!vaultpasswordPasswordField.isManaged());
    }

    @FXML
    private void login() {
        if (vaultnameComboBox.getSelectionModel().isEmpty() || vaultpasswordPasswordField.getText().isEmpty()) {
            errorText.setText("Error: Please fill all fields");
            return;
        }

        try {
            VaultController.login(vaultnameComboBox.getSelectionModel().getSelectedItem(),
                    vaultpasswordPasswordField.getText());
        } catch (Exception e) {
            errorText.setText("Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vaultnameComboBox.getItems().addAll(VaultStore.vaults.keySet());
        if (!vaultnameComboBox.getItems().isEmpty()) {
            if (VaultController.currentVault.get() != null)
                // current vault is set, but not validated
                vaultnameComboBox.getSelectionModel().select(VaultController.currentVault.get().name);
            else
                vaultnameComboBox.getSelectionModel().select(0);
        }
        errorText.setText("");
        if (Main.DEBUG && !vaultnameComboBox.getItems().isEmpty()) {
            errorText.setText("DEBUG: Developer mode is enabled, 'test' vault's password is 'testpass'");
        }
    }
}
