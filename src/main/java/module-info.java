module com.gearstick {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.gearstick to javafx.fxml;
    opens com.gearstick.controllers to javafx.fxml;

    exports com.gearstick;
    exports com.gearstick.controllers;
}