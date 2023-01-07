package com.gearstick.controllers;

import com.gearstick.Checksum;
import com.gearstick.Main;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class ChecksumController implements Initializable {
    private SimpleObjectProperty<File> currentFile = new SimpleObjectProperty<>();

    @FXML
    private TextField currentFileName = new TextField("No file selected");

    @FXML
    private TextArea hashTextArea;

    @FXML
    private TextField targetTextField;
    @FXML
    public ComboBox<String> algorithmComboBox;
    @FXML
    public ProgressBar progressBar;

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Any File");
        currentFile.set(fileChooser.showOpenDialog(Main.scene.getWindow()));
        progressBar.setProgress(0.0F);
        hashTextArea.clear();
    }

    @FXML
    private void calcChecksum() throws IOException, NoSuchAlgorithmException {
        hashTextArea.setText(Checksum.getChecksum(algorithmComboBox.getValue(), currentFile.get()));
        progressBar.setProgress(1.0F);
    }

    @FXML
    private void compareHash() {
        boolean equals = hashTextArea.getText().equals(targetTextField.getText());
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Comparison Result");

        if (equals)
            dialog.setContentText("Hashes match!");
        else
            dialog.setContentText("Comparison failed. The file might be compromised, use the file on your own risk!");

        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        dialog.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> algorithms = FXCollections.observableArrayList("SHA-256", "MD5");
        algorithmComboBox.getItems().addAll(algorithms);
        algorithmComboBox.getSelectionModel().select(0);
        currentFile.addListener((o, oldValue, newValue) -> {
            if (newValue == null)
                currentFileName.setText("No file selected");
            else
                currentFileName.setText(newValue.getName());
        });
    }

}
