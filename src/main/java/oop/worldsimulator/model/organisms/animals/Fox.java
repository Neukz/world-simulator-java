package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

import java.util.List;

public class Fox extends Animal {
    private static final int STRENGTH = 3;
    private static final int INITIATIVE = 7;
    private static final String SYMBOL = "🦊";
    private static final String SPECIES = "Fox";
    private static final boolean REGISTERED = register();


    public Fox(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        Position pos = getPosition();
        List<Position> neighbors = pos.getNeighbors();

        while (!neighbors.isEmpty()) {
            int i = RANDOM.nextInt(neighbors.size());
            Position candidate = neighbors.remove(i);
            setPosition(candidate);

            if (!pos.equals(getPosition()) && !encounteredStrongerOrganism()) {
                setPrevPosition(pos);
                break;
            }
        }
    }

    private boolean encounteredStrongerOrganism() {
        Organism other = world.getCollidingOrganism(this);
        if (other == null) {
            return false;
        }

        return other.getStrength() > this.getStrength();
    }


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Fox::new);
        return true;
    }
}