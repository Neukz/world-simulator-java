package oop.worldsimulator.model.organisms.animals;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.worlds.World;
import oop.worldsimulator.model.organisms.Animal;
import oop.worldsimulator.model.organisms.Organism;

public class Antelope extends Animal {
    public static final String SPECIES = "Antelope";

    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;
    private static final String SYMBOL = "🦌";


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
            Position randomNeighbor = world.getRandomNeighbor(pos, 2);
            setPosition(randomNeighbor);
        } while (pos == getPosition());

        setPrevPosition(pos);
    }

    @Override
    public void collision(Organism other) {
        if (!(other instanceof Antelope)) {
            boolean shouldEscape = RANDOM.nextInt(100) < 50;    // 50% chance to escape from fight

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
}