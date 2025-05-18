package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Animal;

public class Wolf extends Animal {
    public static final String SPECIES = "Wolf";

    private static final int STRENGTH = 9;
    private static final int INITIATIVE = 5;
    private static final String SYMBOL = "🐺";


    public Wolf(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }
}