package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.Plant;

import java.util.List;

public class SosnowskysHogweed extends Plant {
    public static final String SPECIES = "Sosnowsky's Hogweed";

    private static final int STRENGTH = 10;
    private static final String SYMBOL = "🍄";


    public SosnowskysHogweed(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        killNeighboringAnimals();
        super.action();
    }

    @Override
    public void collision(Organism other) {
        super.collision(other);
        other.kill(this);
    }

    private void killNeighboringAnimals() {
        List<Position> neighbors = getPosition().getNeighbors();

        for (Position neighbor : neighbors) {
            Organism organism = world.getOrganismAt(neighbor);

            if (organism instanceof Animal) {
                organism.kill(this);
            }
        }
    }
}