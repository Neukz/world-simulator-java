package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.Plant;

public class Belladonna extends Plant {
    public static final String SPECIES = "Belladonna";

    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🫐";


    public Belladonna(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void collision(Organism other) {
        super.collision(other);
        other.kill(this);
    }
}