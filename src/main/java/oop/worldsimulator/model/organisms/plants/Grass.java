package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.World;
import oop.worldsimulator.model.organisms.Plant;

public class Grass extends Plant {
    public static final String SPECIES = "Grass";

    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🌿";


    public Grass(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }
}