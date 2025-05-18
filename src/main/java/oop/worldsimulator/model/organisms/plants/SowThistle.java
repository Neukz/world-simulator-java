package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Plant;

public class SowThistle extends Plant {
    public static final String SPECIES = "Sow Thistle";

    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🌱";

    public SowThistle(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        for (int i = 0; i < 3; i++) {
            super.action();
        }
    }
}