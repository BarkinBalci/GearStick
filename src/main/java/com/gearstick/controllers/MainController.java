package com.gearstick.controllers;

import java.io.IOException;

import com.gearstick.AES256;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    private void Encrypt() throws IOException {
        String originalString = "GearStick";
        String encryptedString = AES256.encrypt(originalString, "ÇOK GİZLİ ANAHTAR", "TUZLUK");
        System.out.println(encryptedString);
    }
    @FXML
    private void Decrypt() throws IOException{
        String decryptedString = AES256.decrypt("Imq8GHa8DxOD6w26oIHi2Q==", "ÇOK GİZLİ ANAHTAR", "TUZLUK");
        System.out.println(decryptedString);
    }
}
