package oop.worldsimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import oop.worldsimulator.model.factory.OrganismRegistry;
import oop.worldsimulator.model.organisms.animals.*;
import oop.worldsimulator.model.organisms.plants.*;
import oop.worldsimulator.model.worlds.*;

import java.util.function.UnaryOperator;

public class StartController {
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private ChoiceBox<String> worldTypeChoicebox;
    @FXML
    private CheckBox randomSeedCheckbox;


    @FXML
    private void initialize() {
        // Allow only positive integers in width and height fields
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*") && (newText.isEmpty() || Integer.parseInt(newText) > 0)) {
                return change;
            }

            return null;
        };

        widthField.setTextFormatter(new TextFormatter<>(integerFilter));
        heightField.setTextFormatter(new TextFormatter<>(integerFilter));
    }

    @FXML
    private void onStartClicked() {
        try {
            World world = setupWorld();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/oop/worldsimulator/view/world.fxml"));
            Parent root = loader.load();

            // Inject world into the controller
            WorldController controller = loader.getController();
            controller.setWorld(world);

            Stage stage = (Stage) widthField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Invalid input. Please check configuration.");
        }
    }

    private World setupWorld() {
        // Read configuration
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        String worldType = worldTypeChoicebox.getValue();
        boolean randomSeed = randomSeedCheckbox.isSelected();

        OrganismRegistry.registerAll();

        World world = switch (worldType) {
            case "Hexagonal" -> new HexagonalWorld(width, height);
            case "Square" -> new SquareWorld(width, height);
            default -> throw new IllegalArgumentException("Invalid world type");
        };

        if (randomSeed) {
            world.randomSeed();
        }

//        world.populate(
//                // Animals
//                Human.spawn(4, 0, world),
//                new Wolf(9, 3, world),
//                new Wolf(6, 1, world),
//                new Sheep(8, 1, world),
//                new Sheep(9, 5, world),
//                new Fox(6, 3, world),
//                new Fox(8, 9, world),
//                new Turtle(1, 1, world),
//                new Turtle(1, 3, world),
//                new Antelope(0, 8, world),
//                new Antelope(6, 8, world),
//
//                // Plants
//                new Grass(4, 2, world),
//                new SowThistle(4, 8, world),
//                new Guarana(2, 6, world),
//                new Belladonna(7, 6, world),
//                new SosnowskysHogweed(5, 5, world)
//        );

        return world;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}