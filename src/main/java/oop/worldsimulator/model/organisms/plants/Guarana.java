package oop.worldsimulator.model.organisms.plants;

import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Organism;
import oop.worldsimulator.model.organisms.Plant;

public class Guarana extends Plant {
    private static final int STRENGTH = 0;
    private static final String SYMBOL = "🍒";
    private static final String SPECIES = "Guarana";
    private static final boolean REGISTERED = register();


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


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Guarana::new);
        return true;
    }
}