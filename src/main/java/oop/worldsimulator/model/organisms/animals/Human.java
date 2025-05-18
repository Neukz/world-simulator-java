package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.worlds.World;

import java.util.Map;

public class Human extends Animal {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }


    public static final String SPECIES = "Human";

    private static final int STRENGTH = 5;
    private static final int INITIATIVE = 4;
    private static final String SYMBOL = "🚶‍♂️";


    // Singleton instance
    private static Human instance = null;


    private volatile Direction direction = null;
    private boolean canMove = false;
    private final Object lock = new Object();


    private Human(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        canMove = true;
        Position pos = getPosition();

        // Request input until moved to another field
        while (pos == getPosition()) {
            synchronized (lock) {
                while (direction == null) {
                    try {
                        lock.wait();    // Wait for the controller to set a valid direction
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            move();
        }

        setPrevPosition(pos);
        direction = null;
        canMove = false;
    }

    public void setDirection(Direction direction) {
        if (canMove) {
            synchronized (lock) {
                this.direction = direction;
                lock.notifyAll();   // Notify that the direction is set
            }
        }
    }

    private void move() {
        Map<Direction, Position> possibleMoves = world.getValidMoves(getPosition());
        Position newPos = possibleMoves.get(direction);

        if (newPos != null) {
            setPosition(newPos);
        }
    }


    public static Human getInstance() {
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public static Human spawn(int x, int y, World world) {
        if (instance != null) {
            return null;
        }

        instance = new Human(x, y, world);
        return instance;
    }
}