package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Animal;

public class Sheep extends Animal {
    public static final String SPECIES = "Sheep";

    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;
    private static final String SYMBOL = "🐑";


    public Sheep(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }
}