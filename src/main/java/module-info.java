module oop.worldsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens oop.worldsimulator to javafx.fxml;
    exports oop.worldsimulator;
    exports oop.worldsimulator.model;
    opens oop.worldsimulator.model to javafx.fxml;
    exports oop.worldsimulator.controller;
    opens oop.worldsimulator.controller to javafx.fxml;
}