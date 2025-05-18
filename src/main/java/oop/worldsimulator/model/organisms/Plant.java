package oop.worldsimulator.model.organisms;

import oop.worldsimulator.model.worlds.World;

public abstract class Plant extends Organism {
    // % chance (0-100)
    private static final int SOWING_PROBABILITY = 10;


    public Plant(int strength, String symbol, int x, int y, World world) {
        super(strength, 0, symbol, x, y, world);    // Plants have initiative 0
    }

    @Override
    public void action() {
        boolean canReproduce = RANDOM.nextInt(100) < SOWING_PROBABILITY;
        if (canReproduce) {
            reproduce();
        }
    }

    @Override
    public void collision(Organism other) {
        this.kill(other);    // Plants are eaten (die) on collision
    }
}