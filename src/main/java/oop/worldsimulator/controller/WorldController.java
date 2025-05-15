package oop.worldsimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.animals.*;
import oop.worldsimulator.model.organisms.plants.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class WorldController {
    private final static int FIELD_SIZE = 40;
    // Support rendering emojis cross-platform
    private final static String EMOJI_FONT = switch (System.getProperty("os.name")) {
        case String os when os.startsWith("Windows") -> "Segoe UI Emoji";
        case String os when os.startsWith("Mac") -> "Apple Color Emoji";
        default -> "Noto Color Emoji";  // Linux
    };


    @FXML private GridPane worldGrid;
    @FXML private Button nextTurnButton;
    private final World world = new World(10, 10);


    @FXML
    private void initialize() {
        world.populate(
                // Animals
                new Wolf(9, 3, world),
                new Wolf(6, 1, world),
                new Sheep(8, 1, world),
                new Sheep(9, 5, world),
                new Fox(6, 3, world),
                new Fox(8, 9, world),
                new Turtle(1, 1, world),
                new Turtle(1, 3, world),
                new Antelope(0, 8, world),
                new Antelope(6, 8, world),

                // Plants
                new Grass(4, 2, world)
        );

        drawWorld();
    }

    @FXML
    private void onNextTurnClick() {
        world.nextTurn();
        drawWorld();
    }

    private void drawWorld() {
        worldGrid.getChildren().clear();

        // Sort organisms by position
        List<Organism> organisms = world.getOrganisms();
        organisms.sort(Comparator.comparing(Organism::getPosition));

        Iterator<Organism> iterator = organisms.iterator();
        Organism current = iterator.hasNext() ? iterator.next() : null;

        // Draw fields
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                StackPane field = new StackPane();
                field.setPrefSize(FIELD_SIZE, FIELD_SIZE);
                field.setStyle("-fx-border-color: black;");

                // Add organism's symbol if the field is occupied
                if (current != null && current.getPosition().equals(new Position(x, y))) {
                    Label symbol = new Label(current.getSymbol());
                    symbol.setFont(Font.font(EMOJI_FONT, 24));
                    field.getChildren().add(symbol);

                    current = iterator.hasNext() ? iterator.next() : null;
                }

                worldGrid.add(field, x, y);
            }
        }
    }
}