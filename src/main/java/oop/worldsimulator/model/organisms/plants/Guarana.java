package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.Plant;

public class Guarana extends Plant {
    public static final String SPECIES = "Guarana";

    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🍒";


    public Guarana(int x, int y, World world) {
        super(STRENGTH, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void collision(Organism other) {
        boostEater(other);
        super.collision(other);
    }

    private void boostEater(Organism eater) {
        int eaterStrength = eater.getStrength();
        eater.setStrength(eaterStrength + 3);
    }
}