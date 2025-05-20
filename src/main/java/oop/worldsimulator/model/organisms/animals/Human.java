package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.worlds.World;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
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


    private transient Object lock = new Object();
    private volatile Direction direction = null;
    private boolean readyToMove = false;
    private boolean immortalityActive = false;
    private int immortalityDurationLeft = 0;
    private int immortalityCooldownLeft = 0;


    private Human(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    public void setDirection(Direction direction) {
        if (readyToMove) {
            synchronized (lock) {
                this.direction = direction;
                lock.notifyAll();   // Notify that the direction is set
            }
        }
    }

    @Override
    public void kill(Organism killer) {
        if (immortalityActive) {
            // Survive - move to a random free neighboring field
            Position pos = this.getPosition();
            Position randomNeighbor = world.getRandomFreeNeighboringField(this);

            if (randomNeighbor != Position.INVALID_POSITION) {
                this.setPosition(randomNeighbor);
                this.setPrevPosition(pos);
            }
        } else {
            super.kill(killer);
        }
    }

    @Override
    public void action() {
        readyToMove = true;
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
        readyToMove = false;

        updateImmortality();
    }

    public boolean activateImmortality() {
        synchronized (lock) {
            if (!immortalityActive && immortalityCooldownLeft == 0) {
                immortalityActive = true;
                immortalityDurationLeft = 5;

                return true;    // Activated
            }

            return false;   // Cannot activate
        }
    }

    private void move() {
        Map<Direction, Position> moves = world.getValidMoves(getPosition());
        Position newPos = moves.get(direction);

        if (newPos != null) {
            setPosition(newPos);
        }
    }

    private void updateImmortality() {
        // Handle immortality duration and cooldown
        if (immortalityActive) {
            immortalityDurationLeft--;

            if (immortalityDurationLeft == 0) {
                immortalityActive = false;
                immortalityCooldownLeft = 5;

                world.logEvent("Immortality expired!");
            } else {
                world.logEvent("Immortality active for " + immortalityDurationLeft + " more turns.");
            }
        } else if (immortalityCooldownLeft > 0) {
            immortalityCooldownLeft--;

            if (immortalityCooldownLeft == 0) {
                world.logEvent("Immortality available!");
            } else {
                world.logEvent("Immortality available in " + immortalityCooldownLeft + " turns.");
            }
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        lock = new Object();    // Restore lock (Object is not Serializable)
    }

    @Serial
    private Object readResolve() {
        // Restore the saved instance
        instance = this;
        return instance;
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