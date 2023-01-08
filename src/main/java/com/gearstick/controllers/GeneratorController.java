package com.gearstick.controllers;

import com.gearstick.Generator;
import com.gearstick.Main;
import com.gearstick.controllers.vault.VaultAddCredentialController;
import com.gearstick.controllers.vault.VaultController;
import com.gearstick.vault.Vault;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.gearstick.Main.copyClipboard;

public class GeneratorController implements Initializable {
    @FXML
    private TextArea passwordTextArea;
    @FXML
    private Label lengthLabel = new Label("8");
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
    private Button saveButton;

    @FXML
    private void save() throws Exception {
        var password = passwordTextArea.getText();
        var currentVault = VaultController.currentVault.get();
        if (password.isEmpty() || currentVault == null) {
            return;
        }

        Stage stage = new Stage();
        var contoller = new VaultAddCredentialController(currentVault, stage, "", password);

        stage.setTitle("Insert credential");
        stage.setScene(new Scene(Main.loadFXML("Vault/AddCredential", contoller)));
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    private void setSaveButtonDisabled(Vault vault) {
        if (vault == null || !vault.isValidated()) {
            saveButton.setDisable(true);
        } else {
            saveButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSaveButtonDisabled(VaultController.currentVault.get());
        VaultController.currentVault.addListener((o, old, vault) -> setSaveButtonDisabled(vault));

        lengthSlider.valueProperty()
                .addListener((observableValue, oldNumber, newNumber) -> lengthLabel.setText(newNumber.intValue() + ""));
    }

    @FXML
    private void copyGenerated() {
        copyClipboard(passwordTextArea.getText());
    }

    @FXML
    private void generatePassword() {
        passwordTextArea.setText(Generator.generatePassword((int) lengthSlider.getValue(), specialCheckBox.isSelected(),
                numberCheckBox.isSelected(), lowercaseCheckBox.isSelected(), uppercaseCheckBox.isSelected()));
    }
}
