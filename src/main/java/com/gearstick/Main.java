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

    public static final Boolean DEBUG = true;

    /**
     * save fxml load result in order to
     * save cpu/memory to not load again
     */
    private static final HashMap<String, Parent> screenMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #272c33;");

        scene = new Scene(vbox);
        loadRoot();
        makeStageResizable(stage);
        stage.setTitle("GearStick");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String root, Object controller) {
        try {
            switch (root) {
                case "main" -> Main.root[1] = loadFXML("Cryptography");
                case "vault" -> Main.root[1] = loadFXML("Vault/Dashboard", controller);
                case "register" -> Main.root[1] = loadFXML("Vault/Register", null, true); // no-cache (re-render)
                case "login" -> Main.root[1] = loadFXML("Vault/Login", null, true); // no-cache (re-render)
                case "generator" -> Main.root[1] = loadFXML("Generator");
                case "checksum" -> Main.root[1] = loadFXML("Checksum");
                default -> throw new RuntimeException("Unhandled root mode");
            }
            loadRoot();
        } catch (IOException e) {
            // frontend (JavaFX file) error
            e.printStackTrace();
        }
    }

    public static void setRoot(String root) {
        setRoot(root, null);
    }

    private static void loadRoot() throws IOException {
        if (root == null) {
            root = new Parent[] { loadFXML("MenuBar"), loadFXML("Cryptography") };
        }
        ((VBox) scene.getRoot()).getChildren().setAll(root);
    }

    public static Parent loadFXML(String fxml) throws IOException {
        // default
        return loadFXML(fxml, null, false);
    }

    public static Parent loadFXML(String fxml, Object controller) throws IOException {
        // with controller
        return loadFXML(fxml, controller, false);
    }

    public static Parent loadFXML(String fxml, Object controller, boolean noCache) throws IOException {
        var result = screenMap.get(fxml);
        if (result != null)
            return result;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        if (controller != null)
            // custom controller
            fxmlLoader.setController(controller);

        if (noCache || controller != null)
            // if controller is present, do not use cache
            return fxmlLoader.load();

        screenMap.put(fxml, fxmlLoader.load());
        return screenMap.get(fxml);
    }

    private void makeStageResizable(Stage stage) {
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 800) {
                if (root[1] instanceof VBox)
                    ((VBox) root[1]).prefWidthProperty().bind(scene.widthProperty());
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 600) {
                if (root[1] instanceof VBox)
                    ((VBox) root[1]).prefHeightProperty().bind(scene.heightProperty());
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}