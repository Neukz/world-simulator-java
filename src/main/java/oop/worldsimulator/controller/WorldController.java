package oop.worldsimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.factory.OrganismRegistry;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.animals.*;
import oop.worldsimulator.model.organisms.plants.*;
import oop.worldsimulator.model.worlds.*;

import java.util.*;

public class WorldController {
    private final static int SQUARE_SIZE = 40;
    private final static int HEXAGON_SIZE = 30;
    private final static int EMOJI_SIZE = 24;
    // Support rendering emojis cross-platform
    private final static String EMOJI_FONT = switch (System.getProperty("os.name")) {
        case String os when os.startsWith("Windows") -> "Segoe UI Emoji";
        case String os when os.startsWith("Mac") -> "Apple Color Emoji";
        default -> "Noto Color Emoji";  // Linux
    };


    @FXML private GridPane worldGrid;   // Square world
    @FXML private Pane worldPane;   // Hexagonal world
    @FXML private VBox logBox;
    @FXML private Button nextTurnButton;
    private final World world = new HexagonalWorld(10, 10);


    @FXML
    private void initialize() {
        OrganismRegistry.registerAll();

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
                new Grass(4, 2, world),
                new SowThistle(4, 8, world),
                new Guarana(2, 6, world),
                new Belladonna(7, 6, world),
                new SosnowskysHogweed(5, 5, world)
        );

//        world.randomSeed();

        drawWorld();
    }

    @FXML
    private void onNextTurnClick() {
        world.nextTurn();
        drawWorld();
        printLogs();
        world.clearEventLog();
    }

    private void drawWorld() {
        if (world instanceof HexagonalWorld) {
            drawHexagonalWorld();
        } else {
            drawSquareWorld();
        }
    }

    private void drawSquareWorld() {
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

                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.BEIGE);
                square.setStroke(Color.BLACK);

                field.getChildren().add(square);

                // Add organism's symbol if the field is occupied
                if (current != null && current.getPosition().equals(new Position(x, y))) {
                    Label symbol = new Label(current.getSymbol());
                    symbol.setFont(Font.font(EMOJI_FONT, EMOJI_SIZE));
                    field.getChildren().add(symbol);

                    current = iterator.hasNext() ? iterator.next() : null;
                }

                worldGrid.add(field, x, y);
            }
        }
    }

    private void drawHexagonalWorld() {
        worldPane.getChildren().clear();

        double hexWidth = Math.sqrt(3) * HEXAGON_SIZE;
        double hexHeight = 2 * HEXAGON_SIZE;

        // Sort organisms by position
        List<Organism> organisms = world.getOrganisms();
        organisms.sort(Comparator.comparing(Organism::getPosition));

        Iterator<Organism> iterator = organisms.iterator();
        Organism current = iterator.hasNext() ? iterator.next() : null;

        // Draw fields
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                double windowX = x * hexWidth + (y % 2 == 1 ? hexWidth / 2 : 0);
                double windowY = y * (0.75 * hexHeight);

                StackPane field = new StackPane();
                field.setLayoutX(windowX);
                field.setLayoutY(windowY);
                field.setPrefSize(hexWidth, hexHeight);

                Polygon hexagon = createHexagon(HEXAGON_SIZE);
                hexagon.setFill(Color.BEIGE);
                hexagon.setStroke(Color.BLACK);

                field.getChildren().add(hexagon);

                // Add organism's symbol if the field is occupied
                if (current != null && current.getPosition().equals(new Position(x, y))) {
                    Label symbol = new Label(current.getSymbol());
                    symbol.setFont(Font.font(EMOJI_FONT, EMOJI_SIZE));
                    field.getChildren().add(symbol);

                    current = iterator.hasNext() ? iterator.next() : null;
                }

                worldPane.getChildren().add(field);
            }
        }
    }

    private Polygon createHexagon(double radius) {
        Polygon hex = new Polygon();

        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30);
            double x = radius * Math.cos(angleRad);
            double y = radius * Math.sin(angleRad);

            hex.getPoints().addAll(x, y);
        }

        return hex;
    }

    private void printLogs() {
        logBox.getChildren().clear();

        for (String entry : world.getEventLog()) {
            Label log = new Label(entry);
            log.setWrapText(true);
            logBox.getChildren().add(log);
        }
    }
}