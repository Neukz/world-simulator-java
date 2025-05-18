package oop.worldsimulator.model.worlds;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

import java.util.*;

public abstract class World {
    private static final String SAVE_FILENAME = "save.txt";

    protected static final Random RANDOM = new Random();


    private final int width, height;
    private final List<Organism> organisms = new ArrayList<>();
    private final List<Organism> toAdd = new ArrayList<>();
    private final List<String> eventLog = new ArrayList<>();


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

    public void randomSeed() {
        OrganismFactory factory = OrganismFactory.getInstance();
        List<String> types = factory.getRegisteredTypes();

        // Create 2 organisms of each type (except Human)
        for (String species : types) {
            for (int i = 0; i < 2; i++) {
                Position pos = getRandomFreeField();
                Organism organism = factory.create(species, pos.getX(), pos.getY(), this);

                if (organism != null) {
                    organisms.add(organism);
                }
            }
        }
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

    public Position getRandomFreeNeighboringField(Organism organism) {
        List<Position> neighbors = getNeighbors(organism.getPosition());

        while (!neighbors.isEmpty()) {
            int i = RANDOM.nextInt(neighbors.size());
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

    public List<Position> getNeighbors(Position position) {
        return getNeighbors(position, 1);
    }

    public abstract List<Position> getNeighbors(Position position, int range);

    public Position getRandomNeighbor(Position position) {
        return getRandomNeighbor(position, 1);
    }

    public Position getRandomNeighbor(Position position, int range) {
        List<Position> neighbors = getNeighbors(position, range);
        return neighbors.get(RANDOM.nextInt(neighbors.size()));
    }

    private Position getRandomFreeField() {
        Position pos;

        do {
            pos = new Position(RANDOM.nextInt(width), RANDOM.nextInt(height));
        } while (getOrganismAt(pos) != null);

        return pos;
    }
}