package com.gearstick.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import com.gearstick.Checksum;
import com.gearstick.Main;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ChecksumController implements Initializable {
    private SimpleObjectProperty<File> currentFile = new SimpleObjectProperty<>();

    @FXML
    private TextField currentFileName = new TextField("No file selected");

    @FXML
    private TextArea hashTextArea;

    @FXML
    private TextField targetTextField;

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Any File");
        currentFile.set(fileChooser.showOpenDialog(Main.scene.getWindow()));
    }

    @FXML
    private void calcChecksum() throws IOException, NoSuchAlgorithmException {
        hashTextArea.setText(Checksum.getChecksum("SHA-256", currentFile.get()));
    }

    @FXML
    private void compareHash(){
        boolean result = Checksum.compareHash(hashTextArea.getText(), targetTextField.getText());
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Comparison Result");
        if(result)
            dialog.setContentText("Hashes match!");
        else
            dialog.setContentText("Comparison failed. The file might be compromised, use the file on your own risk!");
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        dialog.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentFile.addListener((o, oldValue, newValue) -> {
            if (newValue == null)
                currentFileName.setText("No file selected");
            else
                currentFileName.setText(newValue.getName());
        });
    }

}
