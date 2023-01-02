package com.gearstick.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.Main;
import com.gearstick.vault.Vault;
import com.gearstick.vault.VaultStore;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class VaultController implements Initializable {
    private static Vault currentVault = null;

    @FXML
    public Text vaultnameText;

    @FXML
    private void registerVault() {
        try {
            setVault(VaultStore.createVault(new Vault("null", "null")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public static void requestLoginOrRegister() {
        // TODO: not working
        System.out.println("size: " + VaultStore.vaults.size() + " current: " + currentVault);
        if (VaultStore.vaults.size() == 0) {
            // redirect to register
            Main.setRoot("register");
        } else {
            if (currentVault == null) {
                // redirect to login
                Main.setRoot("login");
            } else
                Main.setRoot("vault");
        }
    }

    public void login(String name, String password) throws Exception {
        // TODO: convert String password to SecretKey
        // help needed
        var vault = VaultStore.vaults.get(name);
        if (vault == null) {
            throw new Exception("No vault found for this name");
        }

        if (vault.validate(null)) {
            setVault(vault);
            requestLoginOrRegister();
        } else {
            throw new Exception("Wrong password");
        }

    }

    public void setVault(Vault vault) {
        currentVault = vault;
        vaultnameText.setText(vault.name);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("NOT WORKING ?????????");

        VaultStore.loadVaults();
        try {
            VaultStore.vaults.put("test", new Vault("test", "test2"));
            setVault(VaultStore.vaults.get("test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(VaultStore.vaults);
    }

}
