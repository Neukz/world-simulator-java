package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Plant;

public class Grass extends Plant {
    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🌿";
    private static final String SPECIES = "Grass";
    private static final boolean REGISTERED = register();


    public Grass(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Grass::new);
        return true;
    }
}