package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

import static com.gearstick.Main.copyClipboard;

public class VaultCredentialController implements Initializable {

    public String key;

    private SimpleBooleanProperty isShowed = new SimpleBooleanProperty(false);
    @FXML
    public TitledPane titledPane = new TitledPane();
    @FXML
    public Text passwordText = new Text();
    @FXML
    public Button toggleButton = new Button();
    @FXML
    public Button copyCredentialButton = new Button();
    @FXML
    public Button deleteCredentialButton = new Button();

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

    private void deleteCredential() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Credential");
        dialog.setHeaderText("Are you sure you want to delete this credential?");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK)
                VaultController.deleteCredential(this.key);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titledPane.setText(key);
        toggleButton.onActionProperty().set(e -> toggleShowPassword());
        copyCredentialButton.onActionProperty().set(e -> copyClipboard(getPassword()));
        deleteCredentialButton.onActionProperty().set(e -> deleteCredential());
        isShowed.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordText.setText(getPassword());
                toggleButton.setText("Hide");
            } else {
                passwordText.setText("");
                toggleButton.setText("Click to Show");
            }
        });
    }
}
