package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Plant;

public class SowThistle extends Plant {
    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🌱";
    private static final String SPECIES = "Sow Thistle";
    private static final boolean REGISTERED = register();


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


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, SowThistle::new);
        return true;
    }
}