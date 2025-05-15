package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;
import oop.worldsimulator.model.factory.OrganismFactory;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

import java.util.Random;

public class Antelope extends Animal {
    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;
    private static final String SYMBOL = "🦌";
    private static final String SPECIES = "Antelope";
    private static final boolean REGISTERED = register();


    private final Random random = new Random();


    public Antelope(int x, int y, World world) {
        super(STRENGTH, INITIATIVE, SYMBOL, x, y, world);
    }

    @Override
    public String getSpecies() {
        return SPECIES;
    }

    @Override
    public void action() {
        Position pos = getPosition();
        do {
            Position randomNeighbor = pos.getRandomNeighbor(2);
            setPosition(randomNeighbor);
        } while (pos == getPosition());
        setPrevPosition(pos);
    }

    @Override
    public void collision(Organism other) {
        if (!(other instanceof Antelope)) {
            boolean shouldEscape = random.nextInt(100) < 50;    // 50% chance to escape from fight

            if (shouldEscape) {
                escape();
                return;
            }
        }

        super.collision(other);
    }

    private void escape() {
        Position escapeTarget = world.getRandomFreeNeighboringField(this);

        if (escapeTarget.equals(Position.INVALID_POSITION)) {
            return; // No space to escape
        }

        setPrevPosition(getPosition());
        setPosition(escapeTarget);
    }


    private static boolean register() {
        OrganismFactory.getInstance().registerType(SPECIES, Antelope::new);
        return true;
    }
}