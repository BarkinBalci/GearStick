package com.gearstick.controllers;

import com.gearstick.Main;
import com.gearstick.controllers.vault.VaultController;

import javafx.fxml.FXML;

public class MainController {

    @FXML
    public void switchToVault() {
        VaultController.requestLoginOrRegister();
    }

    @FXML
    private void switchToLogin() {
        Main.setRoot("login");
    }

    @FXML
    public void switchToRegister() {
        Main.setRoot("register");
    }

    @FXML
    public void switchToCryptography() {
        Main.setRoot("main");
    }

    @FXML
    public void switchToGenerator() {
        Main.setRoot("generator");
    }

    @FXML
    public void switchToChecksum() {
        Main.setRoot("checksum");
    }
}
