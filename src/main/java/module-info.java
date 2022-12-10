module com.gearstick.gearstick {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.gearstick.gearstick to javafx.fxml;
    exports com.gearstick.gearstick;
}