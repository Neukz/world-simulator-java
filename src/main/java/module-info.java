module oop.worldsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens oop.worldsimulator to javafx.fxml;
    exports oop.worldsimulator;
}