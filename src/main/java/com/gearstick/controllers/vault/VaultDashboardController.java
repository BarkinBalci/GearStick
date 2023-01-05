package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.Main;
import com.gearstick.vault.Vault;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class VaultDashboardController implements Initializable {

    private Vault vault;

    @FXML
    public Text vaultnameText = new Text();

    @FXML
    public Text vaultIsValidatedText = new Text();

    @FXML
    public Accordion credentialsAccordion = new Accordion();

    public VaultDashboardController(Vault vault) {
        this.vault = vault;
    }

    @FXML
    public void logout() {
        VaultController.logout();
    }

    public TitledPane createCredentialsPane(String key) {
        try {
            var contents = Main.loadFXML("Vault/CredentialPane", new VaultCredentialController(key));
            TitledPane pane = (TitledPane) contents;
            return pane;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        credentialsAccordion.getPanes().get(0).setVisible(false);

        vaultnameText.setText(vault.name);
        vaultIsValidatedText.setText(vault.isValidated() ? "Validated" : "Not validated");

        if (vault.isValidated()) {
            vault.getCredentialKeys().forEach(key -> {
                credentialsAccordion.getPanes().add(createCredentialsPane(key));
            });
        }
    }
}
