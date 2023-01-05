package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import com.gearstick.Main;
import com.gearstick.vault.Vault;
import com.gearstick.vault.VaultStore;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class VaultController implements Initializable {

    /**
     * The current vault with onChange-listener
     */
    public static SimpleObjectProperty<Vault> currentVault = new SimpleObjectProperty<Vault>();

    /**
     * Auto redirect to login or register
     */
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
                Main.setRoot("vault", new VaultDashboardController(currentVault.get()));
        }
    }

    public static void register(String name, String password) throws Exception {
        Vault vault = new Vault(name, password);
        if (VaultStore.createVault(vault) == null)
            // TODO: replace vault with modal confirmation
            throw new Exception("Vault already exists");

        currentVault.set(vault);
        requestLoginOrRegister();
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
            currentVault.set(vault);
        } else {
            throw new Exception("Wrong password");
        }

        requestLoginOrRegister();
    }

    public static void logout() {
        if (currentVault.get() != null) {
            currentVault.get().invalidate();
        }

        requestLoginOrRegister();
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
                currentVault.set(VaultStore.vaults.get("test"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVaults();
        currentVault.addListener((e, o, vault) -> {
            requestLoginOrRegister();
        });
    }

}
