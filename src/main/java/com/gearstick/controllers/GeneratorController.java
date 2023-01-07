package com.gearstick.controllers;

import com.gearstick.Generator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

import static com.gearstick.Main.copyClipboard;


public class GeneratorController implements Initializable {
    @FXML
    private TextArea passwordTextArea;
    @FXML
    private Label lengthLabel = new Label("8");
    @FXML
    private Slider lengthSlider = new Slider(0, 128, 8);
    @FXML
    private CheckBox numberCheckBox;
    @FXML
    private CheckBox lowercaseCheckBox;
    @FXML
    private CheckBox uppercaseCheckBox;
    @FXML
    private CheckBox specialCheckBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lengthSlider.valueProperty()
                .addListener((observableValue, oldNumber, newNumber) -> lengthLabel.setText(newNumber.intValue() + ""));
    }
    @FXML
    private void copyGenerated(){
        copyClipboard(passwordTextArea.getText());
    }
    @FXML
    private void generatePassword() {
        passwordTextArea.setText(Generator.generatePassword((int) lengthSlider.getValue(), specialCheckBox.isSelected(),
                numberCheckBox.isSelected(), lowercaseCheckBox.isSelected(), uppercaseCheckBox.isSelected()));
    }
}
