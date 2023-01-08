package com.gearstick.controllers.vault;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import com.gearstick.Main;
import com.gearstick.vault.Vault;
import com.gearstick.vault.VaultStore;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class VaultController implements Initializable {

    /**
     * The current vault with onChange-listener
     */
    public static SimpleObjectProperty<Vault> currentVault = new SimpleObjectProperty<>();

    /**
     * Auto redirect to log in or register
     */
    @FXML
    public static void requestLoginOrRegister() {
        loadVaults();
        if (VaultStore.vaults.size() == 0) {
            // redirect to register
            Main.setRoot("register");
        } else {
            if (currentVault.get() == null || !currentVault.get().isValidated()) {
                // redirect to log in
                Main.setRoot("login");
            } else
                Main.setRoot("vault", new VaultDashboardController(currentVault.get()));
        }
    }

    public static void register(String name, String password) throws Exception {
        Vault vault = new Vault(name, password);
        // try to create vault and check if it already exists
        if (VaultStore.createVault(vault) == null) {
            // show a dialog to ask if user wants to overwrite existing vault
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Overwrite Existing Vault");
            dialog.setHeaderText("Are you sure you want to overwrite existing Vault?");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteVault(name);
                VaultStore.createVault(vault);
            } else
                return;
        }

        currentVault.set(vault);
        requestLoginOrRegister();
    }

    public static void login(String name, String password) throws Exception {
        var vault = VaultStore.vaults.get(name);
        if (vault == null)
            throw new Exception("No vault found for this name");

        SecretKey secretKey;

        try {
            secretKey = vault.getKey(password);
        } catch (Exception e) {
            throw new Exception("Invalid password");
        }

        if (vault.validate(secretKey))
            currentVault.set(vault);
        else
            throw new Exception("Wrong password");

        requestLoginOrRegister();
    }

    public static void logout() {
        if (currentVault.get() != null)
            currentVault.get().invalidate();

        requestLoginOrRegister();
    }

    public static void deleteVault(String name) {
        if (currentVault.get() != null && currentVault.get().name.equals(name))
            currentVault.set(null);

        VaultStore.deleteVault(name);
        requestLoginOrRegister();
    }

    public static void deleteCredential(String key) {
        currentVault.get().deleteCredential(key);
        VaultStore.saveVaultToFolder(currentVault.get());
        requestLoginOrRegister();
    }

    /**
     * we require the vault parameter because the security reasons
     * inserting a credential done by external window
     */
    public static void insertCredential(Vault vault, String key, String value) {
        if (vault != null && vault.isValidated()) {
            vault.addCredential(key, value);
            VaultStore.saveVaultToFolder(vault);
            requestLoginOrRegister();
        }
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
        currentVault.addListener((e, o, vault) -> requestLoginOrRegister());
    }

}
