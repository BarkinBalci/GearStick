package com.gearstick;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {
    private static Scene scene;
    private static Parent[] root;

    /**
     * save fxml load result in order to
     * save cpu/memory to not load again
     */
    private static HashMap<String, Parent> screenMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(new VBox());
        loadRoot();

        stage.setTitle("GearStick");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String root) {
        try {
            switch (root) {
                case "main" -> Main.root[1] = loadFXML("Cryptography");
                case "vault" -> Main.root[1] = loadFXML("Vault");
                case "register" -> Main.root[1] = loadFXML("Register");
                case "login" -> Main.root[1] = loadFXML("Login");
                case "generator" -> Main.root[1] = loadFXML("Generator");
                default -> throw new RuntimeException("Unhandled root mode");
            }
            loadRoot();
        } catch (IOException e) {
            // frontend (JavaFX file) error
            e.printStackTrace();
        }
    }

    private static void loadRoot() throws IOException {
        if (root == null) {
            root = new Parent[] { loadFXML("MenuBar"), loadFXML("Cryptography") };
        }
        ((VBox) scene.getRoot()).getChildren().setAll(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        var result = screenMap.get(fxml);
        if (result != null)
            return result;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        screenMap.put(fxml, fxmlLoader.load());
        return screenMap.get(fxml);
    }

    public static void main(String[] args) {
        launch();
    }
}