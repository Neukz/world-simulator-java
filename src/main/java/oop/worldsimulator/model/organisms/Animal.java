package oop.worldsimulator.model.organisms;

import oop.worldsimulator.model.Position;
import oop.worldsimulator.model.World;

public abstract class Animal extends Organism {
    public Animal(int strength, int initiative, String symbol, int x, int y, World world) {
        super(strength, initiative, symbol, x, y, world);
    }

    @Override
    public void action() {
        Position pos = getPosition();
        do {
            Position randomNeighbor = pos.getRandomNeighbor();
            setPosition(randomNeighbor);
        } while (pos == getPosition());	// Repeat until a valid new position is set
        setPrevPosition(pos);
    }

    @Override
    public void collision(Organism other) {
        // If there's an organism of the same type, breed
        if (this.getClass() == other.getClass()) {
            // Revert other's move
            Position otherPrevPos = other.getPrevPosition();
            other.setPosition(otherPrevPos);

            reproduce();
            return;
        }

        // Fight
        if (this.getStrength() > other.getStrength()) {
            other.kill();
        } else {
            this.kill();
        }
    }
}