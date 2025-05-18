package oop.worldsimulator.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.factory.OrganismFactory;
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
        Platform.runLater(() -> {
            if (world instanceof HexagonalWorld) {
                worldPane.getScene().setOnKeyPressed(this::handleKeyPress);
            } else {
                worldGrid.getScene().setOnKeyPressed(this::handleKeyPress);
            }
        });

        OrganismRegistry.registerAll();

        world.populate(
                // Animals
                Human.spawn(4, 0, world),
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
        nextTurnButton.setDisable(true);

        new Thread(() -> {
            world.nextTurn();

            // Update the UI after turn finishes
            Platform.runLater(() -> {
                drawWorld();
                printLogs();
                world.clearEventLog();

                nextTurnButton.setDisable(false);  // Re-enable after turn
            });
        }).start();
    }

    private void handleKeyPress(KeyEvent event) {
        Human human = Human.getInstance();

        if (human != null) {
            Human.Direction direction = switch (event.getCode()) {
                case W -> Human.Direction.UP;
                case S -> Human.Direction.DOWN;
                case A -> Human.Direction.LEFT;
                case D -> Human.Direction.RIGHT;
                case Q -> Human.Direction.UP_LEFT;
                case E -> Human.Direction.UP_RIGHT;
                case Z -> Human.Direction.DOWN_LEFT;
                case C -> Human.Direction.DOWN_RIGHT;
                default -> null;
            };

            if (direction != null) {
                // Avoid blocking the main thread
                Platform.runLater(() -> human.setDirection(direction));
            }
        }
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

                Position pos = new Position(x, y);

                // Enable adding an organism on a free cell
                if (current == null || !current.getPosition().equals(pos)) {
                    field.setOnMouseClicked(e -> openOrganismSelectionDialog(pos));
                }

                // Add organism's symbol if the field is occupied
                if (current != null && current.getPosition().equals(pos)) {
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

                Position pos = new Position(x, y);

                // Enable adding an organism on a free cell
                if (current == null || !current.getPosition().equals(pos)) {
                    field.setOnMouseClicked(e -> openOrganismSelectionDialog(pos));
                }

                // Add organism's symbol if the field is occupied
                if (current != null && current.getPosition().equals(pos)) {
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

    private void openOrganismSelectionDialog(Position position) {
        OrganismFactory factory = OrganismFactory.getInstance();
        List<String> types = factory.getRegisteredTypes();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(types.getFirst(), types);
        dialog.setTitle("Add Organism");
        dialog.setHeaderText("Select an organism to add at " + position + ".");
        dialog.setContentText("Organism:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(species -> {
            Organism newOrganism = factory.create(species, position.getX(), position.getY(), world);

            if (newOrganism != null) {
                world.queueOrganism(newOrganism);
                world.mergeOrganisms();

                drawWorld();    // Refresh view
            }
        });
    }
}