package oop.worldsimulator.model;

import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

import java.util.*;

public class World {
    private static final String SAVE_FILENAME = "save.txt";


    private final int width, height;
    private final List<Organism> organisms = new ArrayList<>();
    private final List<Organism> toAdd = new ArrayList<>();
    private final List<String> eventLog = new ArrayList<>();
    private final Random random = new Random();


    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Organism> getOrganisms() {
        return new ArrayList<>(organisms);  // Defensive copy
    }

    public List<String> getEventLog() {
        return new ArrayList<>(eventLog);   // Defensive copy
    }

    public void queueOrganism(Organism organism) {
        toAdd.add(organism);
    }

    public void logEvent(String event) {
        eventLog.add(event);
    }

    public void clearEventLog() {
        eventLog.clear();
    }

    public void populate(Organism... organisms) {
        Collections.addAll(this.organisms, organisms);
    }

    public void nextTurn() {
        for (Organism o : organisms) {
            o.mature();
        }

        // Sort by initiative and age (natural order)
        Collections.sort(organisms);

        for (Organism organism : organisms) {
            // Skip dead organisms
            if (!organism.isAlive()) {
                continue;
            }

            organism.action();

            // If it's an Animal, check for collision
            if (organism instanceof Animal) {
                Organism other = getCollidingOrganism(organism);
                if (other != null) {
                    other.collision(organism);
                }
            }
        }

        // Remove dead organisms and add newly spawned
        organisms.removeIf(o -> !o.isAlive());
        toAdd.removeIf(o -> !o.isAlive());
        organisms.addAll(toAdd);
        toAdd.clear();

        validateUniquePositions();
    }

    public void validateUniquePositions() {
        Set<Position> seen = new HashSet<>();
        for (Organism o : organisms) {
            if (!seen.add(o.getPosition())) {
                System.err.println("WARNING: Multiple organisms at position " + o.getPosition());
            }
        }
        for (Organism o : toAdd) {
            if (!seen.add(o.getPosition())) {
                System.err.println("WARNING: Multiple organisms (including toAdd) at " + o.getPosition());
            }
        }
    }

    public Organism getCollidingOrganism(Organism organism) {
        for (Organism other : organisms) {
            if (other.isAlive() && !other.equals(organism) && other.getPosition().equals(organism.getPosition())) {
                return other;
            }
        }

        // Consider newly spawned organisms as well
        for (Organism other : toAdd) {
            if (other.isAlive() && !other.equals(organism) && other.getPosition().equals(organism.getPosition())) {
                return other;
            }
        }

        return null;
    }

    public Organism getOrganismAt(Position position) {
        for (Organism o : organisms) {
            if (o.isAlive() && o.getPosition().equals(position)) {
                return o;
            }
        }

        // Consider newly spawned organisms as well
        for (Organism o : toAdd) {
            if (o.isAlive() && o.getPosition().equals(position)) {
                return o;
            }
        }

        return null;
    }

    public Position getRandomFreeField() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        Position pos = new Position(x, y);

        while (getOrganismAt(pos) != null) {
            x = random.nextInt(width);
            y = random.nextInt(height);
            pos = new Position(x, y);
        }

        return pos;
    }

    public Position getRandomFreeNeighboringField(Organism organism) {
        Position position = organism.getPosition();
        List<Position> neighbors = position.getNeighbors();

        while (!neighbors.isEmpty()) {
            int i = random.nextInt(neighbors.size());
            Position randomNeighbor = neighbors.get(i);

            if (positionWithinBounds(randomNeighbor) && getOrganismAt(randomNeighbor) == null) {
                return randomNeighbor;
            }

            neighbors.remove(i);
        }

        return Position.INVALID_POSITION;   // All neighboring fields are occupied
    }

    public boolean positionWithinBounds(Position position) {
        int x = position.getX();
        int y = position.getY();

        return x >= 0 && y >= 0 && x < width && y < height;
    }
}