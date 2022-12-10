package com.gearstick.controllers;

// import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.util.Duration;

public class PlayerController {

    @FXML
    private MediaView mainMediaView;

    @FXML
    private Button playButton;

    @FXML
    private Slider timeSlider;

    @FXML
    private void start() {
        // File file = new File("C:\\Users\\ilker\\Videos\\Valorant\\Valorant 2022.11.04
        // - 15.14.16.03.DVR.mp4");
        // Media media = new Media(file.toURI().toString()); //from file
        String url = "https://cdn.discordapp.com/attachments/918939943773044797/1051180857869746238/2022-12-09_15-14-51.mp4";
        Media media = new Media(url); // from web url
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        // mediaPlayer.setRate(1.5);
        mediaPlayer.setOnReady(() -> {
            timeSlider.setMin(0);
            timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            timeSlider.setValue(0);
        });
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            timeSlider.setValue(newValue.toSeconds());
        });
        timeSlider.pressedProperty().addListener((_0, isReleased, isPressed) -> {
            if (isPressed) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                mediaPlayer.play();
            }
        });

        playButton.setVisible(false);
        timeSlider.setVisible(true);
        mainMediaView.setOnError((e) -> System.out.println("Error: " + e));
        mainMediaView.setMediaPlayer(mediaPlayer);

    }
}
