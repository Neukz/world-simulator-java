package oop.worldsimulator.organisms.plants;

import oop.worldsimulator.World;
import oop.worldsimulator.organisms.Organism;

import java.util.Random;

public abstract class Plant extends Organism {
    // % chance (0-100)
    private static final int SOWING_PROBABILITY = 10;


    private final Random random = new Random();


    public Plant(int strength, String symbol, int x, int y, World world) {
        super(strength, 0, symbol, x, y, world);    // Plants have initiative 0
    }

    @Override
    public void action() {
        boolean canReproduce = random.nextInt(100) < SOWING_PROBABILITY;
        if (canReproduce) {
            reproduce();
        }
    }

    @Override
    public void collision(Organism other) {
        this.kill();    // Plants are eaten (die) on collision
    }
}