package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Animal;

public class Sheep extends Animal {
    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;
    private static final String SYMBOL = "🐑";
    private static final String SPECIES = "Sheep";
    private static final boolean REGISTERED = register();


    public Sheep(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Sheep::new);
        return true;
    }
}