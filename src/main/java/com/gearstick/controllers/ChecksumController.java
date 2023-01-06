package com.gearstick.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.gearstick.Main;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ChecksumController implements Initializable {
    private SimpleObjectProperty<File> currentFile = new SimpleObjectProperty<>();

    @FXML
    private TextField currentFileName = new TextField("No file selected");

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Any File");
        currentFile.set(fileChooser.showOpenDialog(Main.scene.getWindow()));
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
