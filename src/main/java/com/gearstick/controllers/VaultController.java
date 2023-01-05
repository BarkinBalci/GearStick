package com.gearstick.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import com.gearstick.Main;
import com.gearstick.vault.Vault;
import com.gearstick.vault.VaultStore;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class VaultController implements Initializable {

    public static SimpleObjectProperty<Vault> currentVault = new SimpleObjectProperty<Vault>();

    @FXML
    public Text vaultnameText = new Text();

    @FXML
    public Text vaultIsValidatedText = new Text();

    @FXML
    public Accordion credentialsAccordion = new Accordion();

    @FXML
    public static void requestLoginOrRegister() {
        loadVaults();
        if (VaultStore.vaults.size() == 0) {
            // redirect to register
            Main.setRoot("register");
        } else {
            if (currentVault.get() == null || !currentVault.get().isValidated()) {
                // redirect to login
                Main.setRoot("login");
            } else
                Main.setRoot("vault");
        }
    }

    public static void login(String name, String password) throws Exception {
        var vault = VaultStore.vaults.get(name);
        if (vault == null) {
            throw new Exception("No vault found for this name");
        }

        SecretKey secretKey;

        try {
            secretKey = vault.getKey(password);
        } catch (Exception e) {
            throw new Exception("Invalid password");
        }

        if (vault.validate(secretKey)) {
            setVault(vault);
        } else {
            throw new Exception("Wrong password");
        }

        requestLoginOrRegister();
    }

    public static void setVault(Vault vault) {
        if (vault == null)
            return;

        currentVault.set(vault);
    }

    public static void loadVaults() {
        VaultStore.loadVaults();

        if (Main.DEBUG && VaultStore.vaults.size() == 0) {
            System.out.println("DEBUG: Creating test vault");
            try {
                Vault newVault = new Vault("test", "testpass");
                newVault.addCredential("Twitch", "testpass");
                newVault.addCredential("Twitter", "testpass");
                VaultStore.createVault(newVault);
                setVault(VaultStore.vaults.get("test"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TitledPane createCredentialsPane(String key) {
        try {
            var contents = Main.loadFXML("Vault/CredentialPane", new VaultCredentialController(key));
            TitledPane pane = (TitledPane) contents;
            return pane;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void reRenderVault(Vault vault) {
        if (vault != null) {
            vaultnameText.setText(vault.name);
            vaultIsValidatedText.setText(vault.isValidated() ? "Validated" : "Not validated");

            if (vault.isValidated()) {
                vault.getCredentialKeys().forEach(key -> {
                    credentialsAccordion.getPanes().add(createCredentialsPane(key));
                });
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (currentVault.get() != null)
            credentialsAccordion.getPanes().get(0).setVisible(false);

        reRenderVault(currentVault.get());
        currentVault.addListener((e, oldVault, newVault) -> reRenderVault(newVault));
        loadVaults();
    }

}
