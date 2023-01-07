package com.gearstick;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

import com.gearstick.vault.VaultStore;

public class Main extends Application {
    public static Scene scene;
    private static Parent[] root;

    public static final Boolean DEBUG = true;

    /**
     * save fxml load result in order to
     * save cpu/memory to not load again
     */
    private static final HashMap<String, Parent> screenMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(new VBox());
        loadRoot();

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

    public static BorderPane getMainPane() {
        return (BorderPane) root[0];
    }

    private static void loadRoot() throws IOException {
        if (root == null) {
            root = new Parent[] { loadFXML("Main"),
                    VaultStore.vaults.size() == 0 ? loadFXML("Pages/Welcome") : loadFXML("Vault/Login", null, true) };
        }
        getMainPane().setCenter(root[1]);
        scene.setRoot(root[0]);
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

    public static void copyClipboard(String str) {
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public static void main(String[] args) {
        VaultStore.loadVaults();
        launch();
    }
}