package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.vault.Vault;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VaultAddCredentialController implements Initializable {
    private Vault vault;
    private Stage stage;

    @FXML
    private TextField credentiaTextField;
    @FXML
    private PasswordField credentialPasswordField;
    @FXML
    private Text errorText;

    public VaultAddCredentialController(Vault vault, Stage stage) {
        this.vault = vault;
        this.stage = stage;
    }

    @FXML
    private void close() {
        this.stage.close();
    }

    @FXML
    private void insertCredential() {
        var key = credentiaTextField.getText();
        var value = credentialPasswordField.getText();

        if (key.isEmpty() || value.isEmpty()) {
            errorText.setText("Please fill in all fields");
            return;
        }

        VaultController.insertCredential(this.vault, key, value);
        close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorText.setText("");
    }

}
