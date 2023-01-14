module com.gearstick {
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    requires java.desktop;

    opens com.gearstick to javafx.fxml;
    opens com.gearstick.controllers to javafx.fxml;
    opens com.gearstick.controllers.vault to javafx.fxml;

    exports com.gearstick;
    exports com.gearstick.vault;
    exports com.gearstick.controllers;
    exports com.gearstick.controllers.vault;
}