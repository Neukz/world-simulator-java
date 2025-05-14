package oop.worldsimulator.model;

import oop.worldsimulator.model.organisms.Organism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class World {
    private static final String SAVE_FILENAME = "save.txt";


    private final int width, height;
    private final List<Organism> organisms = new ArrayList<>();
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

    public void addOrganism(Organism organism) {
        organisms.add(organism);
    }

    public void populate(Organism... organisms) {
        for (Organism o : organisms) {
            addOrganism(o);
        }
    }

    public void makeTurn() {
        for (Organism organism : organisms) {
            organism.mature();
        }

        Collections.sort(organisms);

        for (Organism organism : organisms) {
            organism.action();
        }
    }

    public Organism getCollidingOrganism(Organism organism) {
        for (Organism other : organisms) {
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

    public boolean positionWithinBounds(Position position) {
        int x = position.getX();
        int y = position.getY();

        return x >= 0 && y >= 0 && x < width && y < height;
    }
}