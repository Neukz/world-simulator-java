package oop.worldsimulator.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.factory.*;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.animals.Human;
import oop.worldsimulator.model.worlds.*;

import java.io.*;
import java.util.*;

public class WorldController {
    private final static String SAVE_FILENAME = "world.ser";
    private final static int SQUARE_SIZE = 40;
    private final static int HEXAGON_SIZE = 25;
    private final static int EMOJI_SIZE = 24;
    // Support rendering emojis cross-platform
    private final static String EMOJI_FONT = switch (System.getProperty("os.name")) {
        case String os when os.startsWith("Windows") -> "Segoe UI Emoji";
        case String os when os.startsWith("Mac") -> "Apple Color Emoji";
        default -> "Noto Color Emoji";  // Linux
    };


    @FXML
    private GridPane squareWorldGrid;
    @FXML
    private Pane hexagonalWorldPane;
    @FXML
    private VBox logBox;
    @FXML
    private Button nextTurnButton;
    @FXML
    private Button saveWorldButton;
    @FXML
    private Button loadWorldButton;
    @FXML
    private Label turnLabel;

    private World world;
    private boolean turnInProgress = false;


    public void setWorld(World world) {
        this.world = world;
        drawWorld();  // Refresh after injection
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            if (world instanceof HexagonalWorld) {
                hexagonalWorldPane.getScene().setOnKeyPressed(this::onKeyPress);
            } else {
                squareWorldGrid.getScene().setOnKeyPressed(this::onKeyPress);
            }
        });
    }

    @FXML
    private void onNextTurnClick() {
        turnInProgress = true;

        // Disable during the turn
        nextTurnButton.setDisable(true);
        saveWorldButton.setDisable(true);
        loadWorldButton.setDisable(true);

        new Thread(() -> {
            world.nextTurn();

            // Update the UI after turn finishes
            Platform.runLater(() -> {
                turnLabel.setText("Turn: " + world.getTurnNo());
                drawWorld();
                printLogs();
                world.clearEventLog();

                // Re-enable after turn
                nextTurnButton.setDisable(false);
                saveWorldButton.setDisable(false);
                loadWorldButton.setDisable(false);

                turnInProgress = false;
            });
        }).start();
    }

    @FXML
    private void onSaveWorldClick() {
        try (FileOutputStream fos = new FileOutputStream(SAVE_FILENAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(world);

            addAndPrintLogs("World saved!");
        } catch (IOException e) {
            addAndPrintLogs("Error occurred while saving the world.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoadWorldClick() {
        try (FileInputStream fis = new FileInputStream(SAVE_FILENAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            world = (World) ois.readObject();

            addAndPrintLogs("World loaded from save!");
            turnLabel.setText("Turn: " + world.getTurnNo());
            drawWorld();    // Refresh view
        } catch (IOException | ClassNotFoundException e) {
            addAndPrintLogs("Error occurred while loading the world.");
            e.printStackTrace();
        }
    }

    private void onKeyPress(KeyEvent event) {
        Human human = Human.getInstance();

        if (human == null) {
            return;
        }

        // Use immortality
        if (event.getCode() == KeyCode.SPACE) {
            if (human.activateImmortality()) {
                addAndPrintLogs("Human has activated Immortality!");
            }

            return;
        }

        // Handle movement
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

    private void drawWorld() {
        // Clear both - in case another type of world was loaded
        squareWorldGrid.getChildren().clear();
        hexagonalWorldPane.getChildren().clear();

        if (world instanceof HexagonalWorld) {
            drawHexagonalWorld();
        } else {
            drawSquareWorld();
        }
    }

    private void drawSquareWorld() {
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

                squareWorldGrid.add(field, x, y);
            }
        }
    }

    private void drawHexagonalWorld() {
        double hexagonWidth = Math.sqrt(3) * HEXAGON_SIZE;
        double hexagonHeight = 2 * HEXAGON_SIZE;

        // Sort organisms by position
        List<Organism> organisms = world.getOrganisms();
        organisms.sort(Comparator.comparing(Organism::getPosition));

        Iterator<Organism> iterator = organisms.iterator();
        Organism current = iterator.hasNext() ? iterator.next() : null;

        // Draw fields
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                double windowX = x * hexagonWidth + (y % 2 == 1 ? hexagonWidth / 2 : 0);
                double windowY = y * (0.75 * hexagonHeight);

                StackPane field = new StackPane();
                field.setLayoutX(windowX);
                field.setLayoutY(windowY);
                field.setPrefSize(hexagonWidth, hexagonHeight);

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

                hexagonalWorldPane.getChildren().add(field);
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

    private void addAndPrintLogs(String entry) {
        world.logEvent(entry);
        printLogs();
        world.clearEventLog();
    }

    private void openOrganismSelectionDialog(Position position) {
        // Prevent adding new organisms mid-turn
        if (turnInProgress) {
            return;
        }

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

                addAndPrintLogs(species + " has been added at " + newOrganism.getPosition() + ".");
                drawWorld();    // Refresh view
            } else {
                addAndPrintLogs("Cannot create " + species + ".");
            }
        });
    }
}